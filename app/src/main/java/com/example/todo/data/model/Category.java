package com.example.todo.data.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private final Long id;
    private String name;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Category(String name) {
        this.id = null;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Category)) return false;
        Category another = (Category) obj;
        return Objects.equals(id, another.id) &&
                Objects.equals(name, another.name);
    }
}
