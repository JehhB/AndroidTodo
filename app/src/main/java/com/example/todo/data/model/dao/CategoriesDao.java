package com.example.todo.data.model.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.data.model.Category;

import java.util.List;
import java.util.Objects;

@Dao
public interface CategoriesDao {
    @Insert(entity = Category.class)
    long insertCategory(Category category);

    @Update(entity = Category.class)
    void updateCategory(Category category);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getCategories();

    @Query("SELECT categories.id AS id, categories.name AS name, COUNT(tasks.id) AS task_count, " +
            "SUM(CASE WHEN tasks.completedAt IS NOT NULL THEN 1 ELSE 0 END) AS completed_task_count " +
            "FROM categories LEFT JOIN tasks ON categories.id = tasks.category_id " +
            "GROUP BY categories.id " +
            "ORDER BY CASE WHEN SUM(CASE WHEN tasks.completedAt IS NOT NULL THEN 1 ELSE 0 END) > 0 THEN 1 ELSE 0 END, categories.id")
    LiveData<List<CategoryWithTaskCount>> getCategoriesWithTaskCount();

    class CategoryWithTaskCount {
        @Embedded
        public Category category;
        public int task_count;
        public int completed_task_count;

        @Override
        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof CategoryWithTaskCount)) return false;
            CategoryWithTaskCount another = (CategoryWithTaskCount) obj;
            return task_count == another.task_count &&
                    completed_task_count == another.completed_task_count &&
                    Objects.equals(category, another.category);
        }
    }
}
