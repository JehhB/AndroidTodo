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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final Set<OnBindViewHolderListener> onBindViewHolderListeners = new HashSet<>();
    private final String taskCountPlaceholder;
    private List<CategoryWithTaskCount> categoriesWithTaskCounts;

    public CategoryAdapter(String taskCountPlaceholder) {
        super();
        this.categoriesWithTaskCounts = Collections.emptyList();
        this.taskCountPlaceholder = taskCountPlaceholder;
    }

    public CategoryAdapter() {
        this("%d Tasks");
    }

    public void setCategoriesWithTaskCounts(List<CategoryWithTaskCount> categoriesWithTaskCounts) {
        this.categoriesWithTaskCounts = categoriesWithTaskCounts;
        notifyDataSetChanged();
    }

    public interface OnBindViewHolderListener {
        void onBindViewHolder(
                CategoryAdapter.ViewHolder viewHolder,
                CategoryWithTaskCount categoryWithTaskCount
        );
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final int PROGRESSBAR_ANIM_STEPS = 10;

        private final View itemView;
        private final TextView txtCategoryName;
        private final TextView txtCategoryTaskCount;
        private final ProgressBar pbCategoryProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            this.pbCategoryProgress = itemView.findViewById(R.id.pbCategoryProgress);
            this.txtCategoryTaskCount = itemView.findViewById(R.id.txtCategoryTaskCount);
        }

        public View getItemView() {
            return itemView;
        }

        public TextView getTxtCategoryName() {
            return txtCategoryName;
        }

        public ProgressBar getPbCategoryProgress() {
            return pbCategoryProgress;
        }

        public TextView getTxtCategoryTaskCount() {
            return txtCategoryTaskCount;
        }

        public void setTaskCount(int count) {
            pbCategoryProgress.setMax(count * PROGRESSBAR_ANIM_STEPS);
        }

        public void setProgress(int count) {
            pbCategoryProgress.setProgress(count * PROGRESSBAR_ANIM_STEPS, true);
        }
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
        holder.setTaskCount(categoryWithTaskCount.task_count);
        holder.setProgress(categoryWithTaskCount.completed_task_count);

        String taskCount = String.format(taskCountPlaceholder, categoryWithTaskCount.task_count);
        holder.getTxtCategoryTaskCount().setText(taskCount);
        onBindViewHolderListeners.forEach(listener -> {
            listener.onBindViewHolder(holder, categoryWithTaskCount);
        });
    }

    @Override
    public int getItemCount() {
        return categoriesWithTaskCounts.size();
    }

    public void setOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        onBindViewHolderListeners.add(onBindViewHolderListener);
    }

    public void removeOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        onBindViewHolderListeners.remove(onBindViewHolderListener);
    }
}
