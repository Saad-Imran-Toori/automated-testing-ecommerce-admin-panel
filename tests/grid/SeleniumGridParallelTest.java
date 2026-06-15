package com.ecommerce.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.net.URL;

/**
 * Selenium Grid Parallel Test Class
 * Tests automation on multiple browsers simultaneously using Selenium Grid
 */
public class SeleniumGridParallelTest {
    
    WebDriver driver;
    
    // URLs
    private static final String LANDING_PAGE_URL = "http://localhost/ecommerce_api/frontend/index.html";
    private static final String ADMIN_LOGIN_URL = "http://localhost/ecommerce_api/frontend/admin_login.html";
    
    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) throws Exception {
        URL gridUrl = new URL("http://localhost:4444");
        
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            driver = new RemoteWebDriver(gridUrl, options);
            System.out.println("✓ Chrome test connected to Selenium Grid");
        } 
        else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            driver = new RemoteWebDriver(gridUrl, options);
            System.out.println("✓ Firefox test connected to Selenium Grid");
        }
        
        driver.manage().window().maximize();
    }
    
    @Test
    public void testLoginPageLoads() {
        driver.get(ADMIN_LOGIN_URL);
        String title = driver.getTitle();
        System.out.println("✓ Page Title: " + title);
        assert title != null : "Page title should not be null";
    }
    
    @Test
    public void testPageHasEmailField() {
        driver.get(ADMIN_LOGIN_URL);
        boolean hasEmailField = driver.getPageSource().contains("email") || 
                                driver.getPageSource().contains("Email");
        System.out.println("✓ Email field present: " + hasEmailField);
        assert hasEmailField : "Email field should be present on login page";
    }
    
    @Test
    public void testPageHasPasswordField() {
        driver.get(ADMIN_LOGIN_URL);
        boolean hasPasswordField = driver.getPageSource().contains("password") || 
                                   driver.getPageSource().contains("Password");
        System.out.println("✓ Password field present: " + hasPasswordField);
        assert hasPasswordField : "Password field should be present on login page";
    }
    
    @Test
    public void testLandingPageLoads() {
        driver.get(LANDING_PAGE_URL);
        String title = driver.getTitle();
        System.out.println("✓ Landing Page Title: " + title);
        assert title != null : "Landing page title should not be null";
    }
    
    @Test
    public void testBrowserCompatibility() {
        if (driver instanceof RemoteWebDriver) {
            RemoteWebDriver remoteDriver = (RemoteWebDriver) driver;
            String capabilities = remoteDriver.getCapabilities().toString();
            System.out.println("✓ Browser Capabilities: " + capabilities);
        }
        assert driver != null : "WebDriver should be initialized";
    }
    
    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed");
        }
    }
}
