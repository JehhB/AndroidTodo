package com.example.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.ui.adapter.CategoryAdapter;
import com.example.todo.ui.adapter.TaskAdapter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCategoryList();
        setupTaskList();
        setupDrawer();
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

    private void setupDrawer() {
        ImageButton btnDrawer = findViewById(R.id.btnDrawer);
        btnDrawer.setOnClickListener(this::showDrawer);

        ImageButton btnDrawerMinimize = findViewById(R.id.btnDrawerMinimize);
        btnDrawerMinimize.setOnClickListener(this::hideDrawer);
        hideDrawer(null);
    }

    private void showDrawer(View view) {
        View drawer = findViewById(R.id.drawer);
        drawer.setVisibility(View.VISIBLE);
        drawer
                .animate()
                .translationX(0)
                .setDuration(250);
    }

    private void hideDrawer(View view) {
        View drawer = findViewById(R.id.drawer);
        drawer
                .animate()
                .translationX(-drawer.getWidth())
                .withEndAction(() -> drawer.setVisibility(View.INVISIBLE))
                .setDuration(200);
    }
}