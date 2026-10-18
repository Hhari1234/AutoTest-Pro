package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CurrencyUtils;

import java.math.BigDecimal;

public class CurrencyUtilsTests {

    @Test(description = "USD prices should convert to INR using the configured rate")
    public void convertUsdPriceToEstimatedInr() {
        BigDecimal converted = CurrencyUtils.convertToInr("$29.99");
        Assert.assertEquals(converted.setScale(2, java.math.RoundingMode.HALF_UP), new BigDecimal("2549.15"),
                "The configured rate should convert USD to INR with precision to 2 decimal places.");
    }

    @Test(description = "Formatted INR output should include the configured symbol and estimated conversion label")
    public void formatInrCurrencyWithSymbolAndLabel() {
        String formatted = CurrencyUtils.formatEstimatedInr("$29.99");
        Assert.assertTrue(formatted.contains("₹"), "The INR format should use the Indian rupee symbol.");
        Assert.assertTrue(formatted.contains("Estimated conversion"), "The output should label the value as an estimate.");
        Assert.assertTrue(formatted.contains("2,549.15"), "The formatted INR output should preserve expected local rounding.");
    }
}
