package com.example.todo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.model.dao.CategoriesDao;
import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.TasksRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
    private final TasksRepository tasksRepository;
    private final CategoriesRepository categoriesRepository;

    private final LiveData<List<Task>> tasks;
    private final MutableLiveData<Category> selectedCategory;

    public MainViewModel(TasksRepository tasksRepository, CategoriesRepository categoriesRepository) {
        super();

        this.tasksRepository = tasksRepository;
        this.categoriesRepository = categoriesRepository;

        this.selectedCategory = new MutableLiveData<>(null);
        this.tasks = Transformations.switchMap(selectedCategory, category -> {
            if (category == null) {
                return this.tasksRepository.getTasks();
            } else {
                return this.tasksRepository.getTasksFromCategory(category.getId());
            }
        });
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category category) {
        Category currentlySelected = selectedCategory.getValue();
        if (currentlySelected == null || currentlySelected.getId() != category.getId()) {
            selectedCategory.setValue(category);
        } else {
            selectedCategory.setValue(null);
        }
    }

    public void insertTask(Task task) {
        tasksRepository.insertTask(task);
    }

    public void deleteTask(Task task) {
        tasksRepository.deleteTask(task);
    }

    public void updateTask(Task task) {
        tasksRepository.updateTask(task);
    }

    public void insertCategory(Category category) {
        categoriesRepository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        categoriesRepository.updateCategory(category);
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesRepository.getCategories();
    }

    public LiveData<List<CategoriesDao.CategoryWithTaskCount>> getCategoriesWithTaskCount() {
        return categoriesRepository.getCategoriesWithTaskCount();
    }
}