package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCategoryList();
        setupTaskList();
    }

    private void setupCategoryList() {
        RecyclerView list = findViewById(R.id.rvCategoryList);
        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        CategoryAdapter adapter = new CategoryAdapter();

        list.setAdapter(adapter);
        list.setLayoutManager(layoutMangager);
    }

    private void setupTaskList() {
        RecyclerView list = findViewById(R.id.rvTaskList);
        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        TaskAdapter adapter = new TaskAdapter();

        list.setAdapter(adapter);
        list.setLayoutManager(layoutMangager);
    }
}