package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By profileButton = By.xpath("//header//button[contains(@aria-label, 'Profile') or contains(., 'Jane Doe')]");
    private final By logoutMenuItem = By.cssSelector("li.logout");

    public LoginPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public LoginPage openLoginPage() {
        open("/#/login");
        waitForVisibility(usernameInput);
        return this;
    }

    public void login(String username, String password) {
        WebElement user = waitForVisibility(usernameInput);
        user.clear();
        user.sendKeys(username);

        WebElement pass = waitForVisibility(passwordInput);
        pass.clear();
        pass.sendKeys(password);

        waitForClickable(submitButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
    }

    public void logout() {
        waitForClickable(profileButton).click();
        waitForClickable(logoutMenuItem).click();
        wait.until(ExpectedConditions.urlContains("login"));
        waitForVisibility(usernameInput);
    }

    public boolean isLoggedIn() {
        try {
            waitForVisibility(profileButton);
            return !driver.getCurrentUrl().contains("login");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAtLoginPage() {
        try {
            waitForVisibility(usernameInput);
            waitForVisibility(passwordInput);
            waitForVisibility(submitButton);
            return driver.getCurrentUrl().contains("login");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUsernameFieldDisplayed() {
        try {
            return waitForVisibility(usernameInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordFieldDisplayed() {
        try {
            return waitForVisibility(passwordInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSubmitButtonDisplayed() {
        try {
            return waitForVisibility(submitButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubmitButtonText() {
        return waitForVisibility(submitButton).getText().trim();
    }
}
