package com.example.todo.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private final Long id;
    public final String name;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Category(String name) {
        this.id = null;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
