package com.example.todo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.data.model.Task;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final Set<OnClickListener> clickListeners = new HashSet<>();
    private List<Task> tasks = Collections.emptyList();

    public interface OnClickListener {
        void onClick(Task task);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final CheckBox cbTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            cbTask = itemView.findViewById(R.id.cbTask);
        }

        public CheckBox getCbTask() {
            return cbTask;
        }

        public View getItemView() {
            return itemView;
        }
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.getCbTask().setText(task.getTask());
        holder.getCbTask().setChecked(task.getCompletedAt() != null);

        holder.getItemView().setOnClickListener(view -> {
            clickListeners.forEach(listener -> {
                listener.onClick(tasks.get(position));
            });
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setOnClickListener(OnClickListener listener) {
        clickListeners.add(listener);
    }

    public void removeOnClickListener(OnClickListener listener) {
        clickListeners.remove(listener);
    }
}
