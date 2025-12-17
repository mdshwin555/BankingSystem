package banking_system;

import java.util.HashMap;
import java.util.Map;

import accounts.*;
import interest.SimpleInterestStrategy;
import notifications.EmailNotifier;
import notifications.SMSNotifier;
// 1. استيراد حزمة الأمان الجديدة
import security.*;
import transactions.DepositCommand;
import transactions.TransactionCommand;
import transactions.TransactionScheduler;

public class BankFacade {
    private Map<String, Account> accountsDatabase = new HashMap<>();

    // 2. تعريف كائن يمثل بداية "سلسلة التحقق"
    private TransactionHandler securityChain;
    private TransactionScheduler scheduler = new TransactionScheduler();

    public BankFacade() {
        // 3. بناء السلسلة عند تشغيل البنك (Constructor)
        TransactionHandler fraudCheck = new FraudCheckHandler();
        TransactionHandler managerApproval = new ManagerApprovalHandler();

        // ربط السلسلة: فحص الاحتيال -> ثم موافقة المدير
        fraudCheck.setNextHandler(managerApproval);
        this.securityChain = fraudCheck;
    }

    // ... (باقي الدوال مثل createAccount تبقى كما هي) ...

    /**
     * تعديل دالة السحب لتستخدم نمط Chain of Responsibility
     */
    public void withdraw(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {

            System.out.println("\n🔍 [System] Security Check for Account: " + accountNumber);

            // 4. التعديل الجوهري: نمرر الطلب للسلسلة أولاً
            // إذا أعادت السلسلة true يعني أن كل الفحوصات (احتيال، مدير) تمت بنجاح
            if (securityChain.handle(account, amount)) {
                account.withdraw(amount);
            } else {
                // إذا فشل أي فحص في السلسلة
                System.out.println("⛔ [System] Transaction BLOCKED by security policy.");
            }

        } else {
            System.out.println("❌ Account not found!");
        }
    }

    /**
     * Creates a new account based on the specified type.
     * Implements a simple **Factory Logic** to decide which subclass to instantiate.
     * Automatically registers observers (Email & SMS) for the new account.
     *
     * @param type           The type of account ("savings", "checking", or "default").
     * @param accountNumber  Unique identifier for the account.
     * @param owner          Name of the account holder.
     * @param initialBalance Starting balance.
     */
    public void createAccount(String type, String accountNumber, String owner, double initialBalance) {
        Account newAccount;

        // Factory logic: Determine account type and instantiate the correct subclass.
        switch (type.toLowerCase()) {
            case "savings":
                // افتراض استراتيجية فائدة بسيطة لعملية الإنشاء
                newAccount = new SavingsAccount(accountNumber, owner, initialBalance, 2.5, new SimpleInterestStrategy());
                break;
            case "checking":
                // افتراض حد سحب على المكشوف 500.0 لعملية الإنشاء
                newAccount = new CheckingAccount(accountNumber, owner, initialBalance, 500.0);
                break;
            case "loan": // حالة جديدة للقروض
                // يتم اعتبار الرصيد الأولي هو قيمة القرض
                newAccount = new LoanAccount(accountNumber, owner, initialBalance);
                break;
            case "investment": // حالة جديدة للاستثمار
                // افتراض مستوى مخاطر "Medium" لعملية الإنشاء
                newAccount = new InvestmentAccount(accountNumber, owner, initialBalance, "Medium");
                break;
            case "default":
            default:
                newAccount = new Account(accountNumber, owner, initialBalance);
                break;
        }

        accountsDatabase.put(accountNumber, newAccount);
        System.out.println("✅ Account created: " + type.toUpperCase() + " (" + accountNumber + ")");

        // Register default observers (Observer Pattern)
        newAccount.addObserver(new EmailNotifier(owner.toLowerCase().replace(" ", "") + "@bank.com"));
        newAccount.addObserver(new SMSNotifier("055xxxxxxx"));
    }

