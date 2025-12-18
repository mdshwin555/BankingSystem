package banking_system;

import core.DatabaseManager;
import accounts.*;
import notifications.EmailNotifier;
import notifications.SMSNotifier;
import security.*;
import transactions.DepositCommand;
import transactions.TransactionScheduler;
import transactions.CurrencyConverter;
import reports.*;
import support.*; 
import services.RecommendationEngine; // استيراد المحرك الجديد

public class BankFacade {
    private DatabaseManager db = DatabaseManager.getInstance();
    private SessionManager session = SessionManager.getInstance(); 
    private TransactionHandler securityChain;
    private TransactionScheduler scheduler = new TransactionScheduler();

    public BankFacade() {
        TransactionHandler fraudCheck = new FraudCheckHandler();
        TransactionHandler managerApproval = new ManagerApprovalHandler();
        fraudCheck.setNextHandler(managerApproval);
        this.securityChain = fraudCheck;
    }

    private boolean hasAccess(UserRole requiredRole) {
        if (!session.isLoggedIn()) {
            System.out.println("⛔️ ACCESS DENIED: Authentication required. Please login.");
            return false;
        }
        UserRole currentRole = session.getCurrentUser().getRole();
        if (currentRole == UserRole.ADMIN) return true;
        if (currentRole == requiredRole || (currentRole == UserRole.MANAGER && requiredRole == UserRole.CUSTOMER)) {
            return true;
        }
        System.out.println("🚫 Permission Denied: Role [" + currentRole + "] cannot perform this action.");
        return false;
    }

    // ===========================================================
    // 📊 ميزة مراقبة النظام (Monitoring & Admin Dashboard)
    // ===========================================================
    public void showAdminDashboard() {
        if (!hasAccess(UserRole.ADMIN)) return;

        System.out.println("\n================================================");
        System.out.println("📊 --- SYSTEM MONITORING DASHBOARD (ADMIN) ---");
        System.out.println("================================================");
        System.out.println("📈 Total Accounts Managed:    " + db.getTotalAccountsCount());
        System.out.println("🔄 Total Processed Ops:       " + db.getTotalGlobalTransactions());
        
        double totalLiquidity = 0;
        // حساب إجمالي السيولة في البنك من المخزن
        for (AccountComponent acc : db.getAllAccountsList()) {
            totalLiquidity += ((Account)acc).getBalance();
        }
        
        System.out.println("💰 Total Bank Liquidity:      $" + String.format("%.2f", totalLiquidity));
        System.out.println("================================================\n");
    }

    // ===========================================================
    // 🧠 ميزة التوصيات الذكية (Smart Recommendations)
    // ===========================================================
    public void getMyBankingAdvice(String accountNumber) {
        if (!hasAccess(UserRole.CUSTOMER)) return;

        Account account = (Account) db.getAccount(accountNumber);
        if (account != null) {
            String advice = RecommendationEngine.getRecommendation(account);
            
            System.out.println("\n--- 🧠 Personal Financial Advisor ---");
            System.out.println("Hello " + account.getOwnerName() + ",");
            System.out.println(advice);
            System.out.println("-------------------------------------\n");
        } else {
            System.out.println("❌ Account not found for recommendations.");
        }
    }

    // ===========================================================
    // 🎫 نظام إدارة تذاكر الدعم (Support Ticket System)
    // ===========================================================

