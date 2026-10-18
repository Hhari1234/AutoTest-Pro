package listeners;

import com.aventstack.extentreports.ExtentTest;
import config.ConfigReader;
import driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.CurrencyUtils;
import utils.ExtentReportManager;
import utils.LoggerUtils;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initialize();
        LoggerUtils.info("Test suite started: " + context.getSuite().getName());
        LoggerUtils.info("Reporting currency: " + ConfigReader.getCurrency() + " | Currency symbol: "
                + ConfigReader.getCurrencySymbol() + " | Exchange rate: "
                + CurrencyUtils.getRateDescription());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.startTest(result.getMethod().getMethodName(), "QA Automation test");
        LoggerUtils.info("Test started: " + result.getMethod().getMethodName());
        if (ExtentReportManager.getCurrentTest() != null) {
            ExtentReportManager.getCurrentTest().info("Configured reporting currency: " + ConfigReader.getCurrency()
                    + " | estimated conversion rate: " + CurrencyUtils.getRateDescription());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getCurrentTest();
        if (test != null) {
            test.pass("Test passed");
        }
        LoggerUtils.info("Test passed: " + result.getMethod().getMethodName());
        ExtentReportManager.endTest();
        ExtentReportManager.flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getCurrentTest();
        if (test != null) {
            test.fail(result.getThrowable());
        }

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
            if (screenshotPath != null && test != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        }

        LoggerUtils.error("Test failed: " + result.getMethod().getMethodName(), result.getThrowable());
        ExtentReportManager.endTest();
        ExtentReportManager.flush();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getCurrentTest();
        if (test != null) {
            test.skip(result.getThrowable());
        }
        LoggerUtils.warn("Test skipped: " + result.getMethod().getMethodName());
        ExtentReportManager.endTest();
        ExtentReportManager.flush();
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerUtils.info("Test suite finished: " + context.getSuite().getName());
        ExtentReportManager.flush();
    }
}
