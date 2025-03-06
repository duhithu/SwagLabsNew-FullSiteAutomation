package StandardSelenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

//3.Open the Side Menu and Close It.
public class SwagLabsnew_OpenLeftSideMenuAndCloseIt {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    public void userLogin() {
        driver.get("https://www.saucedemo.com/v1/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    @Test(priority = 2)
    public void sideNavigation() throws Exception {
        // Login to the system
        userLogin();

        // Click the navigation button
        WebElement menuButton = driver.findElement(By.className("bm-burger-button"));
        menuButton.click();
        
        Thread.sleep(5000);

        // Verify menu is visible 
        WebElement menu = driver.findElement(By.className("bm-menu-wrap"));
        boolean isMenuDisplayed = menu.isDisplayed();
        assert isMenuDisplayed : "Side navigation menu should be visible but it's not";

        // Verify menu items are present
        WebElement menuItem = driver.findElement(By.className("bm-item-list"));
        assert menuItem.isDisplayed() : "Menu items should be visible";

        // Take screenshot of opened menu
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("menu_opened.png"));
        System.out.println("Test Case 002 - Side Navigation Open: PASSED");
    }

    @Test(priority = 3)
    public void sideNavigationClose() throws Exception {
        // Login to the system
        userLogin();

        // Open side navigation
        sideNavigation();

        // Click the navigation close button 
        WebElement closeButton = driver.findElement(By.className("bm-cross-button"));
        closeButton.click();
        
        Thread.sleep(5000);

        // Verify menu is hidden
        boolean isMenuHidden = false;
        try {
            WebElement menu = driver.findElement(By.className("bm-menu-wrap"));
            isMenuHidden = !menu.isDisplayed();
        } catch (StaleElementReferenceException e) {
            isMenuHidden = true; 
        }

        assert isMenuHidden : "Side navigation menu should be hidden but it's still visible";

        // Take screenshot of closed menu
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("menu_closed.png"));
        System.out.println("Test Case 003 - Side Navigation Close: PASSED");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
