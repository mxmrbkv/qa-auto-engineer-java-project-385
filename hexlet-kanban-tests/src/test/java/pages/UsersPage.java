package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UsersPage extends BasePage {

    // List Page Locators
    private final By usersNavMenu = By.xpath("//nav//a[contains(@href, '#/users')] | //a[contains(@href, '#/users')]");
    private final By createButton = By.xpath("//a[contains(@href, '#/users/create')]");
    private final By table = By.cssSelector("table");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By columnEmailHeader = By.cssSelector("th.column-email");
    private final By columnFirstNameHeader = By.cssSelector("th.column-firstName");
    private final By columnLastNameHeader = By.cssSelector("th.column-lastName");
    private final By emailCells = By.cssSelector("table tbody td.column-email");
    private final By selectAllSpan = By.cssSelector("th span.select-all");
    private final By bulkDeleteButton = By.cssSelector("div.RaBulkActionsToolbar-toolbar button[aria-label='Delete']");

    // Form Page Locators
    private final By emailInput = By.name("email");
    private final By firstNameInput = By.name("firstName");
    private final By lastNameInput = By.name("lastName");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By deleteButton = By.xpath("//button[@aria-label='Delete' or contains(@class, 'ra-delete-button') or contains(., 'Delete')]");
    private final By emailError = By.xpath("//p[contains(@class, 'Mui-error') or contains(@class, 'MuiFormHelperText-root')][contains(., 'email') or contains(., 'format')]");

    public UsersPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public UsersPage openUsersPage() {
        open("/#/users");
        waitForVisibility(table);
        return this;
    }

    public UsersPage navigateViaMenu() {
        waitForClickable(usersNavMenu).click();
        waitForVisibility(table);
        return this;
    }

    public UsersPage openCreateForm() {
        waitForClickable(createButton).click();
        waitForVisibility(emailInput);
        return this;
    }

    public void fillForm(String email, String firstName, String lastName) {
        WebElement emailEl = waitForVisibility(emailInput);
        clearAndSendKeys(emailEl, email);

        WebElement firstEl = waitForVisibility(firstNameInput);
        clearAndSendKeys(firstEl, firstName);

        WebElement lastEl = waitForVisibility(lastNameInput);
        clearAndSendKeys(lastEl, lastName);
    }

    public void createUser(String email, String firstName, String lastName) {
        openCreateForm();
        fillForm(email, firstName, lastName);
        waitForClickable(saveButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/create")));
        openUsersPage();
    }

    public void openEditFormForUser(String email) {
        WebElement userRow = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tbody//tr[.//td[contains(@class, 'column-email') and contains(., '" + email + "')]]")
        ));
        userRow.click();
        waitForVisibility(emailInput);
    }

    public void updateUser(String newEmail, String newFirstName, String newLastName) {
        fillForm(newEmail, newFirstName, newLastName);
        waitForClickable(saveButton).click();
        waitForVisibility(table);
    }

    public void deleteCurrentUserInEditForm() {
        waitForClickable(deleteButton).click();
        waitForVisibility(table);
    }

    public void deleteUser(String email) {
        openEditFormForUser(email);
        deleteCurrentUserInEditForm();
    }

    public void selectAll() {
        waitForClickable(selectAllSpan).click();
    }

    public void deleteSelected() {
        waitForClickable(bulkDeleteButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(bulkDeleteButton));
        wait.until(d -> d.findElements(tableRows).isEmpty());
    }

    public boolean isTableDisplayed() {
        try {
            return waitForVisibility(table).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areKeyHeadersDisplayed() {
        try {
            return waitForVisibility(columnEmailHeader).isDisplayed()
                    && waitForVisibility(columnFirstNameHeader).isDisplayed()
                    && waitForVisibility(columnLastNameHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getUserEmails() {
        waitForVisibility(table);
        return driver.findElements(emailCells).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isUserPresent(String email) {
        return getUserEmails().contains(email.trim());
    }

    public int getUsersCount() {
        try {
            waitForVisibility(table);
            return driver.findElements(tableRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getEmailInputValue() {
        return waitForVisibility(emailInput).getDomProperty("value");
    }

    public String getFirstNameInputValue() {
        return waitForVisibility(firstNameInput).getDomProperty("value");
    }

    public String getLastNameInputValue() {
        return waitForVisibility(lastNameInput).getDomProperty("value");
    }

    public boolean isEmailErrorDisplayed() {
        try {
            return waitForVisibility(emailError).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailErrorMessage() {
        return waitForVisibility(emailError).getText().trim();
    }

    public boolean isCreateFormOpen() {
        try {
            return waitForVisibility(emailInput).isDisplayed()
                    && waitForVisibility(firstNameInput).isDisplayed()
                    && waitForVisibility(lastNameInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void clearAndSendKeys(WebElement element, String text) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }
}
