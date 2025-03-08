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
//7. Check out with clicking final, fill the delivery form, check the total price formula
public class SawgLabsNew_CheckOutWithFinal {
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
    public void testCheckoutFlow() throws InterruptedException {
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
            Assert.assertEquals(driver.getCurrentUrl(), BASE_URL + "inventory.html");

            // Add first product to cart
            driver.findElement(By.xpath("(//button[contains(text(),'ADD TO CART')])[1]")).click();
            Thread.sleep(3000);

            // Go to cart
            driver.findElement(By.xpath("//div[@id='shopping_cart_container']/a")).click();
            Thread.sleep(3000);

            // Click Checkout
            driver.findElement(By.xpath("//a[contains(text(),'CHECKOUT')]")).click();
            Thread.sleep(3000);

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
            Assert.assertEquals(driver.getCurrentUrl(), BASE_URL + "checkout-step-two.html");

            // Get product details
            String productName = driver.findElement(By.xpath("//div[@class='inventory_item_name']")).getText();
            String productDesc = driver.findElement(By.xpath("//div[@class='inventory_item_desc']")).getText();
            String productPrice = driver.findElement(By.xpath("//div[@class='inventory_item_price']")).getText();

            // Verify product details
            Assert.assertEquals(productName, "Sauce Labs Backpack");
            Assert.assertEquals(productDesc, "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.");
            Assert.assertTrue(productPrice.startsWith("$"), "Product price doesn't contain dollar sign");

            // Get price details
            String itemTotalStr = driver.findElement(By.xpath("//div[@class='summary_subtotal_label']")).getText().replaceAll("[^0-9.]", "");
            String taxStr = driver.findElement(By.xpath("//div[@class='summary_tax_label']")).getText().replaceAll("[^0-9.]", "");
            String totalStr = driver.findElement(By.xpath("//div[@class='summary_total_label']")).getText().replaceAll("[^0-9.]", "");

            // Convert price strings to doubles
            double itemTotal = Double.parseDouble(itemTotalStr);
            double tax = Double.parseDouble(taxStr);
            double total = Double.parseDouble(totalStr);

            // Verify total calculation
            double expectedTotal = itemTotal + tax;
            Assert.assertEquals(total, expectedTotal, 0.01, "Total price calculation is incorrect");

            // Click Finish
            driver.findElement(By.xpath("//a[contains(text(),'FINISH')]")).click();
            Thread.sleep(3000);

            // Verify checkout-complete page
            Assert.assertEquals(driver.getCurrentUrl(), BASE_URL + "checkout-complete.html");

        } catch (Exception e) {
            // Take screenshot on failure
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("checkout-test-failure.png"));
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
