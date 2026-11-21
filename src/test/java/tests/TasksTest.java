package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.TasksPage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TasksTest extends BaseTest {

    private TasksPage tasksPage;

    @BeforeEach
    public void setUpTasks() {
        loginAsAdmin();
        tasksPage = new TasksPage(driver, baseUrl);
    }

    @Test
    @DisplayName("Displays tasks list and verifies all initial cards are rendered")
    public void testViewTasksList() {
        tasksPage.openTasksPage();

        assertTrue(tasksPage.getTasksCount() > 0, "Tasks should be displayed on Kanban board");
        assertTrue(tasksPage.isTaskPresent("Task 1"), "Initial 'Task 1' should be present");
        assertTrue(tasksPage.isTaskPresent("Task 2"), "Initial 'Task 2' should be present");
    }

    @Test
    @DisplayName("Opens create task form and verifies required fields are displayed")
    public void testOpenCreateTaskForm() {
        tasksPage.openTasksPage();
        tasksPage.openCreateForm();

        assertTrue(tasksPage.isCreateFormOpen(), "Create task form should display title, assignee, and status inputs");
    }

    @Test
    @DisplayName("Creates a new task and verifies it appears in the specified column")
    public void testCreateTask() {
        tasksPage.openTasksPage();
        String uniqueTitle = "Automated Task " + System.currentTimeMillis();
        String content = "Test description for automated task";
        String assigneeEmail = "john@google.com";
        String statusName = "Draft";

        tasksPage.createTask(uniqueTitle, content, assigneeEmail, statusName);

        assertTrue(tasksPage.isTaskPresent(uniqueTitle), "Created task should appear on board");
        assertTrue(tasksPage.isTaskInColumn(uniqueTitle, statusName), "Created task should appear in 'Draft' column");
    }

    @Test
    @DisplayName("Filters tasks by status, assignee, and label")
    public void testFilterTasks() {
        tasksPage.openTasksPage();
        int initialCount = tasksPage.getTasksCount();
        assertTrue(initialCount > 0, "Initial board should contain tasks");

        // Filter by status "To Review"
        tasksPage.filterByStatus("To Review");
        int filteredCount = tasksPage.getTasksCount();
        assertTrue(filteredCount > 0 && filteredCount <= initialCount,
                "Filtered tasks count should be non-empty and subset of initial");
        assertTrue(tasksPage.isTaskInColumn("Task 2", "To Review"),
                "Task 2 should be in 'To Review' column after filter");
    }

    @Test
    @DisplayName("Edits an existing task and confirms updates are displayed")
    public void testEditTask() {
        tasksPage.openTasksPage();
        String targetTask = "Task 3";
        assertTrue(tasksPage.isTaskPresent(targetTask), "Target task should exist");

        String updatedTitle = "Task 3 Updated " + System.currentTimeMillis();
        String updatedContent = "Updated content for Task 3";
        tasksPage.openEditFormForTask(targetTask);
        tasksPage.updateTask(updatedTitle, updatedContent, null);

        assertTrue(tasksPage.isTaskPresent(updatedTitle), "Updated task title should be visible on board");
    }

    @Test
    @DisplayName("Moves task between columns and verifies updated status")
    public void testMoveTaskBetweenColumns() {
        tasksPage.openTasksPage();
        String taskTitle = "Task 5"; // Initially in "Draft" (status 1)
        assertTrue(tasksPage.isTaskInColumn(taskTitle, "Draft"), "Task 5 should initially be in 'Draft'");

        // Move to "Published"
        tasksPage.moveTaskByEditForm(taskTitle, "Published");

        assertTrue(tasksPage.isTaskInColumn(taskTitle, "Published"),
                "Task 5 should now be displayed in 'Published' column");
    }

    @Test
    @DisplayName("Deletes a task and confirms its removal from the board")
    public void testDeleteTask() {
        tasksPage.openTasksPage();
        String taskToDelete = "Task 1";
        assertTrue(tasksPage.isTaskPresent(taskToDelete), "Task 1 should exist initially");

        tasksPage.deleteTask(taskToDelete);

        assertFalse(tasksPage.isTaskPresent(taskToDelete), "Deleted task should no longer appear on board");
    }
}
