package com.developer.abdulah.backgroundservice;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.developer.abdulah.backgroundservice.db.Database;
import com.developer.abdulah.backgroundservice.services.BackgroundService;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView tasksListView;

    TasksAdapter tasksAdapter;

    SwipeRefreshLayout refreshLayout;

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);

        tasksListView = findViewById(R.id.tasksList);
        tasksAdapter = new TasksAdapter(this, getLayoutInflater(), db.getAllTasks());
        tasksListView.setAdapter(tasksAdapter);

        refreshLayout = findViewById(R.id.refreshTasksLayout);
        refreshLayout.setOnRefreshListener(this);
//        startService();
    }

    void startService() {
        Intent intent = new Intent(this, BackgroundService.class);
//        bindService(intent, new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//            }
//        }, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
    void stopService() {
        stopService(new Intent(this, BackgroundService.class));
    }
    void refreshList() {
        tasksAdapter.tasks = db.getAllTasks();

        tasksAdapter.notifyDataSetChanged();

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
    void deleteTasks() {
        db.delete(tasksAdapter.tasks);
        tasksAdapter.tasks = db.getAllTasks();
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startServiceMenuItem:
                startService();
                return true;
            case R.id.stopServiceMenuItem:
                stopService();
                return true;
            case R.id.menuRefreshTasks:
                refreshList();
                return true;
            case R.id.deleteTasksMenuItem:
                deleteTasks();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
