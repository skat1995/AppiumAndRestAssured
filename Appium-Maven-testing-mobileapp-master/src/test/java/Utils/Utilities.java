package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.AppiumDriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import io.appium.java_client.MobileElement;

public class Utilities {

	public static WebElement waitForElementPresence(AppiumDriver<WebElement> driver, WebElement element,
			int waitInSeconds) {
		waitIgnoringStale(driver, waitInSeconds).until(ExpectedConditions.visibilityOf(element));
		return element;
	}

	public static FluentWait<WebDriver> waitIgnoringStale(AppiumDriver<WebElement> driver, int waitInSeconds) {
		return new WebDriverWait(driver, waitInSeconds)
				.ignoring(org.openqa.selenium.StaleElementReferenceException.class)
				.pollingEvery(java.time.Duration.ofMillis(500));
	}

	public static void changeImplicitWaits(AppiumDriver<MobileElement> driver, int waitInSeconds) {
		driver.manage().timeouts().implicitlyWait(waitInSeconds, TimeUnit.SECONDS);
	}

	public static void scrollIntoView(AppiumDriver<MobileElement> driver, MobileElement element) {
		element.click(); // To bring the element into view
	}

	public static void scrollUpViewElement(AppiumDriver<MobileElement> driver) {
		driver.executeScript("mobile:scroll", "{\"direction\": \"up\", \"element\": \"\", \"percent\": 0.2}");
	}

	public static void scrollDownViewElement(AppiumDriver<MobileElement> driver) {
		driver.executeScript("mobile:scroll", "{\"direction\": \"down\", \"element\": \"\", \"percent\": 0.2}");
	}

	public static String takeScreenshot(AppiumDriver<WebElement> driver, String testName, ExtentTest test) {
        // Get current timestamp
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = currentTime.format(formatter);

        // Create directory path for screenshots
        String screenshotsDirectory = "screenshots/";
        File directory = new File(screenshotsDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn't exist
        }

        // Define screenshot file name with path
        String screenshotFileName = testName + ".png";
        String screenshotPath = screenshotsDirectory + screenshotFileName;

        // Capture screenshot and save to the defined path
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.move(screenshotFile.toPath(), Paths.get(screenshotPath));

            // Attach screenshot to Extent report
            test.info("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }

        return screenshotPath;
    }

	// Add more methods as needed for your Android automation project
}
