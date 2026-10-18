package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            initialize();
        }
        return extent;
    }

    public static void initialize() {
        if (extent != null) {
            return;
        }

        try {
            Path reportsDir = Paths.get("reports");
            Files.createDirectories(reportsDir);

            String reportPath = reportsDir.resolve("extent-report.html").toString();
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setReportName("AutoTest Pro Execution Report");
            sparkReporter.config().setDocumentTitle("AutoTest Pro");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize ExtentReports", e);
        }
    }

    public static ExtentTest startTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getCurrentTest() {
        return test.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void endTest() {
        test.remove();
    }
}
