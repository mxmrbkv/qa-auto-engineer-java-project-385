package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public class BaseTest {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = System.getenv().getOrDefault("APP_BASE_URL", "http://localhost:5173");
        driver = DriverFactory.create();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
