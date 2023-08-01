package com.example.todo.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.data.model.Category;

import java.util.List;

@Dao
public interface CategoriesDao {
    @Insert(entity = Category.class)
    public long insertCategory(Category category);

    @Update(entity = Category.class)
    public void updateCategory(Category category);

    @Query("SELECT * FROM categories")
    public LiveData<List<Category>> getCategories();

    @Query("SELECT categories.id AS id, categories.name AS name, COUNT(tasks.id) AS task_count, " +
            "SUM(CASE WHEN tasks.completedAt IS NOT NULL THEN 1 ELSE 0 END) AS completed_task_count " +
            "FROM categories LEFT JOIN tasks ON categories.id = tasks.category_id " +
            "GROUP BY categories.id " +
            "ORDER BY CASE WHEN SUM(CASE WHEN tasks.completedAt IS NOT NULL THEN 1 ELSE 0 END) > 0 THEN 1 ELSE 0 END, categories.id")
    public LiveData<List<CategoryWithTaskCount>> getCategoriesWithTaskCount();

    public static class CategoryWithTaskCount {
        @Embedded
        public Category category;
        public int task_count;
        public int completed_task_count;
    }
}
