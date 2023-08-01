package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.dao.CategoriesDao;

import java.util.List;

public interface CategoriesRepository {
    void insertCategory(Category category);
    void updateCategory(Category category);
    LiveData<List<Category>> getCategories();
    LiveData<List<CategoriesDao.CategoryWithTaskCount>> getCategoriesWithTaskCount();
}
