package com.example.todo.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.data.model.Task;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final Set<Listener> bindViewHolderListener = new HashSet<>();
    private List<Task> tasks = Collections.emptyList();

    public interface Listener {
        void onClick(TaskAdapter.ViewHolder viewHolder, Task task);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View itemView;
        private final CheckBox cbTask;
        private final TextView txtTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            cbTask = itemView.findViewById(R.id.cbTask);
            txtTask = itemView.findViewById(R.id.txtTask);
        }

        public View getItemView() {
            return itemView;
        }

        public CheckBox getCbTask() {
            return cbTask;
        }

        public TextView getTxtTask() {
            return txtTask;
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
        Context context = holder.getItemView().getContext();
        TextView txtTask = holder.getTxtTask();
        CheckBox cbTask = holder.getCbTask();

        txtTask.setText(task.getTask());
        if (task.getCompletedAt() == null) {
            cbTask.setChecked(false);
            txtTask.setTextColor(context.getColor(R.color.black));
            txtTask.setPaintFlags( txtTask.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            cbTask.setChecked(true);
            txtTask.setTextColor(context.getColor(R.color.gray));
            txtTask.setPaintFlags( txtTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        bindViewHolderListener.forEach(listener -> {
            listener.onClick(holder, tasks.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setOnBindViewHolderListener(Listener listener) {
        bindViewHolderListener.add(listener);
    }

    public void removeOnBindViewHolderListener(Listener listener) {
        bindViewHolderListener.remove(listener);
    }
}
