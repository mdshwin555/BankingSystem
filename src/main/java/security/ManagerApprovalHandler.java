package security;

import accounts.Account;

public class ManagerApprovalHandler extends TransactionHandler {
    @Override
    public boolean handle(Account account, double amount) {
        if (amount > 5000) {
            System.out.println("👨‍💼 [Security] Manager Approval Required for amount: " + amount);
            // في نظام حقيقي ننتظر مدخلاً، هنا سنقوم بالموافقة تلقائياً بعد إظهار التنبيه
        }
        return passToNext(account, amount);
    }
}