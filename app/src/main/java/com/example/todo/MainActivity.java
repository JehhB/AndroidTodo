package com.example.todo;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.model.Task;
import com.example.todo.data.model.TaskParcelable;
import com.example.todo.data.repository.CategoriesRepository;
import com.example.todo.data.repository.TasksRepository;
import com.example.todo.di.AppContainer;
import com.example.todo.ui.adapter.CategoryAdapter;
import com.example.todo.ui.adapter.TaskAdapter;
import com.example.todo.ui.helper.ItemSwipeHelper;
import com.example.todo.ui.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private final static long DELETE_DELAY_MILLIS = 5_000;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppContainer container = ((TodoApplication) getApplication()).getAppContainer();
        TasksRepository tasksRepository = container.getTasksRepository();
        CategoriesRepository categoriesRepository = container.getCategoriesRepository();

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                @SuppressWarnings("unchecked")
                T ret = (T) new MainViewModel(tasksRepository, categoriesRepository);
                return ret;
            }
        }).get(MainViewModel.class);

        setupCategoryList();
        setupTaskList();
        hideDrawer(null);

    }

    private void setupCategoryList() {
        RecyclerView list = findViewById(R.id.rvCategoryList);
        CategoryAdapter adapter = new CategoryAdapter(getString(R.string.task_count_placeholder));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list.setAdapter(adapter);
        list.setLayoutManager(layoutManager);

        adapter.setOnBindViewHolderListener(((viewHolder, categoryWithTaskCount) -> {
            viewHolder.getItemView().setOnClickListener(view -> {
                mainViewModel.setSelectedCategory(categoryWithTaskCount.category);
            });
        }));

        mainViewModel.getCategoriesWithTaskCount().observe(this, categories -> {
            adapter.setCategoriesWithTaskCounts(categories);
        });
    }

    private void setupTaskList() {
        RecyclerView list = findViewById(R.id.rvTaskList);
        TaskAdapter adapter = new TaskAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        list.setAdapter(adapter);
        list.setLayoutManager(layoutManager);

        adapter.setOnBindViewHolderListener((viewHolder, task) -> {
            viewHolder.getVgTask().setOnClickListener(view -> {
                Intent intent = new Intent(this, TaskActivity.class);
                TaskParcelable taskParcelable = new TaskParcelable(task);

                intent.putExtra(TaskActivity.EXTRA_TASK_ID, taskParcelable);
                startActivity(intent);
            });

            viewHolder.getCbTask().setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    task.setCompletedAt(System.currentTimeMillis());
                } else {
                    task.setCompletedAt(null);
                }
                mainViewModel.updateTask(task);
            });
        });

        ItemSwipeHelper swipeHelper = new ItemSwipeHelper() {
            @Override
            protected void onDrawChild(RecyclerView rv, RecyclerView.ViewHolder vh, float dX, boolean isForward) {
                TaskAdapter.ViewHolder holder = (TaskAdapter.ViewHolder) vh;
                if (holder.isDeleteShown()) return;
                holder.getVgTask().setTranslationX(dX);
            }

            @Override
            protected void onSwipe(RecyclerView rv, RecyclerView.ViewHolder vh, int direction) {
                TaskAdapter.ViewHolder holder = (TaskAdapter.ViewHolder) vh;
                if (holder.isDeleteShown()) return;

                Task task = adapter.getItem(vh.getAdapterPosition());

                DeleteThread delete = new DeleteThread(task);
                holder.getTxtUndo().setOnClickListener(view -> {
                    holder.hideDelete();
                    delete.abort();
                });

                holder.showDelete(direction == SWIPE_RIGHT ?
                        TaskAdapter.ViewHolder.TO_RIGHT :
                        TaskAdapter.ViewHolder.TO_LEFT);
                delete.start();
            }
        };
        swipeHelper.attachToRecyclerView(list);

        mainViewModel.getTasks().observe(this, tasks -> {
            adapter.setTasks(tasks);
        });

        /* TODO:
        *   Animation issue as describe in this article
        *   https://medium.com/mobile-app-development-publication/recyclerview-supported-wrap-content-not-quite-f04a942ce624
        *   For now decrease remove animation duration and add layout transition to mitigate effect
        */
        final RecyclerView.ItemAnimator animator = list.getItemAnimator();
        if (animator != null) {
            animator.setRemoveDuration(15);
        }

        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) findViewById(R.id.vgMain)).setLayoutTransition(layoutTransition);
    }

    public void showDrawer(View view) {
        View drawer = findViewById(R.id.drawer);
        drawer.setVisibility(View.VISIBLE);
        drawer
                .animate()
                .translationX(0)
                .setDuration(250);
    }

    public void hideDrawer(View view) {
        View drawer = findViewById(R.id.drawer);
        drawer
                .animate()
                .translationX(-drawer.getWidth())
                .withEndAction(() -> drawer.setVisibility(View.INVISIBLE))
                .setDuration(200);
    }

    public void addTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    private class DeleteThread extends Thread {
        private volatile boolean doContinue;
        private final Task task;

        public DeleteThread(Task task) {
            super();

            doContinue = true;
            this.task = task;
        }

        public void abort() {
            doContinue = false;
        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(DELETE_DELAY_MILLIS);
                if (doContinue) {
                    mainViewModel.deleteTask(task);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}