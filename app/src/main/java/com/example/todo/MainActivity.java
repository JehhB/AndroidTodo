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
        CategoryAdapter adapter = new CategoryAdapter(this);

        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        layoutMangager.setOrientation(LinearLayoutManager.HORIZONTAL);


        list.setAdapter(adapter);
        list.setLayoutManager(layoutMangager);
    }

    private void setupTaskList() {
        RecyclerView list = findViewById(R.id.rvTaskList);
        TaskAdapter adapter = new TaskAdapter();

        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        layoutMangager.setOrientation(LinearLayoutManager.VERTICAL);

        list.setAdapter(adapter);
        list.setLayoutManager(layoutMangager);
    }
}