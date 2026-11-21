package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class TasksPage extends BasePage {

    // Board & List Locators
    private final By tasksNavMenu = By.xpath("//nav//a[contains(@href, '#/tasks')] | //a[contains(@href, '#/tasks')]");
    private final By createButton = By.xpath("//a[contains(@href, '#/tasks/create')]");
    private final By taskCards = By.xpath("//div[contains(@class, 'MuiCard-root')]");
    private final By taskTitles = By.xpath("//div[contains(@class, 'MuiCard-root')]//*[contains(@class, 'MuiTypography-h5')]");

    // Form Locators (Create / Edit)
    private final By titleInput = By.name("title");
    private final By contentInput = By.name("content");
    private final By assigneeSelect = By.xpath("//div[contains(@class, 'ra-input-assignee_id')]//div[@role='combobox'] | //div[contains(@class, 'ra-input-assignee_id')]//*[@role='button']");
    private final By statusSelect = By.xpath("//div[contains(@class, 'ra-input-status_id')]//div[@role='combobox'] | //div[contains(@class, 'ra-input-status_id')]//*[@role='button']");

    // Filters on Tasks board
    private final By filterAssigneeSelect = By.xpath("//div[contains(@class, 'filter-field') and contains(@class, 'assignee_id')]//div[@role='combobox'] | //div[contains(@class, 'ra-input-assignee_id')]//div[@role='combobox']");
    private final By filterStatusSelect = By.xpath("//div[contains(@class, 'filter-field') and contains(@class, 'status_id')]//div[@role='combobox'] | //div[contains(@class, 'ra-input-status_id')]//div[@role='combobox']");
    private final By filterLabelSelect = By.xpath("//div[contains(@class, 'filter-field') and contains(@class, 'label_id')]//div[@role='combobox'] | //div[contains(@class, 'ra-input-label_id')]//div[@role='combobox']");

    public TasksPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public TasksPage openTasksPage() {
        open("/#/tasks");
        waitForVisibility(taskCards);
        return this;
    }

    public TasksPage navigateViaMenu() {
        waitForClickable(tasksNavMenu).click();
        waitForVisibility(taskCards);
        return this;
    }

    public TasksPage openCreateForm() {
        waitForClickable(createButton).click();
        waitForVisibility(titleInput);
        return this;
    }

    public boolean isCreateFormOpen() {
        try {
            return waitForVisibility(titleInput).isDisplayed()
                    && waitForVisibility(assigneeSelect).isDisplayed()
                    && waitForVisibility(statusSelect).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectDropdownOption(By dropdownLocator, String optionText) {
        WebElement selectBox = waitForClickable(dropdownLocator);
        selectBox.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@role='listbox']//li[contains(., '" + optionText + "')]")
        ));
        option.click();
    }

    public void fillForm(String title, String content, String assigneeEmail, String statusName) {
        WebElement titleEl = waitForVisibility(titleInput);
        clearAndSendKeys(titleEl, title);

        if (content != null) {
            WebElement contentEl = waitForVisibility(contentInput);
            clearAndSendKeys(contentEl, content);
        }

        if (assigneeEmail != null) {
            selectDropdownOption(assigneeSelect, assigneeEmail);
        }

        if (statusName != null) {
            selectDropdownOption(statusSelect, statusName);
        }
    }

    public void createTask(String title, String content, String assigneeEmail, String statusName) {
        openCreateForm();
        fillForm(title, content, assigneeEmail, statusName);
        waitForClickable(saveButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/create")));
        openTasksPage();
    }

    public void openEditFormForTask(String title) {
        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'MuiCard-root')][.//*[contains(@class, 'MuiTypography-h5') and normalize-space()='" + title + "']]//a[contains(@href, '/tasks/')]")
        ));
        editBtn.click();
        waitForVisibility(titleInput);
    }

    public void updateTask(String newTitle, String newContent, String newStatusName) {
        if (newTitle != null) {
            WebElement titleEl = waitForVisibility(titleInput);
            clearAndSendKeys(titleEl, newTitle);
        }
        if (newContent != null) {
            WebElement contentEl = waitForVisibility(contentInput);
            clearAndSendKeys(contentEl, newContent);
        }
        if (newStatusName != null) {
            selectDropdownOption(statusSelect, newStatusName);
        }
        waitForClickable(saveButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/tasks/")));
        openTasksPage();
    }

    public void deleteTask(String title) {
        openEditFormForTask(title);
        waitForClickable(deleteButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/tasks/")));
        openTasksPage();
    }

    public boolean isTaskPresent(String title) {
        return getTaskTitles().contains(title.trim());
    }

    public List<String> getTaskTitles() {
        waitForVisibility(taskCards);
        return driver.findElements(taskTitles).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public int getTasksCount() {
        try {
            waitForVisibility(taskCards);
            return driver.findElements(taskCards).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isTaskInColumn(String taskTitle, String statusName) {
        By locator = By.xpath("//div[.//h6[contains(., '" + statusName + "')] or .//*[contains(@class, 'MuiTypography-subtitle1') and contains(., '" + statusName + "')]]//div[contains(@class, 'MuiCard-root')][.//*[contains(@class, 'MuiTypography-h5') and normalize-space()='" + taskTitle + "']]");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void moveTaskByEditForm(String taskTitle, String targetStatusName) {
        openEditFormForTask(taskTitle);
        selectDropdownOption(statusSelect, targetStatusName);
        waitForClickable(saveButton).click();
        openTasksPage();
    }

    public void dragAndDropTask(String taskTitle, String targetStatusName) {
        WebElement sourceCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'MuiCard-root')][.//h5[contains(., '" + taskTitle + "')] or .//*[contains(., '" + taskTitle + "')]]")
        ));
        WebElement targetColumn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[.//h6[contains(., '" + targetStatusName + "')] or .//*[contains(@class, 'MuiTypography-subtitle1') and contains(., '" + targetStatusName + "')]]")
        ));

        new Actions(driver)
                .clickAndHold(sourceCard)
                .moveToElement(targetColumn)
                .pause(java.time.Duration.ofMillis(300))
                .release(targetColumn)
                .build()
                .perform();
    }

    public void filterByStatus(String statusName) {
        WebElement selectBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form//div[contains(@class, 'ra-input-status_id')]//div[@role='combobox']")
        ));
        selectBox.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@role='listbox']//li[contains(., '" + statusName + "')]")
        ));
        option.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));
    }

    public void filterByAssignee(String assigneeEmail) {
        WebElement selectBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form//div[contains(@class, 'ra-input-assignee_id')]//div[@role='combobox']")
        ));
        selectBox.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@role='listbox']//li[contains(., '" + assigneeEmail + "')]")
        ));
        option.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));
    }

    public void filterByLabel(String labelName) {
        WebElement selectBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//form//div[contains(@class, 'ra-input-label_id')]//div[@role='combobox']")
        ));
        selectBox.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@role='listbox']//li[contains(., '" + labelName + "')]")
        ));
        option.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ul[@role='listbox']")));
    }

    public String getTitleInputValue() {
        return waitForVisibility(titleInput).getDomProperty("value");
    }

    public String getContentInputValue() {
        return waitForVisibility(contentInput).getDomProperty("value");
    }
}
