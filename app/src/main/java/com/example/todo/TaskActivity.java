package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.todo.ui.adapter.CategorySpinnerAdapter;

public class TaskActivity extends AppCompatActivity {
    private CategorySpinnerAdapter categorySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        EditText etTask = findViewById(R.id.etTask);
        etTask.requestFocus();

        setupCategorySpinner();
    }

    private void setupCategorySpinner() {
        categorySpinnerAdapter = new CategorySpinnerAdapter(this);
        Spinner spnCategory = findViewById(R.id.spnCategory);
        spnCategory.setAdapter(categorySpinnerAdapter);
    }

    public void finish(View view) {
        finish();
    }
}