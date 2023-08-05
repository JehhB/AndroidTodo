package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.CategoriesRepositoryImpl;
import com.example.todo.data.repository.TasksRepository;
import com.example.todo.data.repository.TasksRepositoryImpl;
import com.example.todo.data.source.local.TaskDatabase;
import com.example.todo.ui.adapter.CategoryAdapter;
import com.example.todo.ui.adapter.TaskAdapter;
import com.example.todo.ui.viewmodel.MainViewModel;

import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TaskDatabase taskDatabase = TaskDatabase.getInstance(getApplicationContext());
        ExecutorService executor = TaskDatabase.getExecutor();

        TasksRepository tasksRepository = new TasksRepositoryImpl(taskDatabase.tasksDao(), executor);
        CategoriesRepository categoriesRepository = new CategoriesRepositoryImpl(taskDatabase.categoriesDao(), executor);
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MainViewModel(tasksRepository, categoriesRepository);
            }
        }).get(MainViewModel.class);

        setupCategoryList();
        setupTaskList();
        hideDrawer(null);

        mainViewModel.getCategoriesWithTaskCount().observe(this, list -> {
            categoryAdapter.setCategoriesWithTaskCounts(list);
        });
    }

    private void setupCategoryList() {
        RecyclerView list = findViewById(R.id.rvCategoryList);
        categoryAdapter = new CategoryAdapter(getString(R.string.task_count_placeholder));

        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        layoutMangager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list.setAdapter(categoryAdapter);
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

    public void showDrawer(View view) {
        View drawer = findViewById(R.id.drawer);
        drawer.setVisibility(View.VISIBLE);
        drawer
                .animate()
                .translationX(0)
                .setDuration(250);
    }

    public void hideDrawer(View view) {
        View drawer = findViewById(R.id.drawer);
        drawer
                .animate()
                .translationX(-drawer.getWidth())
                .withEndAction(() -> drawer.setVisibility(View.INVISIBLE))
                .setDuration(200);
    }

    public void addTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }
}