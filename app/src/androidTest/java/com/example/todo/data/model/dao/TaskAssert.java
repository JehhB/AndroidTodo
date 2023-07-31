package com.example.todo.data.model.dao;

import com.example.todo.data.model.Task;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

public class TaskAssert extends AbstractObjectAssert<TaskAssert, Task> {
    private TaskAssert(Task task) {
        super(task, TaskAssert.class);
    }

    public static TaskAssert assertThat(Task task) {
        return new TaskAssert(task);
    }

    public TaskAssert hasId(Long id) {
        Assertions.assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public TaskAssert hasCategoryId(Long categoryId) {
        Assertions.assertThat(actual.getCategoryId()).isEqualTo(categoryId);
        return this;
    }

    public TaskAssert hasDueAt(Long dueAt) {
        Assertions.assertThat(actual.getDueAt()).isEqualTo(dueAt);
        return this;
    }

    public TaskAssert hasCompletedAt(Long completedAt) {
        Assertions.assertThat(actual.getCompletedAt()).isEqualTo(completedAt);
        return this;
    }

    public TaskAssert hasCreatedAt(Long createdAt) {
        Assertions.assertThat(actual.getCreatedAt()).isEqualTo(createdAt);
        return this;
    }

    public TaskAssert hasTask(String task) {
        Assertions.assertThat(actual.getTask()).isEqualTo(task);
        return this;
    }

    public TaskAssert hasDescription(String description) {
        Assertions.assertThat(actual.getDescription()).isEqualTo(description);
        return this;
    }
}
