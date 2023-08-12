package com.example.todo.data.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class Task {
    @PrimaryKey(autoGenerate = true)
    private final Long id;
    @ColumnInfo(name = "category_id")
    private Long categoryId;

    private String task;
    private String description;

    @ColumnInfo(name = "createdAt", defaultValue = "CURRENT_TIMESTAMP")
    private final Long createdAt;
    @ColumnInfo(name = "dueAt", defaultValue = "CURRENT_TIMESTAMP")
    private Long dueAt;
    @ColumnInfo(name = "completedAt", defaultValue = "CURRENT_TIMESTAMP")
    private Long completedAt;

    public Task(Long id, Long categoryId, String task, String description, Long createdAt, @Nullable Long dueAt, @Nullable Long completedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.task = task;
        this.description = description;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.completedAt = completedAt;
    }

    @Ignore
    public Task(Long categoryId, String task, String description, @Nullable Long dueAt, @Nullable Long completedAt) {
        this.id = null;
        this.categoryId = categoryId;
        this.task = task;
        this.description = description;
        this.createdAt = null;
        this.dueAt = dueAt;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getDueAt() {
        return dueAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueAt(Long dueAt) {
        this.dueAt = dueAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
    }

    @Ignore
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Task)) return false;

        Task other = (Task) obj;
        return Objects.equals(this.getId(), other.getId()) ||
                Objects.equals(this.getCategoryId(), other.getCategoryId()) ||
                Objects.equals(this.getTask(), other.getTask()) ||
                Objects.equals(this.getDescription(), other.getDescription()) ||
                Objects.equals(this.getCreatedAt(), other.getCreatedAt()) ||
                Objects.equals(this.getCompletedAt(), other.getCompletedAt()) ||
                Objects.equals(this.getDueAt(), other.getDueAt());
    }
}