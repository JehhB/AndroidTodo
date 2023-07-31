package com.example.todo.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.data.model.Task;

import java.util.List;

@Dao
public interface TasksDao {
    @Insert
    public long insertTask(Task task);

    @Update
    public void updateTask(Task task);

    @Query("SELECT * FROM tasks")
    public LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM tasks WHERE category_id = :categoryId")
    public LiveData<List<Task>> getTasksFromCategory(long categoryId);
}
