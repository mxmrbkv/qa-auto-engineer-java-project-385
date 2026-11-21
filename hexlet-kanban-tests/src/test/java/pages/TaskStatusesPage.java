package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TaskStatusesPage extends BasePage {

    // List Page Locators
    private final By statusesNavMenu = By.xpath("//nav//a[contains(@href, '#/task_statuses')] | //a[contains(@href, '#/task_statuses')]");
    private final By createButton = By.xpath("//a[contains(@href, '#/task_statuses/create')]");
    private final By columnNameHeader = By.cssSelector("th.column-name");
    private final By columnSlugHeader = By.cssSelector("th.column-slug");
    private final By nameCells = By.cssSelector("table tbody td.column-name");
    private final By slugCells = By.cssSelector("table tbody td.column-slug");

    // Form Locators
    private final By nameInput = By.name("name");
    private final By slugInput = By.name("slug");

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
        clearAndSendKeys(nameInput, name);
        clearAndSendKeys(slugInput, slug);
    }

    public void createStatus(String name, String slug) {
        openCreateForm();
        fillForm(name, slug);
        clickSave();
        waitForUrlNotContains("/create");
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
        clickSave();
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
        return getTableRowsCount();
    }

    public String getNameInputValue() {
        return waitForVisibility(nameInput).getDomProperty("value");
    }

    public String getSlugInputValue() {
        return waitForVisibility(slugInput).getDomProperty("value");
    }
}
