package pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LabelsPage extends BasePage {

    // List Page Locators
    private final By labelsNavMenu = By.xpath("//nav//a[contains(@href, '#/labels')] | //a[contains(@href, '#/labels')]");
    private final By createButton = By.xpath("//a[contains(@href, '#/labels/create')]");
    private final By columnNameHeader = By.cssSelector("th.column-name");
    private final By nameCells = By.cssSelector("table tbody td.column-name");

    // Form Locators
    private final By nameInput = By.name("name");

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
        clearAndSendKeys(nameInput, name);
    }

    public void createLabel(String name) {
        openCreateForm();
        fillForm(name);
        clickSave();
        waitForUrlNotContains("/create");
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
        clickSave();
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
        return getTableRowsCount();
    }

    public String getNameInputValue() {
        return waitForVisibility(nameInput).getDomProperty("value");
    }
}
