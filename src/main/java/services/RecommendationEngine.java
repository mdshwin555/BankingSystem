package services;

import accounts.*;

public class RecommendationEngine {
    
    public static String getRecommendation(Account account) {
        double balance = account.getBalance();
        String type = account.getClass().getSimpleName();

        // 1. إذا كان الرصيد مرتفعاً جداً والحساب ليس توفيراً
        if (balance > 10000 && !type.equals("SavingsAccount")) {
            return "💡 [Advice] Your balance is high. Consider opening a **Savings Account** to earn interest!";
        }
        
        // 2. إذا كان الرصيد مرتفعاً والحساب توفير (نقترح استثمار)
        if (balance > 20000 && type.equals("SavingsAccount")) {
            return "🚀 [Opportunity] You have great savings! Talk to us about our **Investment Plans** for higher returns.";
        }

        // 3. إذا كان الرصيد منخفضاً في حساب جاري
        if (balance < 500 && type.equals("CheckingAccount")) {
            return "⚠️ [Alert] Your balance is getting low. Enable **Overdraft Protection** to avoid declined payments.";
        }

        // 4. توصية عامة
        return "✅ [Status] Your financial health looks good. Keep it up!";
    }
}