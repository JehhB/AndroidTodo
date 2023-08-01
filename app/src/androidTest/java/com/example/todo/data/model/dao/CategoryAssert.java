package com.example.todo.data.model.dao;

import com.example.todo.data.model.Category;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;

public class CategoryAssert extends AbstractObjectAssert<CategoryAssert, Category> {
    private CategoryAssert(Category category) {
        super(category, CategoryAssert.class);
    }

    public static CategoryAssert assertThat(Category category) {
        return new CategoryAssert(category);
    }

    public CategoryAssert hasId(Long id) {
        Assertions.assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public CategoryAssert hasName(String name) {
        Assertions.assertThat(actual.getName()).isEqualTo(name);
        return this;
    }
}
