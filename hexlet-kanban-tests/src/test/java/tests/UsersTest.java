package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.UsersPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersTest extends BaseTest {

    private UsersPage usersPage;

    @BeforeEach
    public void setUpUsers() {
        loginAsAdmin();
        usersPage = new UsersPage(driver, baseUrl);
    }

    @Test
    @DisplayName("Displays users table with key headers and data")
    public void testViewUsersList() {
        usersPage.openUsersPage();

        assertTrue(usersPage.isTableDisplayed(), "Users table should be displayed");
        assertTrue(usersPage.areKeyHeadersDisplayed(), "Key column headers should be displayed");
        assertTrue(usersPage.getUsersCount() > 0, "Users table should contain rows");
        assertTrue(usersPage.isUserPresent("john@google.com"), "Initial user should be present in table");
    }

    @Test
    @DisplayName("Opens create user form correctly")
    public void testOpenCreateUserForm() {
        usersPage.openUsersPage();
        usersPage.openCreateForm();

        assertTrue(usersPage.isCreateFormOpen(), "Create user form inputs should be displayed");
    }

    @Test
    @DisplayName("Creates a new user successfully and shows it in the list")
    public void testCreateUser() {
        usersPage.openUsersPage();
        String newEmail = "newuser_" + System.currentTimeMillis() + "@hexlet.io";
        usersPage.createUser(newEmail, "Hexlet", "Student");

        assertTrue(usersPage.isUserPresent(newEmail), "Newly created user should appear in users table");
    }

    @Test
    @DisplayName("Opens edit form with prefilled data and updates user")
    public void testEditUser() {
        usersPage.openUsersPage();
        String targetEmail = "jack@yahoo.com";
        usersPage.openEditFormForUser(targetEmail);

        assertEquals(targetEmail, usersPage.getEmailInputValue(), "Email should be prefilled");
        assertEquals("Jack", usersPage.getFirstNameInputValue(), "First name should be prefilled");
        assertEquals("Jons", usersPage.getLastNameInputValue(), "Last name should be prefilled");

        String updatedEmail = "jack.updated@yahoo.com";
        usersPage.updateUser(updatedEmail, "JackNew", "JonsNew");

        assertTrue(usersPage.isUserPresent(updatedEmail), "Updated user email should be visible in list");
    }

    @Test
    @DisplayName("Validates incorrect email format on user form")
    public void testEmailValidation() {
        usersPage.openUsersPage();
        usersPage.openCreateForm();
        usersPage.fillForm("invalid-email-format", "Ivan", "Ivanov");

        // Click save to trigger form validation
        driver.findElement(org.openqa.selenium.By.cssSelector("button[type='submit']")).click();

        assertTrue(usersPage.isEmailErrorDisplayed(), "Email format validation error should be displayed");
        assertTrue(usersPage.getEmailErrorMessage().contains("Incorrect email format"),
                "Error message should indicate incorrect email format");
    }

    @Test
    @DisplayName("Deletes a single user and confirms removal from list")
    public void testDeleteSingleUser() {
        usersPage.openUsersPage();
        String userToDelete = "peter@outlook.com";
        assertTrue(usersPage.isUserPresent(userToDelete), "User should initially exist");

        usersPage.deleteUser(userToDelete);

        assertFalse(usersPage.isUserPresent(userToDelete), "Deleted user should not be present in table");
    }

    @Test
    @DisplayName("Bulk deletes all users and verifies empty list")
    public void testBulkDeleteUsers() {
        usersPage.openUsersPage();
        assertTrue(usersPage.getUsersCount() > 0, "Users should be present before bulk delete");

        usersPage.selectAll();
        usersPage.deleteSelected();

        assertEquals(0, usersPage.getUsersCount(), "All users should be removed after bulk delete");
    }
}
