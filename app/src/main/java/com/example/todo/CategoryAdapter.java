package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final String[] data = {"Business", "Shopping List"};

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCategoryName;
        private final ProgressBar pbCategoryProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            pbCategoryProgress = itemView.findViewById(R.id.pbCategoryProgress);
        }

        public TextView getTxtCategoryName() {
            return txtCategoryName;
        }

        public ProgressBar getPbCategoryProgress() {
            return pbCategoryProgress;
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
        holder.getTxtCategoryName().setText(data[position]);
        holder.getPbCategoryProgress().setProgress(0);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

}
