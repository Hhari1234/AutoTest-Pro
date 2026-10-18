package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {
    private ScreenshotUtils() {
    }

    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }

        try {
            Path screenshotsDir = Paths.get("screenshots");
            Files.createDirectories(screenshotsDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeTestName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName = safeTestName + "_" + timestamp + ".png";
            Path targetPath = screenshotsDir.resolve(fileName);

            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
            LoggerUtils.info("Screenshot captured: " + targetPath);
            return targetPath.toString();
        } catch (IOException e) {
            LoggerUtils.warn("Unable to capture screenshot for test: " + testName);
            LoggerUtils.error("Screenshot failure", e);
            return null;
        }
    }
}
