package reports;

import core.DatabaseManager;
import accounts.AccountComponent;
import java.util.Map;

public class AccountSummaryReport extends ReportTemplate {

    @Override
    protected void printBody() {
        System.out.println("📋 ACCOUNT SUMMARY REPORT");
        System.out.println(String.format("%-12s | %-15s | %-10s", "Account #", "Owner", "Balance"));
        System.out.println("------------------------------------------------");

        Map<String, AccountComponent> accounts = DatabaseManager.getInstance().getAllAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("   No accounts found in the system.");
        } else {
            for (AccountComponent acc : accounts.values()) {
                System.out.println(String.format("%-12s | %-15s | %-10.2f", 
                    acc.getAccountNumber(), 
                    // نقوم بعمل Casting للحصول على اسم المالك من كلاس Account
                    ((accounts.Account)acc).getOwnerName(), 
                    acc.getBalance()));
            }
        }
    }
}