    public void openSupportTicket(String accountNumber, String subject, String message) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        SupportTicket ticket = new SupportTicket(accountNumber, subject, message);
        SupportManager.getInstance().addTicket(ticket);
        System.out.println("🎫 Ticket #" + ticket.getTicketId() + " opened successfully: " + subject);
    }

    public void viewAllTickets() {
        if (!hasAccess(UserRole.MANAGER)) return;
        System.out.println("\n📂 [Admin] All Support Tickets List:");
        if (SupportManager.getInstance().getAllTickets().isEmpty()) {
            System.out.println("   (No tickets found)");
        } else {
            for (SupportTicket t : SupportManager.getInstance().getAllTickets()) {
                System.out.println("   - " + t);
            }
        }
    }

    public void resolveSupportTicket(int ticketId) {
        if (!hasAccess(UserRole.MANAGER)) return;
        for (SupportTicket t : SupportManager.getInstance().getAllTickets()) {
            if (t.getTicketId() == ticketId) {
                t.resolve();
                System.out.println("✅ Ticket #" + ticketId + " has been marked as RESOLVED.");
                return;
            }
        }
        System.out.println("❌ Ticket #" + ticketId + " not found.");
    }

    // ===========================================================
    // 💱 محول العملات (Currency Conversion)
    // ===========================================================

    public void depositForeignCurrency(String accountNumber, double amount, String currency) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        Account account = (Account) db.getAccount(accountNumber);
        if (account != null) {
            double localAmount = CurrencyConverter.getInstance().convertToLocal(amount, currency);
            System.out.println("💱 Converting " + amount + " " + currency + " to local currency...");
            System.out.println("💰 Converted Amount: " + String.format("%.2f", localAmount));
            account.deposit(localAmount);
            db.incrementTransactionCount(); // تحديث عداد العمليات
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    // ===========================================================
    // 👥 إدارة المستفيدين (Beneficiary Management)
    // ===========================================================

    public void addBeneficiary(String accountNum, String bName, String bAccountNum, String nickname) {
        if (!hasAccess(UserRole.CUSTOMER)) return; 
        Account acc = (Account) db.getAccount(accountNum);
        if (acc != null) {
            acc.addBeneficiary(new Beneficiary(bName, bAccountNum, nickname));
            System.out.println("👤 Beneficiary '" + nickname + "' added successfully to account: " + accountNum);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

    public void printBeneficiaries(String accountNum) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        Account acc = (Account) db.getAccount(accountNum);
        if (acc != null) {
            System.out.println("\n👥 Beneficiary List for " + acc.getOwnerName() + " (" + accountNum + "):");
            if (acc.getBeneficiaries().isEmpty()) {
                System.out.println("   (No beneficiaries added yet)");
            } else {
                for (Beneficiary b : acc.getBeneficiaries()) {
                    System.out.println("   - " + b);
                }
            }
        }
    }

    // ===========================================================
    // 🛠 الحسابات والتقارير والعمليات الأساسية
    // ===========================================================

    public void createAccount(String type, String accountNumber, String owner, double initialBalance) {
        if (!hasAccess(UserRole.MANAGER)) return;
        Account newAccount = AccountFactory.createAccount(type, accountNumber, owner, initialBalance);
        db.saveAccount(newAccount);
        System.out.println("✅ Account created: " + type.toUpperCase() + " (" + accountNumber + ")");
        newAccount.addObserver(new EmailNotifier(owner.toLowerCase().replace(" ", "") + "@bank.com"));
        newAccount.addObserver(new SMSNotifier("055xxxxxxx"));
    }

    public void generateSummaryReport() {
        if (!hasAccess(UserRole.MANAGER)) return;
        new AccountSummaryReport().generateReport();
    }

    public void deposit(String accountNumber, double amount) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        Account account = (Account) db.getAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            db.incrementTransactionCount(); // تحديث عداد العمليات
        }
    }

    public void withdraw(String accountNumber, double amount) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        Account account = (Account) db.getAccount(accountNumber);
        if (account != null) {
            System.out.println("\n🔍 [System] Security Check for Account: " + accountNumber);
            if (securityChain.handle(account, amount)) {
                account.withdraw(amount);
                db.incrementTransactionCount(); // تحديث عداد العمليات
            }
        }
    }

    public void transfer(String fromAccountNum, String toAccountNum, double amount) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        Account fromAccount = (Account) db.getAccount(fromAccountNum);
        Account toAccount = (Account) db.getAccount(toAccountNum);
        if (fromAccount != null && toAccount != null) {
            double oldBalance = fromAccount.getBalance();
            fromAccount.withdraw(amount);
            if (fromAccount.getBalance() < oldBalance) {
                toAccount.deposit(amount);
                db.incrementTransactionCount(); // تحديث عداد العمليات
                System.out.println("🔄 Transfer successful.");
            }
        }
    }

    public void closeAccount(String accountNumber) {
        if (!hasAccess(UserRole.ADMIN)) return; 
        Account account = (Account) db.getAccount(accountNumber);
        if (account == null) return;
        System.out.println("\n--- 🛡 Processing Closure for Account: " + accountNumber + " ---");
        if (account instanceof LoanAccount && account.getBalance() > 0) {
            System.out.println("❌ Cannot close Loan Account with outstanding debt.");
            return;
        } 
        if (account.getBalance() > 0) account.withdraw(account.getBalance());
        db.removeAccount(accountNumber);
        System.out.println("🔒 Account successfully closed.");
    }

    public void freezeAccount(String accountNumber) {
        if (!hasAccess(UserRole.MANAGER)) return;
        Account acc = (Account) db.getAccount(accountNumber);
        if (acc != null) acc.setState(new FrozenState());
    }

    public void activateAccount(String accountNumber) {
        if (!hasAccess(UserRole.MANAGER)) return;
        Account acc = (Account) db.getAccount(accountNumber);
        if (acc != null) acc.setState(new ActiveState());
    }

    public void scheduleDeposit(String accountNumber, double amount) {
        if (!hasAccess(UserRole.CUSTOMER)) return;
        Account account = (Account) db.getAccount(accountNumber);
        if (account != null) scheduler.scheduleTransaction(new DepositCommand(account, amount));
    }

    public void executeAllScheduled() {
        if (!hasAccess(UserRole.MANAGER)) return;
        scheduler.runScheduledTransactions();
    }

    public void printAccountHistory(String accountNumber) {
        Account account = (Account) db.getAccount(accountNumber);
        if (account != null) account.printTransactionHistory();
    }
    
    public Account getAccount(String accountNumber) {
        return (Account) db.getAccount(accountNumber);
    }
}