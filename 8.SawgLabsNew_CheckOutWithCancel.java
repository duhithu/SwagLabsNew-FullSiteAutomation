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
// 8. Check out with clicking cancel, add the delivery details, check the total price formula
public class SawgLabsNew_CheckOutWithCancel {
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
    public void testCheckoutCancelFlow() throws InterruptedException {
        try {
            // Login
            driver.get(BASE_URL);
            Thread.sleep(5000);

            driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys(USERNAME);
            Thread.sleep(3000);

            driver.findElement(By.xpath("//input[@id='password']")).sendKeys(PASSWORD);
            Thread.sleep(3000);

            driver.findElement(By.xpath("//input[@id='login-button']")).click();
            Thread.sleep(5000);

            // Verify redirect to inventory page
            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, BASE_URL + "inventory.html",
                    "Failed to redirect to inventory page");

            // Add first product to cart
            driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]")).click();
            Thread.sleep(3000);

            // Go to cart
            driver.findElement(By.xpath("//div[@id='shopping_cart_container']/a")).click();
            Thread.sleep(3000);

            // Verify cart page
            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, BASE_URL + "cart.html",
                    "Failed to navigate to cart page");

            // Click Checkout
            driver.findElement(By.xpath("//a[contains(text(),'CHECKOUT')]")).click();
            Thread.sleep(3000);

            // Verify checkout step one page
            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, BASE_URL + "checkout-step-one.html",
                    "Failed to navigate to checkout step one");

            // Fill checkout information
            driver.findElement(By.xpath("//input[@id='first-name']")).sendKeys("Duhithu");
            Thread.sleep(2000);

            driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys("Sakuni");
            Thread.sleep(2000);

            driver.findElement(By.xpath("//input[@id='postal-code']")).sendKeys("001");
            Thread.sleep(2000);

            // Click Continue
            driver.findElement(By.xpath("//input[@value='CONTINUE']")).click();
            Thread.sleep(5000);

            // Verify checkout-step-two page
            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, BASE_URL + "checkout-step-two.html",
                    "Failed to navigate to checkout step two");

            // Get and verify product details
            String productName = driver.findElement(By.xpath("//div[@class='inventory_item_name']")).getText();
            String productDesc = driver.findElement(By.xpath("//div[@class='inventory_item_desc']")).getText();
            String productPrice = driver.findElement(By.xpath("//div[@class='inventory_item_price']")).getText();

            // Verify product details
            Assert.assertEquals(productName, "Sauce Labs Backpack", "Product name mismatch");
            Assert.assertEquals(productDesc,
                    "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.",
                    "Product description mismatch");
            Assert.assertTrue(productPrice.startsWith("$"), "Product price doesn't contain dollar sign");

            // Get price details
            String itemTotalStr = driver.findElement(By.xpath("//div[@class='summary_subtotal_label']"))
                    .getText().replaceAll("[^0-9.]", "");
            String taxStr = driver.findElement(By.xpath("//div[@class='summary_tax_label']"))
                    .getText().replaceAll("[^0-9.]", "");
            String totalStr = driver.findElement(By.xpath("//div[@class='summary_total_label']"))
                    .getText().replaceAll("[^0-9.]", "");

            // Convert price strings to doubles
            double itemTotal = Double.parseDouble(itemTotalStr);
            double tax = Double.parseDouble(taxStr);
            double total = Double.parseDouble(totalStr);

            // Verify total calculation
            double expectedTotal = itemTotal + tax;
            Assert.assertEquals(total, expectedTotal, 0.01,
                    "Total price calculation is incorrect");

            // Click Cancel
            driver.findElement(By.xpath("//a[contains(text(),'CANCEL')]")).click();
            Thread.sleep(3000);

            // Verify navigation back to inventory page
            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, BASE_URL + "inventory.html",
                    "Failed to navigate back to inventory page after cancel");

        } catch (Exception e) {
            // Take screenshot on failure
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("checkout-cancel-test-failure.png"));
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


