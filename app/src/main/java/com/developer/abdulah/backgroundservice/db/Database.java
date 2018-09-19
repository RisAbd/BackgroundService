package com.developer.abdulah.backgroundservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by dev on 9/17/18.
 */

public class Database extends SQLiteOpenHelper {

    public static final String NAME = "data.db";
    public static final int VERSION = 1;

    public Database(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Task.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Task createTask(@Nullable String name, @Nullable String status) {
        SQLiteDatabase db = getWritableDatabase();
        Task t = Task.create(db, name, status, null);
        db.close();
        return t;
    }

    public void save(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        task.save(db);
        db.close();
    }

    public void delete(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        task.delete(db);
        db.close();
    }

    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Task> all = Task.getAll(db);
        db.close();
        return all;
    }

    public void delete(ArrayList<Task> tasks) {
        SQLiteDatabase db = getWritableDatabase();
        for (Task t: tasks) {
            t.delete(db);
        }
        db.close();
    }

}
