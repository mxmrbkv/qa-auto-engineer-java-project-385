package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TaskStatusesPage extends BasePage {

    // List Page Locators
    private final By statusesNavMenu = By.xpath("//nav//a[contains(@href, '#/task_statuses')] | //a[contains(@href, '#/task_statuses')]");
    private final By createButton = By.xpath("//a[contains(@href, '#/task_statuses/create')]");
    private final By table = By.cssSelector("table");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By columnNameHeader = By.cssSelector("th.column-name");
    private final By columnSlugHeader = By.cssSelector("th.column-slug");
    private final By nameCells = By.cssSelector("table tbody td.column-name");
    private final By slugCells = By.cssSelector("table tbody td.column-slug");
    private final By selectAllSpan = By.cssSelector("th span.select-all");
    private final By bulkDeleteButton = By.cssSelector("div.RaBulkActionsToolbar-toolbar button[aria-label='Delete']");

    // Form Locators
    private final By nameInput = By.name("name");
    private final By slugInput = By.name("slug");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By deleteButton = By.xpath("//button[@aria-label='Delete' or contains(@class, 'ra-delete-button') or contains(., 'Delete')]");

    public TaskStatusesPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public TaskStatusesPage openTaskStatusesPage() {
        open("/#/task_statuses");
        waitForVisibility(table);
        return this;
    }

    public TaskStatusesPage navigateViaMenu() {
        waitForClickable(statusesNavMenu).click();
        waitForVisibility(table);
        return this;
    }

    public TaskStatusesPage openCreateForm() {
        waitForClickable(createButton).click();
        waitForVisibility(nameInput);
        return this;
    }

    public boolean isCreateFormOpen() {
        try {
            return waitForVisibility(nameInput).isDisplayed()
                    && waitForVisibility(slugInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillForm(String name, String slug) {
        WebElement nameEl = waitForVisibility(nameInput);
        clearAndSendKeys(nameEl, name);

        WebElement slugEl = waitForVisibility(slugInput);
        clearAndSendKeys(slugEl, slug);
    }

    public void createStatus(String name, String slug) {
        openCreateForm();
        fillForm(name, slug);
        waitForClickable(saveButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/create")));
        openTaskStatusesPage();
    }

    public void openEditFormForStatus(String name) {
        WebElement row = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tbody//tr[.//td[contains(@class, 'column-name') and contains(., '" + name + "')]]")
        ));
        row.click();
        waitForVisibility(nameInput);
    }

    public void updateStatus(String newName, String newSlug) {
        fillForm(newName, newSlug);
        waitForClickable(saveButton).click();
        openTaskStatusesPage();
    }

    public void deleteCurrentUserInEditForm() {
        waitForClickable(deleteButton).click();
        waitForVisibility(table);
    }

    public void deleteStatus(String name) {
        openEditFormForStatus(name);
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
            return waitForVisibility(columnNameHeader).isDisplayed()
                    && waitForVisibility(columnSlugHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getStatusNames() {
        waitForVisibility(table);
        return driver.findElements(nameCells).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public List<String> getStatusSlugs() {
        waitForVisibility(table);
        return driver.findElements(slugCells).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isStatusPresent(String name) {
        return getStatusNames().contains(name.trim());
    }

    public int getStatusesCount() {
        try {
            waitForVisibility(table);
            return driver.findElements(tableRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getNameInputValue() {
        return waitForVisibility(nameInput).getDomProperty("value");
    }

    public String getSlugInputValue() {
        return waitForVisibility(slugInput).getDomProperty("value");
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
