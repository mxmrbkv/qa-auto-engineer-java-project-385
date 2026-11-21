package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LabelsPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LabelsTest extends BaseTest {

    private LabelsPage labelsPage;

    @BeforeEach
    public void setUpLabels() {
        loginAsAdmin();
        labelsPage = new LabelsPage(driver, baseUrl);
    }

    @Test
    @DisplayName("Displays labels table with key headers and data")
    public void testViewLabelsList() {
        labelsPage.openLabelsPage();

        assertTrue(labelsPage.isTableDisplayed(), "Labels table should be displayed");
        assertTrue(labelsPage.isNameHeaderDisplayed(), "Name header should be displayed");
        assertTrue(labelsPage.getLabelsCount() > 0, "Labels table should contain rows");
        assertTrue(labelsPage.isLabelPresent("bug"), "Default 'bug' label should be present");
        assertTrue(labelsPage.isLabelPresent("critical"), "Default 'critical' label should be present");
    }

    @Test
    @DisplayName("Opens create label form correctly and accepts name")
    public void testOpenCreateLabelForm() {
        labelsPage.openLabelsPage();
        labelsPage.openCreateForm();

        assertTrue(labelsPage.isCreateFormOpen(), "Create label form should be open and name input visible");
    }

    @Test
    @DisplayName("Creates a new label successfully and shows it in the list")
    public void testCreateLabel() {
        labelsPage.openLabelsPage();
        String newLabelName = "documentation_" + System.currentTimeMillis();

        labelsPage.createLabel(newLabelName);

        assertTrue(labelsPage.isLabelPresent(newLabelName), "Newly created label should appear in the list");
    }

    @Test
    @DisplayName("Edits an existing label and confirms changes are saved")
    public void testEditLabel() {
        labelsPage.openLabelsPage();
        String targetLabel = "enhancement";
        assertTrue(labelsPage.isLabelPresent(targetLabel), "Target label to edit should exist initially");

        labelsPage.openEditFormForLabel(targetLabel);
        assertEquals(targetLabel, labelsPage.getNameInputValue(), "Label name should be prefilled");

        String updatedLabelName = "performance_improvement";
        labelsPage.updateLabel(updatedLabelName);

        assertTrue(labelsPage.isLabelPresent(updatedLabelName), "Updated label name should appear in the table");
    }

    @Test
    @DisplayName("Deletes a single label and verifies removal from list")
    public void testDeleteSingleLabel() {
        labelsPage.openLabelsPage();
        String labelToDelete = "task";
        assertTrue(labelsPage.isLabelPresent(labelToDelete), "Label to delete should exist initially");

        labelsPage.deleteLabel(labelToDelete);

        assertFalse(labelsPage.isLabelPresent(labelToDelete), "Deleted label should no longer be present in table");
    }

    @Test
    @DisplayName("Bulk deletes all labels and confirms empty list")
    public void testBulkDeleteLabels() {
        labelsPage.openLabelsPage();
        assertTrue(labelsPage.getLabelsCount() > 0, "Labels should be present before bulk delete");

        labelsPage.selectAll();
        labelsPage.deleteSelected();

        assertEquals(0, labelsPage.getLabelsCount(), "All labels should be removed after bulk delete");
    }
}
