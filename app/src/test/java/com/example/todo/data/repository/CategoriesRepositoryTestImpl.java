package com.example.todo.data.repository;

import static com.example.todo.data.model.dao.CategoriesDao.CategoryWithTaskCount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.todo.data.model.Category;
import com.example.todo.data.repository.CategoriesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesRepositoryTestImpl implements CategoriesRepository {
    private final List<Category> categoryList;
    private final MutableLiveData<List<Category>> categoryLivedata;

    public CategoriesRepositoryTestImpl() {
        categoryList = new ArrayList<>();
        categoryLivedata = new MutableLiveData<>(categoryList);
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    @Override
    public void insertCategory(Category category) {
        categoryList.add(category);
        categoryLivedata.setValue(categoryList);
    }

    @Override
    public void updateCategory(Category category) {
        for (int i = 0; i < categoryList.size(); ++i) {
            if (categoryList.get(i).getId() == category.getId()) {
                categoryList.set(i, category);
                categoryLivedata.setValue(categoryList);
                return;
            }
        }
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return categoryLivedata;
    }

    @Override
    public LiveData<List<CategoryWithTaskCount>> getCategoriesWithTaskCount() {
        return Transformations.map(categoryLivedata, categories ->
                categories.stream()
                        .map(category -> {
                            CategoryWithTaskCount temp = new CategoryWithTaskCount();
                            temp.task_count = 0;
                            temp.completed_task_count = 0;
                            temp.category = category;
                            return temp;
                        }).collect(Collectors.toList())
        );
    }
}
