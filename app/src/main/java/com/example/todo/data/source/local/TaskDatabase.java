package com.example.todo.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.model.dao.CategoriesDao;
import com.example.todo.data.model.dao.TasksDao;

@Database(entities = {Task.class, Category.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TasksDao tasksDao();

    public abstract CategoriesDao categoriesDao();

    private static TaskDatabase instance;

    public static TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, "task_database")
                    .build();
        }
        return instance;
    }
}
