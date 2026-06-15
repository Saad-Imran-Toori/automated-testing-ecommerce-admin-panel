package gridtest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;

public class ParallelGridTest {

    WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) throws Exception {

        URL gridUrl = new URL("http://localhost:4444");

        if (browser.equalsIgnoreCase("chrome")) {

            ChromeOptions options = new ChromeOptions();
            driver = new RemoteWebDriver(gridUrl, options);

            System.out.println("✅ Chrome connected to Grid");

        } else if (browser.equalsIgnoreCase("firefox")) {

            FirefoxOptions options = new FirefoxOptions();
            driver = new RemoteWebDriver(gridUrl, options);

            System.out.println("✅ Firefox connected to Grid");
        }
    }

    @Test(priority = 1)
    public void testPageLoads() throws Exception {

        driver.get("http://host.docker.internal/ecommerce_api/frontend/");

        Thread.sleep(3000);

        String pageSource = driver.getPageSource();

        System.out.println("Page Source Length: " + pageSource.length());

        Assert.assertTrue(
                pageSource.contains("API Testing Suite"),
                "Page does not contain API Testing Suite");

        System.out.println("✅ Page Load Test Passed");
    }

    @Test(priority = 2)
    public void testLoginButtonExists() throws Exception {

        driver.get("http://host.docker.internal/ecommerce_api/frontend/");

        Thread.sleep(3000);

        String pageSource = driver.getPageSource();

        boolean hasAdminLogin =
                pageSource.contains("Admin Login");

        System.out.println("Admin Login Found: " + hasAdminLogin);

        Assert.assertTrue(
                hasAdminLogin,
                "Admin Login button not found");

        System.out.println("✅ Admin Login Test Passed");
    }

    @Test(priority = 3)
    public void testCreateAccountButtonExists() throws Exception {

        driver.get("http://host.docker.internal/ecommerce_api/frontend/");

        Thread.sleep(3000);

        String pageSource = driver.getPageSource();

        boolean hasCreateAccount =
                pageSource.contains("Create Account");

        System.out.println("Create Account Found: " + hasCreateAccount);

        Assert.assertTrue(
                hasCreateAccount,
                "Create Account button not found");

        System.out.println("✅ Create Account Test Passed");
    }

    @AfterTest
    public void teardown() {

        if (driver != null) {

            driver.quit();

            System.out.println("🔒 Browser closed");
        }
    }
}