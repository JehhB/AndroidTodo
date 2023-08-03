package com.example.todo.ui.viewmodel;


import static com.example.todo.LiveDataTestUtil.getOrAwaitValue;

import static org.assertj.core.api.Assertions.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.repository.CategoriesRepositoryTestImpl;
import com.example.todo.data.repository.TasksRepositoryTestImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class MainViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel mainViewModel;
    private TasksRepositoryTestImpl tasksRepository;
    private CategoriesRepositoryTestImpl categoriesRepository;

    @Before
    public void setUp() {
        tasksRepository = new TasksRepositoryTestImpl();
        categoriesRepository = new CategoriesRepositoryTestImpl();
        mainViewModel = new MainViewModel(tasksRepository, categoriesRepository);

        categoriesRepository.insertCategory(new Category(1L, "Category 1"));
        categoriesRepository.insertCategory(new Category(2L, "Category 2"));
        tasksRepository.insertTask(new Task(1L, 1L, "Task 1", "task in category 1", 1L, null, null));
        tasksRepository.insertTask(new Task(2L, 2L, "Task 2", "task in category 2", 2L, null, null));
    }

    @Test
    public void When_ActiveCategoryIsSetToNull_Expect_AllTask() throws InterruptedException {
        LiveData<List<Task>> tasks = mainViewModel.getTasks();
        mainViewModel.setSelectedCategory(null);

        List<Task> taskList = getOrAwaitValue(tasks);
        assertThat(taskList).allSatisfy(task1 -> {
            assertThat(tasksRepository.getTasksList())
                    .anyMatch(task2 -> task2.getId() == task1.getId());
        });
    }

    @Test
    public void When_ActiveCategoryIsSet_Expect_TaskToBeOfThatCategory() throws InterruptedException {
        LiveData<List<Task>> tasks = mainViewModel.getTasks();
        Category category = categoriesRepository.getCategoryList().get(0);
        mainViewModel.setSelectedCategory(category);

        List<Task> taskList = getOrAwaitValue(tasks);
        assertThat(taskList).allSatisfy(task -> {
            assertThat(task)
                    .extracting(Task::getCategoryId)
                    .isEqualTo(category.getId());
        });
    }
}