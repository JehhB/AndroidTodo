package com.example.todo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "EXTRA_TASK_ID";
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        final Task task = getTask();

        AppContainer container = ((TodoApplication) getApplication()).getAppContainer();
        TasksRepository tasksRepository = container.getTasksRepository();
        CategoriesRepository categoriesRepository = container.getCategoriesRepository();

        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TaskViewModel(
                        task,
                        tasksRepository,
                        categoriesRepository
                );
            }
        }).get(TaskViewModel.class);

        setupCategorySpinner();
        setupActionButton();
        initTaskActivity(task);
    }

    private void initTaskActivity(Task task) {
        TextView txtTaskComplete = findViewById(R.id.txtTaskComplete);
        EditText etTask = findViewById(R.id.etTask);
        EditText etDescription = findViewById(R.id.etDescription);
        Spinner spnCategory = findViewById(R.id.spnCategory);

        etTask.setText(task.getTask());
        etDescription.setText(task.getDescription());

        if (task.getCompletedAt() != null) {
            String placeholder = getString(R.string.completed_at_format);
            Date date = new Date(task.getCompletedAt());

            Locale locale = Locale.getDefault();
            SimpleDateFormat dateFormat = new SimpleDateFormat(placeholder, locale);
            String output = dateFormat.format(date);

            txtTaskComplete.setText(output);
            txtTaskComplete.setVisibility(View.VISIBLE);
        } else {
            txtTaskComplete.setVisibility(View.GONE);
        }

        if (task.getCategoryId() == null) {
            spnCategory.setSelection(spnCategory.getAdapter().getCount() - 1);
        } else {
            taskViewModel.getCategories().observe(this, categories -> {
                for (int i = 0; i < categories.size(); ++i) {
                    if (categories.get(i).getId() == task.getId()) {
                        int position = i;
                        spnCategory.post(() -> {
                            spnCategory.setSelection(position);
                        });
                        return;
                    }
                }
            });
        }

        etTask.requestFocus();
    }

    private Task getTask() {
        Task temp = getIntent().getParcelableExtra(EXTRA_TASK_ID);
        if (temp == null) {
            return new Task(null, "", "", null, null);
        } else {
            return temp;
        }
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
        Handler handler = new Handler();

        EditText etTask = findViewById(R.id.etTask);
        String task = etTask.getText().toString().trim();
        EditText etDescription = findViewById(R.id.etDescription);
        String description = etDescription.getText().toString().trim();

        if (task.isEmpty()) {
            etTask.requestFocus();
            return;
        }

        Spinner spnCategory = findViewById(R.id.spnCategory);
        Category category = (Category) spnCategory.getSelectedItem();

        if (category != null) {
            saveAndFinish(task, description, category.getId());
        } else {
            View content = getLayoutInflater().inflate(R.layout.dialog_create_category, null);
            EditText etCategoryName = content.findViewById(R.id.etCategoryName);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.new_category_dialog_title)
                    .setView(content)
                    .setPositiveButton(R.string.create_button, (dialog1, which) -> {
                        String categoryName = etCategoryName.getText().toString().trim();
                        if (!categoryName.isEmpty()) {
                            Category newCategory = new Category(categoryName);
                            taskViewModel.insertCategory(newCategory, id -> {
                                handler.post(() -> {
                                    saveAndFinish(task, description, id);
                                });
                            });
                        }
                    })
                    .setNegativeButton(R.string.cancel_button, (dialog1, which) -> {
                    })
                    .create();
            dialog.show();
        }
    }

    private void saveAndFinish(String task, String description, Long categoryId) {
        taskViewModel.setTask(task);
        taskViewModel.setDescription(description);
        taskViewModel.setCategory(categoryId);
        taskViewModel.saveTask();

        finish();
    }

    public void finish(View view) {
        finish();
    }
}