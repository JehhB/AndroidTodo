package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Task;

import java.util.List;

public interface TasksRepository {
    void insertTask(Task task);
    void updateTask(Task task);
    LiveData<List<Task>> getTasks();
    LiveData<List<Task>> getTasksFromCategory(long categoryId);
}
