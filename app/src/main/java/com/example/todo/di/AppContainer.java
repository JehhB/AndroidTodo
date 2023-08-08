package com.example.todo.di;

import android.content.Context;

import androidx.room.Room;

import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.CategoriesRepositoryImpl;
import com.example.todo.data.repository.TasksRepository;
import com.example.todo.data.repository.TasksRepositoryImpl;
import com.example.todo.data.source.local.TaskDatabase;

import java.util.concurrent.ExecutorService;

public class AppContainer {
    private final TaskDatabase taskDatabase;
    private final ExecutorService taskDatabaseExecutor;
    private final TasksRepository tasksRepository;
    private final CategoriesRepository categoriesRepository;

    public AppContainer(Context context) {
        taskDatabase = TaskDatabase.getInstance(context);
        taskDatabaseExecutor = TaskDatabase.getExecutor();
        tasksRepository = new TasksRepositoryImpl(taskDatabase.tasksDao(), taskDatabaseExecutor);
        categoriesRepository = new CategoriesRepositoryImpl(taskDatabase.categoriesDao(), taskDatabaseExecutor);
    }

    public TaskDatabase getTaskDatabase() {
        return taskDatabase;
    }

    public ExecutorService getTaskDatabaseExecutor() {
        return taskDatabaseExecutor;
    }

    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }

    public CategoriesRepository getCategoriesRepository() {
        return categoriesRepository;
    }
}
