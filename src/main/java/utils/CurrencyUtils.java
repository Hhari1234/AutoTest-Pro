package utils;

import config.ConfigReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CurrencyUtils {
    private static final Pattern USD_PATTERN = Pattern.compile("\\$\\s*([0-9]+(?:\\.[0-9]+)?)");

    private CurrencyUtils() {
    }

    public static BigDecimal parseUsdPrice(String rawPriceText) {
        if (rawPriceText == null || rawPriceText.isBlank()) {
            throw new IllegalArgumentException("Price text cannot be blank.");
        }

        Matcher matcher = USD_PATTERN.matcher(rawPriceText);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }

        String cleaned = rawPriceText.replaceAll("[^0-9.]", "");
        if (cleaned.isBlank()) {
            throw new IllegalArgumentException("Unable to parse a USD price from: " + rawPriceText);
        }

        return new BigDecimal(cleaned);
    }

    public static BigDecimal convertToInr(String usdPriceText) {
        BigDecimal usdValue = parseUsdPrice(usdPriceText);
        return usdValue.multiply(ConfigReader.getUsdToInrRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public static String formatInr(BigDecimal amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        format.setCurrency(Currency.getInstance("INR"));
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(amount);
    }

    public static String formatInr(String usdPriceText) {
        return formatInr(convertToInr(usdPriceText));
    }

    public static String formatEstimatedInr(String usdPriceText) {
        return "Estimated conversion using rate " + getRateDescription() + ": " + formatInr(usdPriceText);
    }

    public static String getRateDescription() {
        return ConfigReader.getCurrencySymbol() + ConfigReader.getUsdToInrRate().setScale(2, RoundingMode.HALF_UP)
                .toPlainString() + " per USD";
    }
}
