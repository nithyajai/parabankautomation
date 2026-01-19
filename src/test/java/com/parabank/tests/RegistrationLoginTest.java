package com.parabank.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.UUID;

public class RegistrationLoginTest  {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // DataProvider for multiple users
    @DataProvider(name = "userData")
    public Object[][] userDataProvider() {
        return new Object[][]{
                {"Nithya", "Jayachandran", "Test Street", "Chennai", "TN", "600001", "9876543210", "123456789"},
        };
    }

    @Test(dataProvider = "userData")
    public void registrationAndLoginTest(String firstName, String lastName, String street, String city, String state,
                                         String zip, String phone, String ssn) {

        String dynamicUsername = "user_" + UUID.randomUUID().toString().substring(0, 5);
        String password = "Password@123";

        // Registration
        driver.get("https://parabank.parasoft.com/parabank/register.htm");
        driver.findElement(By.id("customer.firstName")).sendKeys(firstName);
        driver.findElement(By.id("customer.lastName")).sendKeys(lastName);
        driver.findElement(By.id("customer.address.street")).sendKeys(street);
        driver.findElement(By.id("customer.address.city")).sendKeys(city);
        driver.findElement(By.id("customer.address.state")).sendKeys(state);
        driver.findElement(By.id("customer.address.zipCode")).sendKeys(zip);
        driver.findElement(By.id("customer.phoneNumber")).sendKeys(phone);
        driver.findElement(By.id("customer.ssn")).sendKeys(ssn);
        driver.findElement(By.id("customer.username")).sendKeys(dynamicUsername);
        driver.findElement(By.id("customer.password")).sendKeys(password);
        driver.findElement(By.id("repeatedPassword")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Register']")).click();

        // Wait for registration success
        String successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1"))).getText();
        Assert.assertEquals(successMessage, "Welcome " + dynamicUsername);

        // Login immediately after registration
        driver.findElement(By.linkText("Log Out")).click();
        driver.get("https://parabank.parasoft.com/parabank/index.htm");
        driver.findElement(By.name("username")).sendKeys(dynamicUsername);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Log In']")).click();

        String accountOverviewHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1"))).getText();
        Assert.assertEquals(accountOverviewHeader, "Accounts Overview");
    }
    
}
