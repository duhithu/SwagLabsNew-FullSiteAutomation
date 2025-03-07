package StandardSelenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
//5. Product card
public class SawgLabsNew_ProductCardTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void userLogin() throws InterruptedException {
        driver.get("https://www.saucedemo.com/v1/");
        Thread.sleep(5000);  

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        Thread.sleep(5000);  

        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        Thread.sleep(5000);  

        driver.findElement(By.id("login-button")).click();
        Thread.sleep(5000);  
    }

    @Test
    public void verifyFirstProductCard() throws Exception {
        // Login and verify redirect to inventory page
        userLogin();

        // Expected values
        String expectedName = "Sauce Labs Backpack";
        String expectedDesc = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";
        String expectedPrice = "$29.99";
        String expectedImageSrc = "/img/sauce-backpack-1200x1500.jpg";

        // Wait for the inventory container to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_container")));
        Thread.sleep(5000);  

        // Get the first product card
        WebElement productCard = driver.findElements(By.className("inventory_item")).get(0);
        Thread.sleep(5000);  

        // Verify product name
        String actualName = productCard.findElement(By.className("inventory_item_name")).getText();
        System.out.println("Product Name - Expected: " + expectedName);
        System.out.println("Product Name - Actual: " + actualName);
        assert expectedName.equals(actualName) : "Product name mismatch";
        Thread.sleep(5000);  

        // Verify product description
        String actualDesc = productCard.findElement(By.className("inventory_item_desc")).getText();
        System.out.println("Product Description - Expected: " + expectedDesc);
        System.out.println("Product Description - Actual: " + actualDesc);
        assert expectedDesc.equals(actualDesc) : "Product description mismatch";
        Thread.sleep(5000);  

        // Verify product price
        String actualPrice = productCard.findElement(By.className("inventory_item_price")).getText();
        System.out.println("Product Price - Expected: " + expectedPrice);
        System.out.println("Product Price - Actual: " + actualPrice);
        assert expectedPrice.equals(actualPrice) : "Price mismatch";
        Thread.sleep(5000);  

        // Verify product image
        WebElement imgElement = productCard.findElement(By.tagName("img"));
        String actualImageSrc = imgElement.getAttribute("src");
        System.out.println("Image Source - Expected to contain: " + expectedImageSrc);
        System.out.println("Image Source - Actual: " + actualImageSrc);
        assert actualImageSrc != null && actualImageSrc.contains(expectedImageSrc) : "Image source mismatch";
        Thread.sleep(5000);  

        // Take screenshot before clicking
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("product_card_before_click.png"));
        Thread.sleep(5000);  

        // Click on the product name
        productCard.findElement(By.className("inventory_item_name")).click();
        Thread.sleep(5000); 

        // Wait for and verify the back button is present on detail page
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_details_back_button")));
        Thread.sleep(5000);  

        // Take screenshot of detail page
        screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("product_detail_page.png"));
        Thread.sleep(5000);  

        // Click back button
        driver.findElement(By.className("inventory_details_back_button")).click();
        Thread.sleep(5000);  

        // Wait for inventory container to be present again
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_container")));
        Thread.sleep(5000);  

        System.out.println("Test Completed Successfully!");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
