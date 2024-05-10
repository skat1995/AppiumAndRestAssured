package Utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.appium.java_client.AppiumDriver;

public class ExtentReportListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);

    private static ExtentReports extent;
    private ExtentTest test;
    
    private static AppiumDriver<WebElement> driver;

    // Setter method to set the driver object
    public static void setDriver(AppiumDriver<WebElement> driver) {
        ExtentReportListener.driver = driver;
    }

    public static ExtentReports getExtent() {
        return extent;
    }

    public void onStart(ITestContext context) {
        logger.info("Test suite execution started.");
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportFileName = "test-report/extent-report_" + timeStamp + ".html";

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(new File(reportFileName));
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Automation Test Report");
        htmlReporter.config().setReportName("Anderoid and RestAssured - Test Results");

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Environment", "QA");
    }

    public void onTestStart(ITestResult result) {
        logger.info("Executing test: " + result.getMethod().getDescription());
        test = extent.createTest(result.getName(), result.getMethod().getDescription());
    }

    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: " + result.getMethod().getDescription());
        test.log(Status.PASS, "Test Passed");
        
//        try {
//	        String screenshotPath = Utilities.takeScreenshot(driver, result.getMethod().getDescription(), test);
//	    } catch (Exception e) {
//	        logger.error("Exception occurred: " + e.getMessage());
//	        Assert.fail("Test failed: " + e.getMessage());
//	    }
    }

    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: " + result.getMethod().getDescription());
        test.log(Status.FAIL, "Test Failed");
    }

    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: " + result.getMethod().getDescription());
        test.log(Status.SKIP, "Test Skipped");
    }

    public void onFinish(ITestContext context) {
        logger.info("Test suite execution finished.");
        extent.flush();
    }

    private String captureScreenshot(Object driver) {
        return null;
    }
}