package com.example.todo.data.model.dao;

import static com.example.todo.LiveDataTestUtil.getOrAwaitValue;
import static org.assertj.core.api.Assertions.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todo.DatabaseDisableForeignKeyUtil;
import com.example.todo.data.model.Task;
import com.example.todo.data.source.local.TaskDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TasksDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskDatabase database;
    private TasksDao tasksDao;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        TaskDatabase.class
                ).addCallback(new DatabaseDisableForeignKeyUtil())
                .allowMainThreadQueries()
                .build();
        tasksDao = database.tasksDao();
    }

    @After
    public void tearDown() {
        database.close();
        database = null;
        tasksDao = null;
    }

    @Test
    public void Expect_TasksToBeEmpty_When_NewDatase() throws InterruptedException {
        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void Expect_1ItemInTasks_When_TaskIsInsertedToNewDatabase() throws InterruptedException {
        final Task insertedTask = new Task(1L, "Task", "description", null, null);
        tasksDao.insertTask(insertedTask);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(1)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(insertedTask.getCategoryId())
                                .hasTask(insertedTask.getTask())
                                .hasDescription(insertedTask.getDescription())
                                .hasDueAt(insertedTask.getDueAt())
                                .hasCompletedAt(insertedTask.getCompletedAt())
                );
    }

    @Test
    public void Expect_NoItemInTasks_When_TaskIsInsertedThenDeleted() throws InterruptedException {
        final Task task = new Task(1L, 1L, "Task", "description", null, null, null);
        tasksDao.insertTask(task);
        tasksDao.deleteTask(task);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void Expect_2ItemInTasks_When_TaskIsInsertedTwiceToNewDatabase() throws InterruptedException {
        final Task insertedTask = new Task(1L, "Task", "description", null, null);
        tasksDao.insertTask(insertedTask);
        tasksDao.insertTask(insertedTask);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(2)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(insertedTask.getCategoryId())
                                .hasTask(insertedTask.getTask())
                                .hasDescription(insertedTask.getDescription())
                                .hasDueAt(insertedTask.getDueAt())
                                .hasCompletedAt(insertedTask.getCompletedAt())
                );
    }

    @Test
    public void Expect_ItemsToBeFiltered_When_ItMatchesCategoryId() throws InterruptedException {
        int itemsPerCategory = 5;
        long numberOfCategories = 5;
        long desiredCategoryId = 1;

        for (long categoryId = 1; categoryId <= numberOfCategories; ++categoryId) {
            for (int i = 0; i < itemsPerCategory; ++i) {
                final Task task = new Task(
                        categoryId,
                        "Task " + i,
                        "description",
                        null,
                        null
                );
                tasksDao.insertTask(task);
            }
        }

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasksFromCategory(desiredCategoryId));
        assertThat(tasks)
                .hasSize(itemsPerCategory)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(desiredCategoryId)
                );
    }

    @Test
    public void Expect_DueAtToChange_When_DueAtIsUpdated() throws InterruptedException {
        final Task originalTask = new Task(1L, "Task", "description", null, null);
        long newDueAt = 1L;

        tasksDao.insertTask(originalTask);
        Task taskInDb = getOrAwaitValue(tasksDao.getTasks()).get(0);
        taskInDb.setDueAt(newDueAt);
        tasksDao.updateTask(taskInDb);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(1)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(originalTask.getCategoryId())
                                .hasTask(originalTask.getTask())
                                .hasDescription(originalTask.getDescription())
                                .hasDueAt(newDueAt)
                                .hasCompletedAt(originalTask.getCompletedAt())
                );
    }

    @Test
    public void Expect_CompletedAtToChange_When_CompletedAtIsUpdated() throws InterruptedException {
        final Task originalTask = new Task(1L, "Task", "description", null, null);
        long newCompletedAt = 1L;

        tasksDao.insertTask(originalTask);
        Task taskInDb = getOrAwaitValue(tasksDao.getTasks()).get(0);
        taskInDb.setCompletedAt(newCompletedAt);
        tasksDao.updateTask(taskInDb);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(1)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(originalTask.getCategoryId())
                                .hasTask(originalTask.getTask())
                                .hasDescription(originalTask.getDescription())
                                .hasDueAt(originalTask.getDueAt())
                                .hasCompletedAt(newCompletedAt)
                );
    }

    @Test
    public void Expect_CategoryIdToChange_When_CategoryIdIsUpdated() throws InterruptedException {
        final Task originalTask = new Task(1L, "Task", "description", null, null);
        long newCategoryId = 2L;

        tasksDao.insertTask(originalTask);
        Task taskInDb = getOrAwaitValue(tasksDao.getTasks()).get(0);
        taskInDb.setCategoryId(newCategoryId);
        tasksDao.updateTask(taskInDb);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(1)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(newCategoryId)
                                .hasTask(originalTask.getTask())
                                .hasDescription(originalTask.getDescription())
                                .hasDueAt(originalTask.getDueAt())
                                .hasCompletedAt(originalTask.getCompletedAt())
                );
    }

    @Test
    public void Expect_TaskToChange_When_TaskIsUpdated() throws InterruptedException {
        final Task originalTask = new Task(1L, "Task", "description", null, null);
        String newTask = "New Task";

        tasksDao.insertTask(originalTask);
        Task taskInDb = getOrAwaitValue(tasksDao.getTasks()).get(0);
        taskInDb.setTask(newTask);
        tasksDao.updateTask(taskInDb);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(1)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(originalTask.getCategoryId())
                                .hasTask(newTask)
                                .hasDescription(originalTask.getDescription())
                                .hasDueAt(originalTask.getDueAt())
                                .hasCompletedAt(originalTask.getCompletedAt())
                );
    }

    @Test
    public void Expect_DescriptionToChange_When_DescriptionIsUpdated() throws InterruptedException {
        final Task originalTask = new Task(1L, "Task", "description", null, null);
        String newDescription = "New Description";

        tasksDao.insertTask(originalTask);
        Task taskInDb = getOrAwaitValue(tasksDao.getTasks()).get(0);
        taskInDb.setDescription(newDescription);
        tasksDao.updateTask(taskInDb);

        List<Task> tasks = getOrAwaitValue(tasksDao.getTasks());
        assertThat(tasks)
                .hasSize(1)
                .allSatisfy(
                        task -> TaskAssert.assertThat(task)
                                .isNotNull()
                                .hasCategoryId(originalTask.getCategoryId())
                                .hasTask(originalTask.getTask())
                                .hasDescription(newDescription)
                                .hasDueAt(originalTask.getDueAt())
                                .hasCompletedAt(originalTask.getCompletedAt())
                );
    }
}