package com.example.todo.data.repository;

import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.dao.CategoriesDao;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class CategoriesRepositoryImpl implements CategoriesRepository {
    private final CategoriesDao categoriesDao;
    private final ExecutorService executor;

    public CategoriesRepositoryImpl(CategoriesDao categoriesDao, ExecutorService executor) {
        this.categoriesDao = categoriesDao;
        this.executor = executor;

    }

    @Override
    public void insertCategory(Category category) {
        executor.execute(() -> categoriesDao.insertCategory(category));
    }

    @Override
    public void updateCategory(Category category) {
        executor.execute(() -> categoriesDao.updateCategory(category));
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return categoriesDao.getCategories();
    }

    @Override
    public LiveData<List<CategoriesDao.CategoryWithTaskCount>> getCategoriesWithTaskCount() {
        return categoriesDao.getCategoriesWithTaskCount();
    }
}
