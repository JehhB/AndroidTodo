package com.example.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.TasksRepository;
import com.example.todo.di.AppContainer;
import com.example.todo.ui.adapter.CategorySpinnerAdapter;
import com.example.todo.ui.viewmodel.TaskViewModel;

public class TaskActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        EditText etTask = findViewById(R.id.etTask);
        etTask.requestFocus();

        AppContainer container = ((TodoApplication) getApplication()).getAppContainer();
        TasksRepository tasksRepository = container.getTasksRepository();
        CategoriesRepository categoriesRepository = container.getCategoriesRepository();

        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TaskViewModel(
                        new Task(null, "", "", null, null),
                        tasksRepository,
                        categoriesRepository
                );
            }
        }).get(TaskViewModel.class);

        setupCategorySpinner();
    }

    private void setupCategorySpinner() {
        CategorySpinnerAdapter categorySpinnerAdapter = new CategorySpinnerAdapter(this);
        Spinner spnCategory = findViewById(R.id.spnCategory);
        spnCategory.setAdapter(categorySpinnerAdapter);

        taskViewModel.getCategories().observe(this, categories -> {
            categorySpinnerAdapter.setData(categories);
        });
    }

    private void setupActionButton() {
        Button button = findViewById(R.id.btnTaskAction);

        if (taskViewModel.isNew()) {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_add, 0);
            button.setText(R.string.add_task);
        } else {
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_edit, 0);
            button.setText(R.string.edit_task);
        }
    }

    public void saveTask(View view) {
        EditText etTask = findViewById(R.id.etTask);
        String task = etTask.getText().toString().trim();

        if (task.isEmpty()) {
            etTask.requestFocus();
            return;
        }

        Spinner spnCategory = findViewById(R.id.spnCategory);
        Category category = (Category) spnCategory.getSelectedItem();

        if (category == null) {
            View content = getLayoutInflater().inflate(R.layout.dialog_create_category, null);
            EditText etCategoryName = content.findViewById(R.id.etCategoryName);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.new_category_dialog_title)
                    .setView(content)
                    .setPositiveButton(R.string.create_button, (dialog1, which) -> {
                        String categoryName = etCategoryName.getText().toString().trim();
                        if (!categoryName.isEmpty())
                            taskViewModel.newCategory(categoryName);
                    })
                    .setNegativeButton(R.string.cancel_button, (dialog1, which) -> {
                    })
                    .create();
            dialog.show();
            return;
        }

        EditText etDescription = findViewById(R.id.etDescription);
        String description = etDescription.getText().toString().trim();

        taskViewModel.setTask(task);
        taskViewModel.setDescription(description);
        taskViewModel.setCategory(category.getId());
        taskViewModel.saveTask();

        finish();
    }

    public void finish(View view) {
        finish();
    }
}