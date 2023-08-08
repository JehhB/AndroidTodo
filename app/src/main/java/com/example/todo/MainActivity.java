package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.model.TaskParcelable;
import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.TasksRepository;
import com.example.todo.di.AppContainer;
import com.example.todo.ui.adapter.CategoryAdapter;
import com.example.todo.ui.adapter.TaskAdapter;
import com.example.todo.ui.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppContainer container = ((TodoApplication) getApplication()).getAppContainer();
        TasksRepository tasksRepository = container.getTasksRepository();
        CategoriesRepository categoriesRepository = container.getCategoriesRepository();

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

    }

    private void setupCategoryList() {
        RecyclerView list = findViewById(R.id.rvCategoryList);
        CategoryAdapter adapter = new CategoryAdapter(getString(R.string.task_count_placeholder));

        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        layoutMangager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list.setAdapter(adapter);
        list.setLayoutManager(layoutMangager);

        mainViewModel.getCategoriesWithTaskCount().observe(this, categories -> {
            adapter.setCategoriesWithTaskCounts(categories);
        });
    }

    private void setupTaskList() {
        RecyclerView list = findViewById(R.id.rvTaskList);
        TaskAdapter adapter = new TaskAdapter();

        LinearLayoutManager layoutMangager = new LinearLayoutManager(this);
        layoutMangager.setOrientation(LinearLayoutManager.VERTICAL);

        list.setAdapter(adapter);
        list.setLayoutManager(layoutMangager);

        adapter.setOnClickListener(task -> {
            Intent intent = new Intent(this, TaskActivity.class);
            TaskParcelable taskParcelable = new TaskParcelable(task);

            intent.putExtra(TaskActivity.EXTRA_TASK_ID, taskParcelable);
            startActivity(intent);
        });

        mainViewModel.getTasks().observe(this, tasks -> {
            adapter.setTasks(tasks);
        });
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