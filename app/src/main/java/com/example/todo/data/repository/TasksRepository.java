package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Task;

import java.util.List;
import java.util.function.Consumer;

public interface TasksRepository {
    void insertTask(Task task, Consumer<Long> callback);

    void insertTask(Task task);

    void deleteTask(Task task);

    void updateTask(Task task);

    LiveData<List<Task>> getTasks();

    LiveData<List<Task>> getTasksFromCategory(long categoryId);
}
