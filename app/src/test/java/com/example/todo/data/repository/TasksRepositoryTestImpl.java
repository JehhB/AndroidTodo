package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.todo.data.model.Task;
import com.example.todo.data.repository.TasksRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TasksRepositoryTestImpl implements TasksRepository {
    private final List<Task> tasksList;
    private final MutableLiveData<List<Task>> tasksLiveData;

    public TasksRepositoryTestImpl() {
        tasksList = new ArrayList<>();
        tasksLiveData = new MutableLiveData<>(tasksList);
    }

    public List<Task> getTasksList() {
        return tasksList;
    }

    @Override
    public void insertTask(Task task) {
        tasksList.add(task);
        tasksLiveData.setValue(tasksList);
    }

    @Override
    public void updateTask(Task task) {
        for (int i = 0; i < tasksList.size(); ++i) {
            if (tasksList.get(i).getId() == task.getId()) {
                tasksList.set(i, task);
                tasksLiveData.setValue(tasksList);
                return;
            }
        }
    }

    @Override
    public LiveData<List<Task>> getTasks() {
        return tasksLiveData;
    }

    @Override
    public LiveData<List<Task>> getTasksFromCategory(long categoryId) {
        return Transformations.map(tasksLiveData, tasks ->
                tasks.stream()
                        .filter(task -> task.getCategoryId() == categoryId)
                        .collect(Collectors.toList())
        );
    }
}
