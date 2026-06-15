package com.ecommerce.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import java.io.IOException;
import java.util.logging.Level;

public class CompleteWebsiteTest {

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        java.util.logging.Logger.getLogger("org.htmlunit").setLevel(Level.OFF);
        
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.setJavaScriptTimeout(10000);
        webClient.getOptions().setTimeout(30000);
    }

    @AfterEach
    public void tearDown() {
        if (webClient != null) {
            webClient.close();
        }
    }

    // ============================================================
    // INDEX.HTML Tests
    // ============================================================

    @Test
    public void testIndexPageLoads() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/index.html");
        assertTrue(page.getTitleText().contains("E-Commerce API Testing Suite"));
        System.out.println("✅ Index page loads");
    }

    @Test
    public void testIndexPageHasAdminLoginButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/index.html");
        HtmlAnchor adminLink = page.getFirstByXPath("//a[contains(@class, 'admin-btn')]");
        assertNotNull(adminLink, "Admin Portal button not found");
        System.out.println("✅ Admin login button exists");
    }

    @Test
    public void testIndexPageHasCreateAccountButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/index.html");
        HtmlAnchor createLink = page.getFirstByXPath("//a[contains(@class, 'btn-outline')]");
        assertNotNull(createLink, "Create Account button not found");
        System.out.println("✅ Create Account button exists");
    }

    @Test
    public void testIndexPageHasFeaturesSection() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/index.html");
        String content = page.asNormalizedText();
        assertTrue(content.contains("REST API"), "Features section missing");
        System.out.println("✅ Features section exists");
    }

    // ============================================================
    // ADMIN_LOGIN.HTML Tests
    // ============================================================

    @Test
    public void testAdminLoginPageLoads() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_login.html");
        assertTrue(page.getTitleText().contains("Admin Login"));
        System.out.println("✅ Login page loads");
    }

    @Test
    public void testLoginFormHasEmailField() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_login.html");
        HtmlInput emailField = page.getHtmlElementById("email");
        assertNotNull(emailField, "Email field missing");
        System.out.println("✅ Email field exists");
    }

    @Test
    public void testLoginFormHasPasswordField() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_login.html");
        HtmlInput passwordField = page.getHtmlElementById("password");
        assertNotNull(passwordField, "Password field missing");
        System.out.println("✅ Password field exists");
    }

    @Test
    public void testLoginFormHasSubmitButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_login.html");
        HtmlButton submitButton = page.getFirstByXPath("//button[contains(@class, 'btn-login')]");
        assertNotNull(submitButton, "Login button missing");
        System.out.println("✅ Login button exists");
    }

    @Test
    public void testLoginFormHasSignupLink() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_login.html");
        HtmlAnchor signupLink = page.getFirstByXPath("//a[contains(text(),'Create Admin Account')]");
        assertNotNull(signupLink, "Signup link missing");
        System.out.println("✅ Signup link exists");
    }

    // ============================================================
    // ADMIN_SIGNUP.HTML Tests
    // ============================================================

    @Test
    public void testSignupPageLoads() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_signup.html");
        assertTrue(page.getTitleText().contains("Admin Signup"));
        System.out.println("✅ Signup page loads");
    }

    @Test
    public void testSignupFormHasAllFields() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_signup.html");
        
        assertNotNull(page.getHtmlElementById("firstName"), "First name field missing");
        assertNotNull(page.getHtmlElementById("lastName"), "Last name field missing");
        assertNotNull(page.getHtmlElementById("email"), "Email field missing");
        assertNotNull(page.getHtmlElementById("password"), "Password field missing");
        assertNotNull(page.getHtmlElementById("confirmPassword"), "Confirm password field missing");
        
        System.out.println("✅ All signup fields exist");
    }

    @Test
    public void testSignupFormHasCreateAccountButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_signup.html");
        HtmlButton submitButton = page.getFirstByXPath("//button[contains(@class, 'btn-signup')]");
        assertNotNull(submitButton, "Create Account button missing");
        System.out.println("✅ Create Account button exists");
    }

    @Test
    public void testSignupFormHasLoginLink() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_signup.html");
        HtmlAnchor loginLink = page.getFirstByXPath("//a[contains(text(),'Login here')]");
        assertNotNull(loginLink, "Login link missing");
        System.out.println("✅ Login link exists");
    }

    // ============================================================
    // ADMIN_DASHBOARD.HTML Tests
    // ============================================================

    @Test
    public void testDashboardPageLoads() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        String content = page.asNormalizedText();
        assertTrue(content.contains("Admin Dashboard"), "Dashboard not loaded");
        System.out.println("✅ Dashboard loads");
    }

    @Test
    public void testDashboardHasAddProductForm() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        
        assertNotNull(page.getHtmlElementById("productName"), "Product name field missing");
        assertNotNull(page.getHtmlElementById("productPrice"), "Product price field missing");
        assertNotNull(page.getHtmlElementById("productCat"), "Category dropdown missing");
        
        System.out.println("✅ Add product form exists");
    }

    @Test
    public void testDashboardHasAddProductButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        // Look for button that contains "Add Product" text
        HtmlButton addButton = page.getFirstByXPath("//button[contains(text(), 'Add Product')]");
        if (addButton == null) {
            // Alternative: look for submit button with btn-primary class
            addButton = page.getFirstByXPath("//button[@type='submit' and contains(@class, 'btn-primary')]");
        }
        assertNotNull(addButton, "Add Product button not found");
        System.out.println("✅ Add Product button exists");
    }

    @Test
    public void testDashboardHasViewInventoryButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        HtmlButton inventoryButton = page.getHtmlElementById("toggleInventoryBtn");
        if (inventoryButton == null) {
            inventoryButton = page.getFirstByXPath("//button[contains(text(), 'View Inventory')]");
        }
        assertNotNull(inventoryButton, "View Inventory button missing");
        System.out.println("✅ View Inventory button exists");
    }

    @Test
    public void testDashboardHasProductTable() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        HtmlTable productTable = page.getHtmlElementById("productsTable");
        assertNotNull(productTable, "Products table missing");
        System.out.println("✅ Product table exists");
    }

    @Test
    public void testDashboardHasLogoutButton() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        // Look for button that contains "Logout" text
        HtmlButton logoutButton = page.getFirstByXPath("//button[contains(text(), 'Logout')]");
        if (logoutButton == null) {
            // Alternative: look by onclick attribute
            logoutButton = page.getFirstByXPath("//button[@onclick='logout()']");
        }
        assertNotNull(logoutButton, "Logout button not found");
        System.out.println("✅ Logout button exists");
    }

    @Test
    public void testDashboardHasAdminNameDisplay() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        HtmlSpan adminName = page.getHtmlElementById("adminName");
        assertNotNull(adminName, "Admin name display missing");
        System.out.println("✅ Admin name display exists");
    }

    // ============================================================
    // NAVIGATION FLOW Tests
    // ============================================================

    @Test
    public void testNavigationFromIndexToLogin() throws IOException {
        HtmlPage indexPage = webClient.getPage("http://localhost/ecommerce_api/frontend/index.html");
        HtmlAnchor adminLink = indexPage.getFirstByXPath("//a[contains(@class, 'admin-btn')]");
        HtmlPage loginPage = adminLink.click();
        assertTrue(loginPage.getTitleText().contains("Admin Login"));
        System.out.println("✅ Index → Login works");
    }

    @Test
    public void testNavigationFromLoginToSignup() throws IOException {
        HtmlPage loginPage = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_login.html");
        HtmlAnchor signupLink = loginPage.getFirstByXPath("//a[contains(text(),'Create Admin Account')]");
        HtmlPage signupPage = signupLink.click();
        assertTrue(signupPage.getTitleText().contains("Admin Signup"));
        System.out.println("✅ Login → Signup works");
    }

    @Test
    public void testNavigationFromSignupToLogin() throws IOException {
        HtmlPage signupPage = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_signup.html");
        HtmlAnchor loginLink = signupPage.getFirstByXPath("//a[contains(text(),'Login here')]");
        HtmlPage loginPage = loginLink.click();
        assertTrue(loginPage.getTitleText().contains("Admin Login"));
        System.out.println("✅ Signup → Login works");
    }

    // ============================================================
    // STRUCTURE Tests
    // ============================================================

    @Test
    public void testAllPagesHaveViewportMetaTag() throws IOException {
        String[] pages = {
            "http://localhost/ecommerce_api/frontend/index.html",
            "http://localhost/ecommerce_api/frontend/admin_login.html",
            "http://localhost/ecommerce_api/frontend/admin_signup.html",
            "http://localhost/ecommerce_api/frontend/admin_dashboard.html"
        };
        for (String pageUrl : pages) {
            HtmlPage page = webClient.getPage(pageUrl);
            assertTrue(page.asXml().contains("viewport"), "Missing viewport: " + pageUrl);
        }
        System.out.println("✅ All pages have viewport");
    }

    @Test
    public void testAllPagesHaveFontAwesome() throws IOException {
        String[] pages = {
            "http://localhost/ecommerce_api/frontend/index.html",
            "http://localhost/ecommerce_api/frontend/admin_login.html",
            "http://localhost/ecommerce_api/frontend/admin_signup.html",
            "http://localhost/ecommerce_api/frontend/admin_dashboard.html"
        };
        for (String pageUrl : pages) {
            HtmlPage page = webClient.getPage(pageUrl);
            String content = page.asXml();
            assertTrue(content.contains("font-awesome") || content.contains("fontawesome"), 
                      "Missing Font Awesome: " + pageUrl);
        }
        System.out.println("✅ All pages have Font Awesome");
    }

    // ============================================================
    // CSS CLASS Tests
    // ============================================================

    @Test
    public void testDashboardHasFormCardClass() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        HtmlDivision formCard = page.getFirstByXPath("//div[contains(@class, 'form-card')]");
        assertNotNull(formCard, "form-card class missing");
        System.out.println("✅ Dashboard has form-card");
    }

    @Test
    public void testDashboardHasProductsCardClass() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        HtmlDivision productsCard = page.getFirstByXPath("//div[contains(@class, 'products-card')]");
        assertNotNull(productsCard, "products-card class missing");
        System.out.println("✅ Dashboard has products-card");
    }

    @Test
    public void testDashboardHasPrimaryButtonClass() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/ecommerce_api/frontend/admin_dashboard.html");
        HtmlButton primaryButton = page.getFirstByXPath("//button[contains(@class, 'btn-primary')]");
        assertNotNull(primaryButton, "btn-primary class button missing");
        System.out.println("✅ Dashboard has btn-primary class");
    }
}