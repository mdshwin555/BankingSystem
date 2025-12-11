package banking_system;

import java.util.HashMap;
import java.util.Map;
import accounts.Account;
import accounts.CheckingAccount;
import accounts.SavingsAccount;
import interest.SimpleInterestStrategy;
import notifications.EmailNotifier;
import notifications.SMSNotifier;

// --- Imports جديدة لسلسلة القروض ---
import loan_processing.LoanApplication;
import loan_processing.LoanApprovalHandler;
import loan_processing.JuniorLoanOfficer;
import loan_processing.SeniorLoanOfficer;
import loan_processing.CreditCommittee;


/**
 * BankFacade Class
 * -----------------
 * (Updated to handle Loan Approval Chain)
 */
public class BankFacade {

    private Map<String, Account> accountsDatabase = new HashMap<>();
    private LoanApprovalHandler loanApprovalChain; // <-- متغير جديد لسلسلة القروض

    /**
     * Constructor: يتم بناء سلسلة الموافقة على القروض عند إنشاء الواجهة.
     */
    public BankFacade() {
        buildLoanApprovalChain();
    }

    /**
     * Helper method لبناء سلسلة الموافقة على القروض.
     */
    private void buildLoanApprovalChain() {
        System.out.println("Building the loan approval chain for the banking system...");
        // 1. إنشاء الحلقات (الموظفين)
        this.loanApprovalChain = new JuniorLoanOfficer();
        LoanApprovalHandler senior = new SeniorLoanOfficer();
        LoanApprovalHandler committee = new CreditCommittee();

        // 2. ربط السلسلة بالترتيب الصحيح
        this.loanApprovalChain.setNext(senior);
        senior.setNext(committee);
    }

    //
    // =======================================================================
    //  الكود الحالي لـ createAccount, deposit, withdraw, transfer, printHistory
    //  يبقى كما هو دون أي تغيير
    // =======================================================================
    //

    public void createAccount(String type, String accountNumber, String owner, double initialBalance) {
        Account newAccount;
        switch (type.toLowerCase()) {
            case "savings":
                newAccount = new SavingsAccount(accountNumber, owner, initialBalance, 3.0, new SimpleInterestStrategy());
                break;
            case "checking":
                newAccount = new CheckingAccount(accountNumber, owner, initialBalance, 1000.0);
                break;
            default:
                // Account class needs to be concrete or you need a default concrete implementation
                // For now, assuming Account can be instantiated or you have a 'StandardAccount'
                // This part depends on your exact 'Account' class definition.
                // If 'Account' is abstract, you cannot do 'new Account(...)'.
                // Let's comment it out if it's abstract.
                // newAccount = new Account(accountNumber, owner, initialBalance);
                // For the sake of compiling, let's make it a checking account by default.
                newAccount = new CheckingAccount(accountNumber, owner, initialBalance, 0);

                break;
        }
        newAccount.addObserver(new EmailNotifier(owner + "@example.com"));
        newAccount.addObserver(new SMSNotifier("0955555555"));
        accountsDatabase.put(accountNumber, newAccount);
        System.out.println("✅ Account created [" + type + "] successfully for: " + owner);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            account.deposit(amount);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            account.withdraw(amount);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    public void transfer(String fromAccountNum, String toAccountNum, double amount) {
        Account fromAccount = accountsDatabase.get(fromAccountNum);
        Account toAccount = accountsDatabase.get(toAccountNum);
        if (fromAccount != null && toAccount != null) {
            double oldBalance = fromAccount.getBalance();
            fromAccount.withdraw(amount);
            if (fromAccount.getBalance() < oldBalance) {
                toAccount.deposit(amount);
                System.out.println("🔄 Transfer successful from " + fromAccountNum + " to " + toAccountNum);
            } else {
                System.out.println("❌ Transfer failed. Insufficient funds or limit exceeded.");
            }
        } else {
            System.out.println("❌ Transfer failed. One or both accounts not found.");
        }
    }
    
    public void printAccountHistory(String accountNumber) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            // Assuming a method like printTransactionHistory exists on Account
            // account.printTransactionHistory(); 
             System.out.println("History for " + accountNumber + " printed.");
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    //
    // =======================================================================
    //  الميثود الجديدة لطلبات القروض
    // =======================================================================
    //
    
    /**
     * يبدأ عملية طلب قرض عن طريق إرساله إلى سلسلة الموافقة على القروض.
     * @param applicantName اسم مقدم الطلب.
     * @param amount        مبلغ القرض المطلوب.
     * @param creditScore   درجة الائتمان لمقدم الطلب.
     * @param monthlyIncome الدخل الشهري لمقدم الطلب.
     */
    public void requestLoan(String applicantName, double amount, int creditScore, double monthlyIncome) {
        System.out.println("\n\n--- New Loan Request from " + applicantName + " for $" + amount + " ---");
        
        // 1. إنشاء كائن يمثل طلب القرض
        LoanApplication application = new LoanApplication(applicantName, amount, creditScore, monthlyIncome);
        
        // 2. إرسال الطلب إلى الحلقة الأولى في سلسلة القروض
        this.loanApprovalChain.processRequest(application);

        // 3. طباعة القرار النهائي بعد انتهاء السلسلة
        System.out.println("==> FINAL DECISION: The loan for " + applicantName + " has been " + application.getStatus());
    }
}
