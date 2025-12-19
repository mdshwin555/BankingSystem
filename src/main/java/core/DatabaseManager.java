package core;

import accounts.AccountComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection; // أضفنا هذا الاستيراد الصغير

/**
 * Singleton Pattern: DatabaseManager
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Map<String, AccountComponent> accounts = new HashMap<>();

    // 🆕 ميزة جديدة: عداد العمليات العالمي لمراقبة أداء البنك
    private int totalGlobalTransactions = 0;

    private DatabaseManager() { } 

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void saveAccount(AccountComponent account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public AccountComponent getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void removeAccount(String accountNumber) {
        accounts.remove(accountNumber);
    }

    public Map<String, AccountComponent> getAllAccounts() {
        return accounts;
    }

    // 🆕 الدوال الجديدة المطلوبة للوحة التحكم الإدارية (Dashboard)
    
    public void incrementTransactionCount() {
        totalGlobalTransactions++;
    }

    public int getTotalGlobalTransactions() {
        return totalGlobalTransactions;
    }

    public int getTotalAccountsCount() {
        return accounts.size();
    }

    // دالة مساعدة للحصول على الحسابات كقائمة (لسهولة حساب السيولة)
    public Collection<AccountComponent> getAllAccountsList() {
        return accounts.values();
    }
}