    /**
     * Performs a deposit operation on a specific account.
     * @param accountNumber Target account number.
     * @param amount        Amount to deposit.
     */
    public void deposit(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            account.deposit(amount);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    /**
     * Performs a withdrawal operation on a specific account.
     * @param accountNumber Target account number.
     * @param amount        Amount to withdraw.
     */
//    public void withdraw(String accountNumber, double amount) {
//        Account account = accountsDatabase.get(accountNumber);
//        if (account != null) {
//            account.withdraw(amount);
//        } else {
//            System.out.println("❌ Account not found!");
//        }
//    }

    /**
     * Transfers funds between two accounts.
     * This operation is atomic: it ensures withdrawal succeeds before depositing.
     * * @param fromAccountNum Source account number.
     * @param toAccountNum   Destination account number.
     * @param amount         Amount to transfer.
     */
    public void transfer(String fromAccountNum, String toAccountNum, double amount) {
        Account fromAccount = accountsDatabase.get(fromAccountNum);
        Account toAccount = accountsDatabase.get(toAccountNum);

        if (fromAccount != null && toAccount != null) {
            // Check balance before transaction to verify success later
            double oldBalance = fromAccount.getBalance();
            
            // Attempt to withdraw from source
            fromAccount.withdraw(amount);

            // Verify if withdrawal was successful (balance decreased)
            if (fromAccount.getBalance() < oldBalance) {
                // Complete the transfer by depositing to destination
                toAccount.deposit(amount);
                System.out.println("🔄 Transfer successful from " + fromAccountNum + " to " + toAccountNum);
            } else {
                System.out.println("❌ Transfer failed. Insufficient funds or limit exceeded.");
            }
        } else {
            System.out.println("❌ Transfer failed. One or both accounts not found.");
        }
    }

    /**
     * Prints the full transaction history (Audit Log) for a specific account.
     * @param accountNumber The account to retrieve logs for.
     */
    public void printAccountHistory(String accountNumber) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            account.printTransactionHistory();
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    public Account getAccount(String accountNumber) {
        // بما أن AccountGroup لا يتم تخزينه في accountsDatabase، يجب أن تعيد الدالة حساباً فردياً
        return accountsDatabase.get(accountNumber);
    }

    // أضف هذه الدوال داخل فئة BankFacade

    /**
     * تحديث معلومات صاحب الحساب
     */
    public void updateAccountInfo(String accountNumber, String newName) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            account.setOwnerName(newName);
            System.out.println("📝 Account " + accountNumber + " updated. New owner: " + newName);
        } else {
            System.out.println("❌ Account not found for update.");
        }
    }

    /**
     * إغلاق الحساب مع منطق مخصص حسب النوع
     */
    public void closeAccount(String accountNumber) {
        Account account = accountsDatabase.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Error: Account " + accountNumber + " not found.");
            return;
        }

        System.out.println("\n--- 🛡️ Processing Closure for Account: " + accountNumber + " ---");

        // 1. منطق خاص بحساب القروض (تسوية الديون)
        if (account instanceof accounts.LoanAccount) {
            if (account.getBalance() > 0) {
                System.out.println("❌ Cannot close Loan Account: Outstanding debt of " + account.getBalance() + " must be settled first.");
                return;
            }
        }
        // 2. منطق خاص بالحسابات العادية (سحب الرصيد المتبقي)
        else {
            double remainingBalance = account.getBalance();
            if (remainingBalance > 0) {
                System.out.println("💰 Withdrawing remaining balance: " + remainingBalance + " before closure.");
                account.withdraw(remainingBalance);
            }
        }

        // 3. الإغلاق النهائي (إزالته من قاعدة البيانات)
        accountsDatabase.remove(accountNumber);
        System.out.println("🔒 Account " + accountNumber + " has been successfully closed.");


    }

    public void freezeAccount(String accountNumber) {
        Account acc = accountsDatabase.get(accountNumber);
        if (acc != null) acc.setState(new FrozenState());
    }

    public void activateAccount(String accountNumber) {
        Account acc = accountsDatabase.get(accountNumber);
        if (acc != null) acc.setState(new ActiveState());
    }

    public void scheduleDeposit(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            TransactionCommand depositCmd = new DepositCommand(account, amount);
            scheduler.scheduleTransaction(depositCmd);
        } else {
            System.out.println("❌ Account not found for scheduling.");
        }
    }

    // دالة لتنفيذ كل ما هو مجدول (مثل دفعات نهاية الشهر)
    public void executeAllScheduled() {
        scheduler.runScheduledTransactions();
    }
}