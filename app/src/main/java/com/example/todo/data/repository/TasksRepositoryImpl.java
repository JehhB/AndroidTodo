package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Task;
import com.example.todo.data.model.dao.TasksDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class TasksRepositoryImpl implements TasksRepository {
    private final TasksDao tasksDao;
    private final ExecutorService executor;

    public TasksRepositoryImpl(TasksDao tasksDao, ExecutorService executor) {
        this.tasksDao = tasksDao;
        this.executor = executor;
    }

    @Override
    public void insertTask(Task task, Consumer<Long> callback) {
        executor.execute(() -> {
            long id = tasksDao.insertTask(task);
            if (callback != null) {
                callback.accept(id);
            }
        });
    }

    @Override
    public void insertTask(Task task) {
        insertTask(task, null);
    }

    @Override
    public void deleteTask(Task task) {
        executor.execute(() -> {
            tasksDao.deleteTask(task);
        });
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
        return tasksDao.getTasksFromCategory(categoryId);
    }
}
