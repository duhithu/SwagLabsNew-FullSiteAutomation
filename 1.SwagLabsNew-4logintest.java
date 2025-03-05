package StandardSelenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
//1.Login Senarios
public class SwagLabsNew {
    //Global Variable Section
    String BaseURL = "https://www.saucedemo.com/v1/index.html";
    WebDriver driver;
    String actualText;
    String expectedText;
    Boolean status;

    @BeforeTest
    public void BeforeTestMethod() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterTest
    public void AfterTestMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void testValidLogin() throws InterruptedException {
        // Test case for successful login
        driver.get(BaseURL);
        Thread.sleep(5000);

        driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys("standard_user");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

        Thread.sleep(3000);

        // Verify successful login by checking for products page
        actualText = driver.findElement(By.xpath("//div[@class='product_label']")).getText();
        expectedText = "Products";
        Assert.assertEquals(actualText, expectedText, "Login failed: Products page not displayed");
    }

    @Test(priority = 2)
    public void testInvalidUsername() throws InterruptedException {
        // Test case for invalid username
        driver.get(BaseURL);
        Thread.sleep(5000);

        driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys("invalid_user");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

        Thread.sleep(3000);

        // Verify error message
        actualText = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
        expectedText = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(actualText, expectedText, "Incorrect error message for invalid username");
    }

    @Test(priority = 3)
    public void testInvalidPassword() throws InterruptedException {
        // Test case for invalid password
        driver.get(BaseURL);
        Thread.sleep(5000);

        driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys("standard_user");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("wrong_password");
        driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

        Thread.sleep(3000);

        // Verify error message
        actualText = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
        expectedText = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(actualText, expectedText, "Incorrect error message for invalid password");
    }

    @Test(priority = 4)
    public void testEmptyCredentials() throws InterruptedException {
        // Test case for empty credentials
        driver.get(BaseURL);
        Thread.sleep(5000);

        // Click login without entering any credentials
        driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

        Thread.sleep(3000);

        // Verify error message
        actualText = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
        expectedText = "Epic sadface: Username is required";
        Assert.assertEquals(actualText, expectedText, "Incorrect error message for empty credentials");
    }

    @Test(priority = 5)
    public void testLockedOutUser() throws InterruptedException {
        // Test case for locked out user
        driver.get(BaseURL);
        Thread.sleep(5000);

        driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys("locked_out_user");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

        Thread.sleep(3000);

        // Verify error message
        actualText = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();
        expectedText = "Epic sadface: Sorry, this user has been locked out.";
        Assert.assertEquals(actualText, expectedText, "Incorrect error message for locked out user");
    }
}
