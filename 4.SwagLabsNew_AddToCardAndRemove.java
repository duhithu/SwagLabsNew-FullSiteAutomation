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
//4.Add to cart and remove
public class SwagLabsNew_AddToCardAndRemove {
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
    public void verifyCartButtonStates() throws Exception {
        // Login and verify redirect
        userLogin();

        // Expected Results
        String expectedUrl = "https://www.saucedemo.com/v1/inventory.html";
        String expectedAddButtonText = "ADD TO CART";
        String expectedRemoveButtonText = "REMOVE";

        // Verify redirect URL
        String actualUrl = driver.getCurrentUrl();
        System.out.println("URL Verification:");
        System.out.println("Expected URL: " + expectedUrl);
        System.out.println("Actual URL: " + actualUrl);
        assert actualUrl.equals(expectedUrl) : "URL mismatch after login";
        Thread.sleep(5000);

        // Get the first product card
        WebElement productCard = driver.findElements(By.className("inventory_item")).get(0);

        // Verify initial Add to Cart button
        WebElement cartButton = productCard.findElement(By.className("btn_primary"));
        String actualAddButtonText = cartButton.getText();
        System.out.println("\nAdd Button Verification:");
        System.out.println("Expected Text: " + expectedAddButtonText);
        System.out.println("Actual Text: " + actualAddButtonText);
        assert actualAddButtonText.equals(expectedAddButtonText) : "Add button text mismatch";

        // Take screenshot before clicking Add to Cart
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("before_add_to_cart.png"));
        Thread.sleep(5000);

        // Click Add to Cart button
        cartButton.click();
        Thread.sleep(5000);

        // Verify button changed to Remove
        WebElement removeButton = productCard.findElement(By.className("btn_secondary"));
        String actualRemoveButtonText = removeButton.getText();
        System.out.println("\nRemove Button Verification:");
        System.out.println("Expected Text: " + expectedRemoveButtonText);
        System.out.println("Actual Text: " + actualRemoveButtonText);
        assert actualRemoveButtonText.equals(expectedRemoveButtonText) : "Remove button text mismatch";

        // Take screenshot after clicking Add to Cart
        screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("after_add_to_cart.png"));
        Thread.sleep(5000);

        // Click Remove button
        removeButton.click();
        Thread.sleep(5000);

        // Verify button changed back to Add to Cart
        cartButton = productCard.findElement(By.className("btn_primary"));
        String finalButtonText = cartButton.getText();
        System.out.println("\nFinal Button State Verification:");
        System.out.println("Expected Text: " + expectedAddButtonText);
        System.out.println("Actual Text: " + finalButtonText);
        assert finalButtonText.equals(expectedAddButtonText) : "Button did not return to Add to Cart state";

        // Take final screenshot
        screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("after_remove.png"));

        System.out.println("Test Results Summary:");
        System.out.println("1. URL Verification: PASSED");
        System.out.println("2. Initial Add to Cart Button: PASSED");
        System.out.println("3. Button Changed to Remove: PASSED");
        System.out.println("4. Button Returned to Add to Cart: PASSED");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
