package accounts;

import interest.SimpleInterestStrategy;

/**
 * Factory Pattern: AccountFactory
 * مسؤول عن إنشاء الكائنات (Accounts) بناءً على النوع الممرر.
 * هذا يسهل إضافة أنواع حسابات جديدة مستقبلاً دون تعديل الكود الأساسي.
 */
public class AccountFactory {

    public static Account createAccount(String type, String accountNumber, String owner, double initialBalance) {
        switch (type.toLowerCase()) {
            case "savings":
                // حساب توفير مع استراتيجية فائدة بسيطة (يمكن تطويرها لاحقاً)
                return new SavingsAccount(accountNumber, owner, initialBalance, 2.5, new SimpleInterestStrategy());
                
            case "checking":
                // حساب جاري مع حد سحب على المكشوف 500.0
                return new CheckingAccount(accountNumber, owner, initialBalance, 500.0);
                
            case "loan":
                // حساب قرض (الرصيد يمثل مبلغ القرض)
                return new LoanAccount(accountNumber, owner, initialBalance);
                
            case "investment":
                // حساب استثمار مع مستوى مخاطر متوسط
                return new InvestmentAccount(accountNumber, owner, initialBalance, "Medium");
                
            default:
                // الحساب العادي في حال عدم تحديد نوع معروف
                return new Account(accountNumber, owner, initialBalance);
        }
    }
}