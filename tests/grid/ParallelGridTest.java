import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParallelGridTest {
    
    WebDriver driver;
    
    @Test
    @Parameters("browser")
    public void testLoginPageOnBrowser(String browser) {
        
        // Initialize driver based on browser parameter
        switch(browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        // Navigate to your application
        driver.get("http://localhost/ecommerce_api/");
        
        // Print confirmation
        System.out.println("✓ Testing on: " + browser);
        System.out.println("✓ Page title: " + driver.getTitle());
        
        // Close browser
        driver.quit();
    }
}
