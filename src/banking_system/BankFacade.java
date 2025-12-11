package banking_system;

import java.util.HashMap;
import java.util.Map;
import accounts.Account;
import accounts.CheckingAccount;
import accounts.SavingsAccount;
import interest.SimpleInterestStrategy;
import notifications.EmailNotifier;
import notifications.SMSNotifier;

// --- Imports لسلسلة موافقة المعاملات ---
import transactions.approval_chain.ApprovalHandler;
import transactions.approval_chain.AutoApprovalHandler;
import transactions.approval_chain.ComplianceHandler;
import transactions.approval_chain.ManagerApprovalHandler;
import transactions.approval_chain.Transaction;

// --- Imports لسلسلة موافقة القروض ---
import loan_processing.LoanApplication;
import loan_processing.LoanApprovalHandler;
import loan_processing.JuniorLoanOfficer;
import loan_processing.SeniorLoanOfficer;
import loan_processing.CreditCommittee;

/**
 * BankFacade Class
 * -----------------
 * (Updated to handle both Transaction and Loan Approval Chains)
 */
public class BankFacade {

    private Map<String, Account> accountsDatabase = new HashMap<>();
    
    // السلاسل التي ستقوم بالمعالجة
    private ApprovalHandler transactionApprovalChain; // <-- سلسلة موافقات المعاملات
    private LoanApprovalHandler loanApprovalChain;      // <-- سلسلة موافقات القروض

    /**
     * Constructor: يتم بناء كل سلاسل الموافقة عند إنشاء الواجهة.
     */
    public BankFacade() {
        buildTransactionApprovalChain(); // بناء سلسلة المعاملات
        buildLoanApprovalChain();        // بناء سلسلة القروض
    }

    /**
     * Helper method لبناء سلسلة الموافقة على المعاملات.
     */
    private void buildTransactionApprovalChain() {
        System.out.println("Building the transaction approval chain...");
        this.transactionApprovalChain = new AutoApprovalHandler();
        ApprovalHandler manager = new ManagerApprovalHandler();
        ApprovalHandler compliance = new ComplianceHandler();
        this.transactionApprovalChain.setNext(manager);
        manager.setNext(compliance);
    }
    
    /**
     * Helper method لبناء سلسلة الموافقة على القروض.
     */
    private void buildLoanApprovalChain() {
        System.out.println("Building the loan approval chain...");
        this.loanApprovalChain = new JuniorLoanOfficer();
        LoanApprovalHandler senior = new SeniorLoanOfficer();
        LoanApprovalHandler committee = new CreditCommittee();
        this.loanApprovalChain.setNext(senior);
        senior.setNext(committee);
    }

    //
    // --- الميثودات التي تم تعديلها لتستخدم سلسلة الموافقات ---
    //
    
    public void deposit(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account == null) {
            System.out.println("❌ Account not found!");
            return;
        }

        System.out.println("\n-> Initiating deposit of " + amount + " to " + accountNumber);
        Transaction transaction = new Transaction(amount, "CASH_DEPOSIT", accountNumber);
        this.transactionApprovalChain.process(transaction);

        if (transaction.getStatus().contains("APPROVED")) {
            System.out.println("==> Approval GRANTED. Processing deposit.");
            account.deposit(amount);
        } else {
            System.out.println("==> Approval DENIED. Deposit cancelled.");
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account == null) {
            System.out.println("❌ Account not found!");
            return;
        }

        System.out.println("\n-> Initiating withdrawal of " + amount + " from " + accountNumber);
        Transaction transaction = new Transaction(amount, accountNumber, "CASH_WITHDRAWAL");
        this.transactionApprovalChain.process(transaction);

        if (transaction.getStatus().contains("APPROVED")) {
            System.out.println("==> Approval GRANTED. Processing withdrawal.");
            account.withdraw(amount);
        } else {
            System.out.println("==> Approval DENIED. Withdrawal cancelled.");
        }
    }

    public void transfer(String fromAccountNum, String toAccountNum, double amount) {
        Account fromAccount = accountsDatabase.get(fromAccountNum);
        Account toAccount = accountsDatabase.get(toAccountNum);
        if (fromAccount == null || toAccount == null) {
            System.out.println("❌ Transfer failed. One or both accounts not found.");
            return;
        }

        System.out.println("\n-> Initiating transfer of " + amount + " from " + fromAccountNum + " to " + toAccountNum);
        Transaction transaction = new Transaction(amount, fromAccountNum, toAccountNum);
        this.transactionApprovalChain.process(transaction);
        
        if (transaction.getStatus().contains("APPROVED")) {
            System.out.println("==> Approval GRANTED. Processing transfer.");
            // ملاحظة: من الأفضل أن تكون عملية السحب والإيداع التالية مضمونة
            // لكن بما أننا حصلنا على الموافقة، سنفترض أنها ستنجح.
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            System.out.println("🔄 Transfer successful!");
        } else {
            System.out.println("==> Approval DENIED. Transfer cancelled.");
        }
    }

    //
    // --- باقي الميثودات تبقى كما هي دون تغيير ---
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
                newAccount = new CheckingAccount(accountNumber, owner, initialBalance, 0);
                break;
        }
        newAccount.addObserver(new EmailNotifier(owner + "@example.com"));
        newAccount.addObserver(new SMSNotifier("0955555555"));
        accountsDatabase.put(accountNumber, newAccount);
        System.out.println("✅ Account created [" + type + "] successfully for: " + owner);
    }
    
    public void printAccountHistory(String accountNumber) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
             System.out.println("History for " + accountNumber + " printed.");
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    public void requestLoan(String applicantName, double amount, int creditScore, double monthlyIncome) {
        System.out.println("\n\n--- New Loan Request from " + applicantName + " for $" + amount + " ---");
        LoanApplication application = new LoanApplication(applicantName, amount, creditScore, monthlyIncome);
        this.loanApprovalChain.processRequest(application);
        System.out.println("==> FINAL DECISION: The loan for " + applicantName + " has been " + application.getStatus());
    }
}
