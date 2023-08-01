package com.example.todo.data.model.dao;

import static com.example.todo.LiveDataTestUtil.getOrAwaitValue;
import static org.assertj.core.api.Assertions.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todo.data.model.Category;
import com.example.todo.data.model.Task;
import com.example.todo.data.source.local.TaskDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CategoriesDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskDatabase database;
    private CategoriesDao categoriesDao;
    private TasksDao tasksDao;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        TaskDatabase.class
                ).allowMainThreadQueries()
                .build();
        categoriesDao = database.categoriesDao();
        tasksDao = database.tasksDao();
    }

    @After
    public void tearDown() {
        database.close();
        database = null;
        categoriesDao = null;
        tasksDao = null;
    }

    @Test
    public void Expect_CategoriesToBeEmpty_When_NewDatabase() throws InterruptedException {
        List<Category> categories = getOrAwaitValue(categoriesDao.getCategories());
        assertThat(categories).isEmpty();
    }

    @Test
    public void Expect_1ItemInCategories_WhenCategoryIsInsertedToDatabase() throws InterruptedException {
        Category category = new Category("category");
        categoriesDao.insertCategory(category);

        List<Category> categories = getOrAwaitValue(categoriesDao.getCategories());
        assertThat(categories)
                .hasSize(1)
                .allSatisfy(c -> CategoryAssert.assertThat(c).hasName(category.getName()));
    }

    @Test
    public void Expect_2ItemInCategories_WhenCategoryIsInsertedTwiceToNewDatabase() throws InterruptedException {
        Category category = new Category("category");
        categoriesDao.insertCategory(category);
        categoriesDao.insertCategory(category);

        List<Category> categories = getOrAwaitValue(categoriesDao.getCategories());
        assertThat(categories)
                .hasSize(2)
                .allSatisfy(c -> CategoryAssert.assertThat(c).hasName(category.getName()));
    }

    @Test
    public void Expect_CategoryNameToChance_WhenCategoryNameIsUpdated() throws InterruptedException {
        Category originalCategory = new Category("category");
        categoriesDao.insertCategory(originalCategory);
        String newName = "new name";

        Category categoryInDb = getOrAwaitValue(categoriesDao.getCategories()).get(0);
        categoryInDb.setName(newName);
        categoriesDao.updateCategory(categoryInDb);

        List<Category> categories = getOrAwaitValue(categoriesDao.getCategories());
        assertThat(categories)
                .hasSize(1)
                .allSatisfy(c -> CategoryAssert.assertThat(c).hasName(newName));
    }

    @Test
    public void Expect_TaskCountOf0_When_CategoryIsNewlyInserted() throws InterruptedException {
        Category category = new Category("category");
        categoriesDao.insertCategory(category);

        List<CategoriesDao.CategoryWithTaskCount> categoriesWithTaskCount;
        categoriesWithTaskCount = getOrAwaitValue(categoriesDao.getCategoriesWithTaskCount());

        assertThat(categoriesWithTaskCount)
                .hasSize(1)
                .allSatisfy(c -> {
                    assertThat(c)
                            .extracting("task_count", "completed_task_count")
                            .containsExactly(0, 0);
                    CategoryAssert.assertThat(c.category).hasName(category.getName());
                });
    }

    @Test
    public void Expect_TaskCountOf1ButNoCompleted_When_WhenIncompleteTaskIsAssignedToCategory() throws InterruptedException {
        Category category = new Category("category");
        Long categoryId = categoriesDao.insertCategory(category);

        Task task = new Task(categoryId, "task", "description", null, null);
        tasksDao.insertTask(task);

        List<CategoriesDao.CategoryWithTaskCount> categoriesWithTaskCount;
        categoriesWithTaskCount = getOrAwaitValue(categoriesDao.getCategoriesWithTaskCount());

        assertThat(categoriesWithTaskCount)
                .hasSize(1)
                .allSatisfy(c -> {
                    assertThat(c)
                            .extracting("task_count", "completed_task_count")
                            .containsExactly(1, 0);
                    CategoryAssert.assertThat(c.category)
                            .hasId(categoryId)
                            .hasName(category.getName());
                });
    }

    @Test
    public void Expect_TaskCountOf1AndComplete1_When_WhenCompleteTaskIsAssignedToCategory() throws InterruptedException {
        Category category = new Category("category");
        Long categoryId = categoriesDao.insertCategory(category);

        Task task = new Task(categoryId, "task", "description", null, 1L);
        tasksDao.insertTask(task);

        List<CategoriesDao.CategoryWithTaskCount> categoriesWithTaskCount;
        categoriesWithTaskCount = getOrAwaitValue(categoriesDao.getCategoriesWithTaskCount());

        assertThat(categoriesWithTaskCount)
                .hasSize(1)
                .allSatisfy(c -> {
                    assertThat(c)
                            .extracting("task_count", "completed_task_count")
                            .containsExactly(1, 1);
                    CategoryAssert.assertThat(c.category)
                            .hasId(categoryId)
                            .hasName(category.getName());
                });
    }

    @Test
    public void Expect_CategoriesToBeSortedDependingIfItHavePendingTask_When_QueryingByTaskCount() throws InterruptedException {
        Category completeCategory = new Category("complete");
        Long completeCategoryId = categoriesDao.insertCategory(completeCategory);
        Task completedTask = new Task(completeCategoryId, "Completed", "", null, 1L);
        tasksDao.insertTask(completedTask);

        Category incompleteCategory = new Category("Not complete");
        Long incompleteCategoryId = categoriesDao.insertCategory(incompleteCategory);
        Task incompletedTask = new Task(incompleteCategoryId, "Incomplete", "", null, null);
        tasksDao.insertTask(incompletedTask);

        categoriesDao.insertCategory(completeCategory);
        categoriesDao.insertCategory(incompleteCategory);

        List<CategoriesDao.CategoryWithTaskCount> categoriesWithTaskCount;
        categoriesWithTaskCount = getOrAwaitValue(categoriesDao.getCategoriesWithTaskCount());

        assertThat(categoriesWithTaskCount).isSortedAccordingTo((a, b) -> {
            int aIsComplete =  a.task_count == a.completed_task_count ? 1 : 0;
            int bIsComplete =  b.task_count == b.completed_task_count ? 1 : 0;

            return aIsComplete - bIsComplete;
        });
    }
}