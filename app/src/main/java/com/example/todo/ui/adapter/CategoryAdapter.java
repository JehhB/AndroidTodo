package com.example.todo.ui.adapter;

import static com.example.todo.data.model.dao.CategoriesDao.CategoryWithTaskCount;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.data.model.Category;

import java.util.Arrays;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final String taskCountPlaceholder;
    private List<CategoryWithTaskCount> categoriesWithTaskCounts;

    public CategoryAdapter(String taskCountPlaceholder) {
        super();
        this.categoriesWithTaskCounts = Arrays.asList();
        this.taskCountPlaceholder = taskCountPlaceholder;
    }

    public CategoryAdapter() {
        this("%d Tasks");
    }

    public void setCategoriesWithTaskCounts(List<CategoryWithTaskCount> categoriesWithTaskCounts) {
        this.categoriesWithTaskCounts = categoriesWithTaskCounts;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCategoryName;
        private final TextView txtCategoryTaskCount;
        private final ProgressBar pbCategoryProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            pbCategoryProgress = itemView.findViewById(R.id.pbCategoryProgress);
            txtCategoryTaskCount = itemView.findViewById(R.id.txtCategoryTaskCount);
        }

        public TextView getTxtCategoryName() {
            return txtCategoryName;
        }

        public ProgressBar getPbCategoryProgress() {
            return pbCategoryProgress;
        }

        public TextView getTxtCategoryTaskCount() { return txtCategoryTaskCount; }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryWithTaskCount categoryWithTaskCount = categoriesWithTaskCounts.get(position);
        Category category = categoryWithTaskCount.category;

        holder.getTxtCategoryName().setText(category.getName());
        holder.getPbCategoryProgress().setProgress(categoryWithTaskCount.completed_task_count);
        holder.getPbCategoryProgress().setMax(categoryWithTaskCount.task_count);

        String taskCount = String.format(taskCountPlaceholder, categoryWithTaskCount.task_count);
        holder.getTxtCategoryTaskCount().setText(taskCount);
    }

    @Override
    public int getItemCount() {
        return categoriesWithTaskCounts.size();
    }

}
