package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    protected String baseUrl;
    protected WebDriverWait wait;

    protected final By table = By.cssSelector("table");
    protected final By tableRows = By.cssSelector("table tbody tr");
    protected final By selectAllSpan = By.cssSelector("th span.select-all");
    protected final By bulkDeleteButton = By.cssSelector("div.RaBulkActionsToolbar-toolbar button[aria-label='Delete']");
    protected final By saveButton = By.cssSelector("button[type='submit']");
    protected final By deleteButton = By.xpath("//button[@aria-label='Delete' or contains(@class, 'ra-delete-button') or contains(., 'Delete')]");

    public BasePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String path) {
        driver.get(baseUrl + path);
    }

    public void open() {
        driver.get(baseUrl);
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForUrlNotContains(String fragment) {
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains(fragment)));
    }

    protected void clearAndSendKeys(WebElement element, String text) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

    protected void clearAndSendKeys(By locator, String text) {
        clearAndSendKeys(waitForVisibility(locator), text);
    }

    public void selectAll() {
        waitForClickable(selectAllSpan).click();
    }

    public void deleteSelected() {
        waitForClickable(bulkDeleteButton).click();
        waitForInvisibility(bulkDeleteButton);
        wait.until(d -> d.findElements(tableRows).isEmpty());
    }

    public boolean isTableDisplayed() {
        try {
            return waitForVisibility(table).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getTableRowsCount() {
        try {
            waitForVisibility(table);
            return driver.findElements(tableRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickSave() {
        waitForClickable(saveButton).click();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
