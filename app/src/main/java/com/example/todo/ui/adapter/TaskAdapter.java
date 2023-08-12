package com.example.todo.ui.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
    private final Set<OnBindViewHolderListener> onBindViewHolderListeners = new HashSet<>();
    private List<Task> tasks = Collections.emptyList();

    public interface OnBindViewHolderListener {
        void onBindViewHolder(TaskAdapter.ViewHolder viewHolder, Task task);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final static int TO_LEFT = 1;
        public final static int TO_RIGHT = -1;

        private final CheckBox cbTask;
        private final TextView txtTask;
        private final TextView txtUndo;
        private final ViewGroup vgDeleteUndo;
        private final ViewGroup vgTask;
        private boolean deleteShown;
        private int direction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTask = itemView.findViewById(R.id.cbTask);
            txtTask = itemView.findViewById(R.id.txtTask);
            txtUndo = itemView.findViewById(R.id.txtUndo);
            vgTask = itemView.findViewById(R.id.vgTask);
            vgDeleteUndo = itemView.findViewById(R.id.vgDeleteUndo);
            direction = 1;
            reset();
        }

        public CheckBox getCbTask() {
            return cbTask;
        }

        public TextView getTxtTask() {
            return txtTask;
        }

        public TextView getTxtUndo() {
            return txtUndo;
        }

        public ViewGroup getVgTask() {
            return vgTask;
        }

        public void showDelete(int direction) {
            this.direction = direction;

            vgTask.setVisibility(View.INVISIBLE);
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(150);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                vgDeleteUndo.setAlpha(value);
                vgDeleteUndo.setTranslationX(direction * 16 * (1 - value));
            });
            animator.start();
            deleteShown = true;
        }

        public void hideDelete() {
            vgTask.setVisibility(View.VISIBLE);
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(150);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                vgDeleteUndo.setAlpha(1 - value);
                vgTask.setAlpha(value);
                vgTask.setTranslationX(-direction * itemView.getWidth() * (1 - value));
            });
            animator.start();
            deleteShown = false;
        }

        public void reset() {
            deleteShown = false;
            vgTask.setVisibility(View.VISIBLE);
            vgDeleteUndo.setAlpha(0);
            vgTask.setAlpha(1);
            vgTask.setTranslationX(0);
        }

        public boolean isDeleteShown() {
            return deleteShown;
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
        Context context = holder.itemView.getContext();
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
        holder.reset();

        onBindViewHolderListeners.forEach(onBindViewHolderListener -> {
            onBindViewHolderListener.onBindViewHolder(holder, tasks.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getItem(int i) {
        return tasks.get(i);
    }

    public void setOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        onBindViewHolderListeners.add(onBindViewHolderListener);
    }

    public void removeOnBindViewHolderListener(OnBindViewHolderListener onBindViewHolderListener) {
        onBindViewHolderListeners.remove(onBindViewHolderListener);
    }
}
