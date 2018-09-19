package com.developer.abdulah.backgroundservice.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dev on 9/17/18.
 */

public class Task implements Cloneable {

    final static int NO_ID = -1;

    int id;
    int created;
    String name, status;

    static void createTable(SQLiteDatabase db) {
        db.execSQL("create table tasks (" +
                "id integer primary key, " +
                "name text, " +
                "created int not null default (cast(strftime('%s', 'now') as int)), " +
                "status text not null default 'unknown'" +
                ");");
    }

    static Task create(SQLiteDatabase db, @Nullable String name, @Nullable String status, @Nullable Integer created) {
        int taskId;
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        if (status != null) {
            cv.put("status", status);
        }
        if (created != null) {
            cv.put("created", created);
        }
        taskId = (int) db.insert("tasks", null, cv);
        return get(db, taskId);
    }

    static Task get(SQLiteDatabase db, int taskId) {
        Cursor c = db.query("tasks", new String[] {"id", "created", "name", "status"}, "id = ?", new String[] {taskId+""}, null, null, null);
        c.moveToFirst();
        Task t = new Task(
                c.getInt(0),
                c.getInt(1),
                c.getString(2),
                c.getString(3)
        );
        c.close();
        return t;
    }

    static ArrayList<Task> getAll(SQLiteDatabase db) {
        ArrayList<Task> out = new ArrayList<>();
        Cursor c = db.query("tasks", new String[] {"id", "created", "name", "status"}, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                out.add(new Task(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2),
                        c.getString(3)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return out;
    }

    public Task(@Nullable String name, @Nullable String status) {
        this(NO_ID, (int) (new Date().getTime() / 1000), name, status);
    }

    Task(int id, int created, String name, String status) {
        this.id = id;
        this.created = created;
        this.status = status;
        this.name = name;
    }

    void save(SQLiteDatabase db) {
        if (id != NO_ID) {
            ContentValues cv = new ContentValues(4);
            cv.put("created", created);
            cv.put("status", status);
            cv.put("name", name);
            int i = db.update("tasks", cv, "id = ?", new String[]{id + ""});
        } else {
            copyFrom(create(db, name, status, created));
        }
    }

    void delete(SQLiteDatabase db) {
        if (id == NO_ID) {
            return;
        }
        int i = db.delete("tasks", "id = ?", new String[] {id+""});
        id = NO_ID;
    }

    private void copyFrom(Task t) {
        this.name = t.name;
        this.id = t.id;
        this.created = t.created;
        this.status = t.status;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public int getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
