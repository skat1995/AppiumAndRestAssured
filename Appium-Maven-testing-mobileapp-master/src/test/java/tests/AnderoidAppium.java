package tests;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import PageObject.HomePageNavigation;
import Utils.ExtentReportListener;
import Utils.Utilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

@Listeners(ExtentReportListener.class)

public class AnderoidAppium {
	AppiumDriver<WebElement> driver; // Change driver type to WebElement

	private static final Logger logger = LogManager.getLogger(AnderoidAppium.class);
	ExtentTest test;

	@BeforeTest
	public void setup() {
		try {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14");
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Galaxy F14 5G");
			caps.setCapability(MobileCapabilityType.UDID, "RZCW30JBJCR");
			caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
			caps.setCapability("appPackage", "com.instagram.android");
			caps.setCapability("appActivity", "com.instagram.mainactivity.InstagramMainActivity");
			URL url = new URL("http://0.0.0.0:4723/wd/hub");
			driver = new AppiumDriver<WebElement>(url, caps); // Change driver initialization
			 // Initialize ExtentTest object
			ExtentReportListener.setDriver(driver);
		} catch (Exception exp) {
			System.out.println(exp);
			logger.error("Exception occurred: " + exp.getMessage());
	        Assert.fail("Test setup failed: " + exp.getMessage());
		}
	}

	@Test()
	public void myAnderoidInstagramLaunchTest() throws InterruptedException {

		Thread.sleep(2000);
		System.out.println("Lets Navigate To Ur HomeFeedPage....");
		System.out.println("App is getting Launched Redurecting to ur profile");
		HomePageNavigation hp = new HomePageNavigation(driver);
		WebElement skatid = hp.getCredentialLabel(); // Change type to WebElement
		Utilities.waitForElementPresence(driver, skatid, 5);
		hp.clickIdSelect();
		hp.clickIdSelect();
		Thread.sleep(10000);		
		WebElement saveButton = hp.getSaveButton(); // Change type to WebElement
		Utilities.waitForElementPresence(driver, saveButton, 5);
		hp.clickSaveButton();
		Thread.sleep(10000);
		WebElement allowButton = hp.getAllowButton(); // Change type to WebElement
		Utilities.waitForElementPresence(driver, allowButton, 10);
		hp.clickAllowButton();
	}

	@AfterTest
	public void teardown() {
		driver.quit();
	}
}