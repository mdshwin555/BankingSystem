package transactions;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Pattern: CurrencyConverter
 * يدير أسعار الصرف ويقوم بعمليات التحويل بين العملات.
 */
public class CurrencyConverter {
    private static CurrencyConverter instance;
    private Map<String, Double> exchangeRates = new HashMap<>();

    private CurrencyConverter() {
        // أسعار صرف افتراضية (مقابل العملة الأساسية للبنك)
        exchangeRates.put("USD", 1.0);    // العملة المرجعية
        exchangeRates.put("EUR", 0.92);   // مثال: اليورو
        exchangeRates.put("SYP", 13000.0); // مثال: عملة محلية
        exchangeRates.put("SAR", 3.75);   // مثال: ريال سعودي
    }

    public static synchronized CurrencyConverter getInstance() {
        if (instance == null) {
            instance = new CurrencyConverter();
        }
        return instance;
    }

    /**
     * تحويل مبلغ من عملة معينة إلى العملة الأساسية للحساب.
     */
    public double convertToLocal(double amount, String fromCurrency) {
        if (!exchangeRates.containsKey(fromCurrency)) {
            System.out.println("⚠️ Currency " + fromCurrency + " not supported. Using default.");
            return amount;
        }
        double rate = exchangeRates.get(fromCurrency);
        double convertedAmount = amount / rate; // منطق تحويل مبسط
        return convertedAmount;
    }

    public void updateRate(String currency, double newRate) {
        exchangeRates.put(currency, newRate);
    }
}