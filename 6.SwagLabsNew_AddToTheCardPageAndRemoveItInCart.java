package StandardSelenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
//6.Add to the card page and remove it in cart
public class SwagLabsNew_AddToTheCardPageAndRemoveItInCart {
    private WebDriver driver;
    private static final String BASE_URL = "https://www.saucedemo.com/v1/";
    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testCartFunctionality() throws InterruptedException {
        try {
            // Login
            driver.get(BASE_URL);
            Thread.sleep(5000);  

            driver.findElement(By.id("user-name")).sendKeys(USERNAME);
            Thread.sleep(5000);  

            driver.findElement(By.id("password")).sendKeys(PASSWORD);
            Thread.sleep(5000);  

            driver.findElement(By.id("login-button")).click();
            Thread.sleep(5000);  

            // Verify redirect to inventory page
            String currentUrl = driver.getCurrentUrl();
            String expectedInventoryUrl = BASE_URL + "inventory.html";
            Assert.assertEquals(currentUrl, expectedInventoryUrl, "Failed to redirect to inventory page");

            // Add first product to cart
            WebElement addToCartButton = driver.findElement(By.cssSelector(".inventory_item:first-child .btn_primary"));
            addToCartButton.click();
            Thread.sleep(5000);  

            // Click cart icon
            driver.findElement(By.cssSelector(".shopping_cart_link")).click();
            Thread.sleep(5000);  

            // Verify redirect to cart page
            currentUrl = driver.getCurrentUrl();
            String expectedCartUrl = BASE_URL + "cart.html";
            Assert.assertEquals(currentUrl, expectedCartUrl, "Failed to redirect to cart page");

            // Get product details
            WebElement cartItem = driver.findElement(By.cssSelector(".cart_item"));
            String actualProductName = cartItem.findElement(By.cssSelector(".inventory_item_name")).getText();
            String actualProductDesc = cartItem.findElement(By.cssSelector(".inventory_item_desc")).getText();
            String actualProductPrice = cartItem.findElement(By.cssSelector(".inventory_item_price")).getText();
            Thread.sleep(5000);  

            // Expected values
            String expectedProductName = "Sauce Labs Backpack";
            String expectedProductDesc = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";
            String expectedProductPrice = "$29.99";

            // Verify product details
            Assert.assertEquals(actualProductName, expectedProductName, "Product name mismatch");
            Assert.assertEquals(actualProductDesc, expectedProductDesc, "Product description mismatch");
            Assert.assertEquals(actualProductPrice, expectedProductPrice, "Product price mismatch");

            // Verify dollar sign in price
            Assert.assertTrue(actualProductPrice.startsWith("$"), "Price does not contain dollar sign");
            Thread.sleep(5000);  

            // Click Remove button
            driver.findElement(By.cssSelector(".btn_secondary")).click();
            Thread.sleep(5000);  

            // Verify item is removed
            Assert.assertTrue(driver.findElements(By.cssSelector(".cart_item")).isEmpty(),
                    "Product was not removed from cart");

        } catch (Exception e) {
            // Take screenshot on failure
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("test-failure-screenshot.png"));
            } catch (Exception ex) {
                System.out.println("Failed to save screenshot: " + ex.getMessage());
            }
            throw e;
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
