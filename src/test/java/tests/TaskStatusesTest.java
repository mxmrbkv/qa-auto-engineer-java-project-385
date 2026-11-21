package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.TaskStatusesPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskStatusesTest extends BaseTest {

    private TaskStatusesPage statusesPage;

    @BeforeEach
    public void setUpStatuses() {
        loginAsAdmin();
        statusesPage = new TaskStatusesPage(driver, baseUrl);
    }

    @Test
    @DisplayName("Displays task statuses table with key headers and data")
    public void testViewStatusesList() {
        statusesPage.openTaskStatusesPage();

        assertTrue(statusesPage.isTableDisplayed(), "Statuses table should be displayed");
        assertTrue(statusesPage.areKeyHeadersDisplayed(), "Key headers (name, slug) should be displayed");
        assertTrue(statusesPage.getStatusesCount() > 0, "Statuses table should contain rows");
        assertTrue(statusesPage.isStatusPresent("Draft"), "Initial status 'Draft' should be present");
        assertTrue(statusesPage.getStatusSlugs().contains("draft"), "Initial slug 'draft' should be present");
    }

    @Test
    @DisplayName("Opens create status form correctly with required inputs")
    public void testOpenCreateStatusForm() {
        statusesPage.openTaskStatusesPage();
        statusesPage.openCreateForm();

        assertTrue(statusesPage.isCreateFormOpen(), "Create status form inputs should be displayed");
    }

    @Test
    @DisplayName("Creates a new status successfully and shows it in the list")
    public void testCreateStatus() {
        statusesPage.openTaskStatusesPage();
        String uniqueName = "In Progress " + System.currentTimeMillis();
        String uniqueSlug = "in_progress_" + System.currentTimeMillis();

        statusesPage.createStatus(uniqueName, uniqueSlug);

        assertTrue(statusesPage.isStatusPresent(uniqueName), "Newly created status should appear in the table");
        assertTrue(statusesPage.getStatusSlugs().contains(uniqueSlug), "Newly created slug should appear in the table");
    }

    @Test
    @DisplayName("Opens edit form, changes status data and verifies updates are saved")
    public void testEditStatus() {
        statusesPage.openTaskStatusesPage();
        String targetStatus = "To Review";
        assertTrue(statusesPage.isStatusPresent(targetStatus), "Status to edit should exist");

        statusesPage.openEditFormForStatus(targetStatus);
        assertEquals(targetStatus, statusesPage.getNameInputValue(), "Name should be prefilled");
        assertEquals("to_review", statusesPage.getSlugInputValue(), "Slug should be prefilled");

        String updatedName = "In Code Review";
        String updatedSlug = "in_code_review";
        statusesPage.updateStatus(updatedName, updatedSlug);

        assertTrue(statusesPage.isStatusPresent(updatedName), "Updated status name should appear in list");
        assertTrue(statusesPage.getStatusSlugs().contains(updatedSlug), "Updated slug should appear in list");
    }

    @Test
    @DisplayName("Deletes a single status and verifies removal from list")
    public void testDeleteSingleStatus() {
        statusesPage.openTaskStatusesPage();
        String statusToDelete = "To Be Fixed";
        assertTrue(statusesPage.isStatusPresent(statusToDelete), "Status to delete should exist initially");

        statusesPage.deleteStatus(statusToDelete);

        assertFalse(statusesPage.isStatusPresent(statusToDelete), "Deleted status should no longer be present");
    }

    @Test
    @DisplayName("Bulk deletes all statuses and verifies list is empty")
    public void testBulkDeleteStatuses() {
        statusesPage.openTaskStatusesPage();
        assertTrue(statusesPage.getStatusesCount() > 0, "Statuses should be present before bulk delete");

        statusesPage.selectAll();
        statusesPage.deleteSelected();

        assertEquals(0, statusesPage.getStatusesCount(), "All statuses should be removed after bulk delete");
    }
}
