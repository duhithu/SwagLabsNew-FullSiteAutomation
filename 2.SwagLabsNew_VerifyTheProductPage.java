package StandardSelenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

//2.Verify the Product Page
public class SwagLabsNew_VerifyTheProductPage {
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

    @Test
    public void testValidLoginAndProductPage() throws InterruptedException {
        // Navigate to the login page
        driver.get(BaseURL);
        Thread.sleep(5000);

        // Login with valid credentials
        driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys("standard_user");
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

        Thread.sleep(3000);

        // Verify successful login and Products page elements
        // Verify Products header
        actualText = driver.findElement(By.xpath("//div[@class='product_label']")).getText();
        expectedText = "Products";
        Assert.assertEquals(actualText, expectedText, "Products header not found");


    }
}
