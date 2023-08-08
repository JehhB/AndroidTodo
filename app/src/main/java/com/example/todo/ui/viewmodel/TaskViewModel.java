package com.example.todo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.TasksRepository;

import java.util.List;

public class TaskViewModel extends ViewModel {
    private final Task task;
    private final TasksRepository tasksRepository;
    private final CategoriesRepository categoriesRepository;

    public TaskViewModel(Task task, TasksRepository tasksRepository, CategoriesRepository categoriesRepository) {
        this.task = task;
        this.tasksRepository = tasksRepository;
        this.categoriesRepository = categoriesRepository;
    }

    public boolean isNew() {
        return task.getId() == 0;
    }

    public void saveTask() {
        if (isNew()) {
            tasksRepository.insertTask(task);
        } else {
            tasksRepository.updateTask(task);
        }
    }

    public void setTask(String task) {
        this.task.setTask(task);
    }

    public void setDescription(String description) {
        task.setDescription(description);
    }

    public void setCategory(long category_id) {
        task.setCategoryId(category_id);
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesRepository.getCategories();
    }
}
