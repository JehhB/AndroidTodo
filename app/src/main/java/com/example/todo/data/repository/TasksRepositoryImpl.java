package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Task;
import com.example.todo.data.model.dao.TasksDao;
import com.example.todo.data.source.local.TaskDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class TasksRepositoryImpl implements TasksRepository {
    private final TasksDao tasksDao;
    private final ExecutorService executor;

    public TasksRepositoryImpl(TasksDao tasksDao, ExecutorService executor) {
        this.tasksDao = tasksDao;
        this.executor = executor;
    }

    @Override
    public void insertTask(Task task) {
        executor.execute(() -> tasksDao.insertTask(task));
    }

    @Override
    public void updateTask(Task task) {
        executor.execute(() -> tasksDao.updateTask(task));
    }

    @Override
    public LiveData<List<Task>> getTasks() {
        return tasksDao.getTasks();
    }

    @Override
    public LiveData<List<Task>> getTasksFromCategory(long categoryId) {
        return tasksDao.getTasks();
    }
}
