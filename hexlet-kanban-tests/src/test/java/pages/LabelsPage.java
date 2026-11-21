package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LabelsPage extends BasePage {

    // List Page Locators
    private final By labelsNavMenu = By.xpath("//nav//a[contains(@href, '#/labels')] | //a[contains(@href, '#/labels')]");
    private final By createButton = By.xpath("//a[contains(@href, '#/labels/create')]");
    private final By table = By.cssSelector("table");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By columnNameHeader = By.cssSelector("th.column-name");
    private final By nameCells = By.cssSelector("table tbody td.column-name");
    private final By selectAllSpan = By.cssSelector("th span.select-all");
    private final By bulkDeleteButton = By.cssSelector("div.RaBulkActionsToolbar-toolbar button[aria-label='Delete']");

    // Form Locators
    private final By nameInput = By.name("name");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By deleteButton = By.xpath("//button[@aria-label='Delete' or contains(@class, 'ra-delete-button') or contains(., 'Delete')]");

    public LabelsPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public LabelsPage openLabelsPage() {
        open("/#/labels");
        waitForVisibility(table);
        return this;
    }

    public LabelsPage navigateViaMenu() {
        waitForClickable(labelsNavMenu).click();
        waitForVisibility(table);
        return this;
    }

    public LabelsPage openCreateForm() {
        waitForClickable(createButton).click();
        waitForVisibility(nameInput);
        return this;
    }

    public boolean isCreateFormOpen() {
        try {
            return waitForVisibility(nameInput).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillForm(String name) {
        WebElement nameEl = waitForVisibility(nameInput);
        clearAndSendKeys(nameEl, name);
    }

    public void createLabel(String name) {
        openCreateForm();
        fillForm(name);
        waitForClickable(saveButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/create")));
        openLabelsPage();
    }

    public void openEditFormForLabel(String name) {
        WebElement row = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tbody//tr[.//td[contains(@class, 'column-name') and contains(., '" + name + "')]]")
        ));
        row.click();
        waitForVisibility(nameInput);
    }

    public void updateLabel(String newName) {
        fillForm(newName);
        waitForClickable(saveButton).click();
        openLabelsPage();
    }

    public void deleteCurrentUserInEditForm() {
        waitForClickable(deleteButton).click();
        waitForVisibility(table);
    }

    public void deleteLabel(String name) {
        openEditFormForLabel(name);
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

    public boolean isNameHeaderDisplayed() {
        try {
            return waitForVisibility(columnNameHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getLabelNames() {
        waitForVisibility(table);
        return driver.findElements(nameCells).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isLabelPresent(String name) {
        return getLabelNames().contains(name.trim());
    }

    public int getLabelsCount() {
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

    private void clearAndSendKeys(WebElement element, String text) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }
}
