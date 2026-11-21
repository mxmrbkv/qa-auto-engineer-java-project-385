package tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthTest extends BaseTest {

    @Test
    @DisplayName("Displays main elements on login page")
    public void testLoginPageElements() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        loginPage.openLoginPage();

        assertEquals("Task manager", loginPage.getTitle());
        assertTrue(loginPage.isUsernameFieldDisplayed(), "Username input should be displayed");
        assertTrue(loginPage.isPasswordFieldDisplayed(), "Password input should be displayed");
        assertTrue(loginPage.isSubmitButtonDisplayed(), "Submit button should be displayed");
        assertTrue("Sign in".equalsIgnoreCase(loginPage.getSubmitButtonText()), "Submit button text should be 'Sign in'");
    }

    @Test
    @DisplayName("User logs in successfully with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        loginPage.openLoginPage();
        loginPage.login("admin", "password123");

        assertTrue(loginPage.isLoggedIn(), "User should be logged in and main application should be visible");
    }

    @Test
    @DisplayName("User logs out successfully")
    public void testSuccessfulLogout() {
        LoginPage loginPage = new LoginPage(driver, baseUrl);
        loginPage.openLoginPage();
        loginPage.login("admin", "password123");
        assertTrue(loginPage.isLoggedIn(), "User should be logged in before testing logout");

        loginPage.logout();
        assertTrue(loginPage.isAtLoginPage(), "User should be redirected to login page after logout");
    }

}
