package com.example.todo.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.model.dao.CategoriesDao;
import com.example.todo.data.model.dao.TasksDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Category.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TasksDao tasksDao();

    public abstract CategoriesDao categoriesDao();

    private static TaskDatabase instance;
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static TaskDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (TaskDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, "task_database")
                            .build();
                }
            }
        }
        return instance;
    }

    public static ExecutorService getExecutor() {
        return executor;
    }
}
