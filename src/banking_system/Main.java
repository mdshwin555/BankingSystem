package banking_system;

import accounts.Account;
import accounts.AccountGroup;
import accounts.SavingsAccount;
import interest.CompoundInterestStrategy;
import interest.SimpleInterestStrategy;
import transactions.ExternalTransferGateway;
import transactions.LegacySystemAdapter;
import transactions.LegacyWireTransferAPI;
import transactions.approval_chain.ApprovalHandler;
import transactions.approval_chain.AutoApprovalHandler;
import transactions.approval_chain.ComplianceHandler;
import transactions.approval_chain.ManagerApprovalHandler;
import transactions.approval_chain.Transaction;

public class Main {
    public static void main(String[] args) {
        System.out.println("========== 🏦 Starting Advanced Banking System ==========");

        BankFacade bank = new BankFacade();

        System.out.println("\n--- 1. Creating Accounts ---");
        bank.createAccount("savings", "SAV-001", "Ahmad", 2000.0);
        bank.createAccount("checking", "CHK-002", "Samer", 1000.0);

        System.out.println("\n========== 🧪 Starting Withdrawal Tests ==========");

        System.out.println("\n[Test 1] Withdrawing 2500 from Savings (Balance: 2000):");
        bank.withdraw("SAV-001", 2500.0);

        System.out.println("\n[Test 2] Withdrawing 1500 from Checking (Balance: 1000):");
        bank.withdraw("CHK-002", 1500.0);

        System.out.println("\n========== 🔄 Starting Transfer Tests ==========");

        System.out.println("\n[Test 3] Transferring 500 from Ahmad to Samer:");
        bank.transfer("SAV-001", "CHK-002", 500.0);

        System.out.println("\n========== 📜 Generating Audit Logs ==========");

        bank.printAccountHistory("SAV-001");
        bank.printAccountHistory("CHK-002");

        System.out.println("\n========== ✅ All Tests Finished ==========");

        System.out.println("\n--- 2. Testing Composite Pattern (Account Group) ---");

        // 1. Create a new Account Group
        AccountGroup familyGroup = new AccountGroup("GRP-001", "Family Savings Pool");

        // 2. Retrieve existing accounts (assuming BankFacade can access the group)
        // NOTE: You need to modify BankFacade to handle/store AccountComponents

        // * For a simplified test here, we will create the accounts directly and add
        // them to the group.
        // * In a proper system, the BankFacade manages the accounts and adds them to
        // groups.

        // Create new accounts (these are Leaf nodes)
        Account ahmed = new Account("ACC-101", "Ahmed", 5000.0);
        Account sara = new SavingsAccount("ACC-102", "Sara", 3000.0, 2.5, new SimpleInterestStrategy());

        // Add accounts to the Group (Composite)
        familyGroup.addComponent(ahmed);
        familyGroup.addComponent(sara);

        System.out.println("\n[Test 4] Group Deposit 1000:");
        familyGroup.deposit(1000.0); // Deposit 1000 into ALL accounts in the group

        familyGroup.getBalance(); // Check total balance

        System.out.println("\n[Test 5] Group Withdraw 7000:");
        // The group attempts to withdraw 7000, drawing from Ahmed (6000) then Sara
        // (4000)
        familyGroup.withdraw(7000.0);

        familyGroup.getBalance();
        // Individual account balances after group withdrawal:
        // Ahmed's balance after partial withdrawal should be 0 (withdrew 6000)
        // Sara's balance should be the remaining total - 7000 (withdrew 1000 from Sara)

        System.out.println("\n========== ⚙️ Testing Adapter Pattern (External API Integration) ==========");

        // 1. Instantiate the incompatible external system (Adaptee)
        LegacyWireTransferAPI legacySystem = new LegacyWireTransferAPI();

        // 2. Instantiate the Adapter, wrapping the Adaptee
        ExternalTransferGateway adapter = new LegacySystemAdapter(legacySystem);

        // 3. The client (Main/BankFacade) calls the standard method on the Adapter
        // It doesn't need to know the complex details of the Legacy API.

        System.out.println("\n[Test 6] Processing external transfer of 1200.0:");
        boolean transferSuccess = adapter.processStandardTransfer("ACC-101", "EXT-100", 1200.0);

        System.out.println("Global Transfer Status: " + (transferSuccess ? "SUCCESS" : "FAILURE"));

        System.out.println("\n========== 💡 Testing Strategy Pattern (Interest Calculation) ==========");

        // 1. Create a Savings Account using Simple Interest Strategy
        SavingsAccount ahmedSavings = new SavingsAccount("SAV-A77", "Ahmed S", 10000.0, 5.0,
                new SimpleInterestStrategy());

        System.out.println("\n[Test 8.1] Applying Simple Interest (Balance: 10000, Rate: 5%):");
        ahmedSavings.addInterest(); // Interest = 10000 * 0.05 = 500.0

        System.out.println("\n--- Changing Strategy at Runtime ---");

        // 2. Change the strategy at runtime to Compound Interest
        ahmedSavings.setInterestStrategy(new CompoundInterestStrategy());

        System.out.println("\n[Test 8.2] Applying Compound Interest (Balance: 10500, Rate: 5%):");
        ahmedSavings.addInterest(); // Interest = 10500 * (1.05 - 1) = 525.0

        System.out.println("\nFinal Balance for Ahmed S: " + ahmedSavings.getBalance());

        // =================================================================
        // أضف هذا الكود في نهاية ميثود main الحالية
        // =================================================================

        System.out.println("\n\n========== ⛓️ Testing Chain of Responsibility (Transaction Approval) ==========");

        // 1. إنشاء حلقات السلسلة
        ApprovalHandler autoApprover = new AutoApprovalHandler();
        ApprovalHandler managerApprover = new ManagerApprovalHandler();
        ApprovalHandler complianceApprover = new ComplianceHandler();

        // 2. بناء السلسلة بالترتيب: Auto -> Manager -> Compliance
        autoApprover.setNext(managerApprover);
        managerApprover.setNext(complianceApprover);

        // 3. إنشاء معاملات لاختبار السلسلة
        Transaction transaction1 = new Transaction(11500.0, "CHK-002", "SAV-001");
        Transaction transaction2 = new Transaction(8500.0, "SAV-001", "EXT-ACCT");
        Transaction transaction3 = new Transaction(25000.0, "SAV-001", "CORP-ACCT");

        System.out.println("\n--- Processing Transaction 1 ($500.0) ---");
        // ابدأ دائمًا من الحلقة الأولى في السلسلة
        autoApprover.process(transaction1);
        System.out.println("Final Status: " + transaction1.getStatus());

        System.out.println("\n--- Processing Transaction 2 ($8,500.0) ---");
        autoApprover.process(transaction2);
        System.out.println("Final Status: " + transaction2.getStatus());

        System.out.println("\n--- Processing Transaction 3 ($25,000.0) ---");
        autoApprover.process(transaction3);
        System.out.println("Final Status: " + transaction3.getStatus());





        // =================================================================
//  أضف هذا الكود في نهاية ميثود main الحالية
// =================================================================

System.out.println("\n\n========== 🏦 Testing Loan Approval Chain of Responsibility ==========");

// أنشأنا كائن bank في بداية main، لذلك يمكننا استخدامه مباشرة
// Case 1: Small loan, great credit -> Should be approved by Junior Officer
bank.requestLoan("Ahmad", 8000, 720, 4000);

// Case 2: Medium loan, good credit -> Should be passed to and approved by Senior Officer
bank.requestLoan("Samer", 45000, 680, 6000);

// Case 3: Large loan, okay credit -> Should be passed to and approved by Credit Committee
bank.requestLoan("Layla", 150000, 610, 10000);

// Case 4: Loan too large -> Should be rejected by Credit Committee
bank.requestLoan("Ziad", 300000, 750, 15000);

// Case 5: Good loan amount, but bad credit -> Should be passed up the chain and eventually rejected
bank.requestLoan("Mona", 40000, 550, 5000);


    }
}