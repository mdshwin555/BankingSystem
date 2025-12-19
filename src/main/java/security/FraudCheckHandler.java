package security;

import accounts.Account;

public class FraudCheckHandler extends TransactionHandler {
    @Override
    public boolean handle(Account account, double amount) {
        if (amount > 10000) {
            System.out.println("⚠️ [Security] Fraud Alert: Amount " + amount + " exceeds daily limit for Account: " + account.getAccountNumber());
            return false; // يرفض المعاملة ويوقف السلسلة
        }
        System.out.println("✅ [Security] Fraud check passed.");
        return passToNext(account, amount);
    }
}