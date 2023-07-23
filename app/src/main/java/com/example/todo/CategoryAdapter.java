package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final String[] data = {"Business", "Shopping List", "Academics"};
    private final Context context;

    public CategoryAdapter(Context context) {
        super();
        this.context = context;

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
        holder.getTxtCategoryName().setText(data[position]);
        holder.getPbCategoryProgress().setProgress(0);

        String taskCount = String.format(context.getString(R.string.task_count_placeholder), 0);
        holder.getTxtCategoryTaskCount().setText(taskCount);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

}
