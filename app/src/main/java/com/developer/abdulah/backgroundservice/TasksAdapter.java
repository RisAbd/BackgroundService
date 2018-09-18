package com.developer.abdulah.backgroundservice;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.developer.abdulah.backgroundservice.db.Database;
import com.developer.abdulah.backgroundservice.db.Task;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dev on 9/18/18.
 */

public class TasksAdapter extends BaseAdapter implements Button.OnClickListener {

    ArrayList<Task> tasks;
    Context context;
    LayoutInflater inflater;

    public TasksAdapter(Context context, LayoutInflater inflater, ArrayList<Task> tasks) {
        this.inflater = inflater;
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public void onClick(View v) {
        Task t = (Task) v.getTag();
        Database db = new Database(context);
        db.delete(t);
        tasks = db.getAllTasks();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.task_item, parent, false);
            ((Button) view.findViewById(R.id.deleteTask)).setOnClickListener(this);
        }

        Task t = getItem(position);
        ((Button) view.findViewById(R.id.deleteTask)).setTag(t);
        ((TextView) view.findViewById(R.id.taskNameTextView)).setText(t.getName() + "(" + t.getId() + ")");
        ((TextView) view.findViewById(R.id.taskStatusTextView)).setText(t.getStatus());
        ((TextView) view.findViewById(R.id.taskCreatedTextView)).setText(new Date(t.getCreated()*1000)+"");

        return view;
    }
}
