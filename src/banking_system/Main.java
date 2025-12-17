package banking_system;

import accounts.*;
import interest.CompoundInterestStrategy;
import interest.SimpleInterestStrategy;
import transactions.ExternalTransferGateway;
import transactions.LegacySystemAdapter;
import transactions.LegacyWireTransferAPI;

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

        // * For a simplified test here, we will create the accounts directly and add them to the group.
        // * In a proper system, the BankFacade manages the accounts and adds them to groups.

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
        // The group attempts to withdraw 7000, drawing from Ahmed (6000) then Sara (4000)
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
        SavingsAccount ahmedSavings = new SavingsAccount("SAV-A77", "Ahmed S", 10000.0, 5.0, new SimpleInterestStrategy());

        System.out.println("\n[Test 8.1] Applying Simple Interest (Balance: 10000, Rate: 5%):");
        ahmedSavings.addInterest(); // Interest = 10000 * 0.05 = 500.0

        System.out.println("\n--- Changing Strategy at Runtime ---");

        // 2. Change the strategy at runtime to Compound Interest
        ahmedSavings.setInterestStrategy(new CompoundInterestStrategy());

        System.out.println("\n[Test 8.2] Applying Compound Interest (Balance: 10500, Rate: 5%):");
        ahmedSavings.addInterest(); // Interest = 10500 * (1.05 - 1) = 525.0

        System.out.println("\nFinal Balance for Ahmed S: " + ahmedSavings.getBalance());


        System.out.println("\n--- 3. Testing Decorator Pattern (Fees on Overdraft) ---");

        // 1. إنشاء حساب تدقيق (CheckingAccount) لديه بالفعل منطق السحب على المكشوف (limit 1000.0)
        CheckingAccount chkAccount = new CheckingAccount("CHK-D99", "Sarah D", 1500.0, 1000.0);

        // 2. تزيين هذا الحساب بإضافة رسوم 50.0 عند استخدام السحب على المكشوف
        AccountComponent decoratedAccountWithFee = new OverdraftProtectionDecorator(chkAccount, 50.0);

        System.out.println("\n[Test 9.1] Withdrawal 500 (No Overdraft):");
        decoratedAccountWithFee.withdraw(500.0);
        // الرصيد: 1500 - 500 = 1000.0 (لا رسوم)

        System.out.println("\n[Test 9.2] Withdrawal 1200 (Uses Overdraft):");
        decoratedAccountWithFee.withdraw(1200.0);
        // 1. السحب الأساسي: 1000 - 1200 = -200.0
        // 2. المُزيّن يرى أن الرصيد أصبح سلبياً (-200.0 < 0)
        // 3. المُزيّن يضيف رسوم 50.0: -200.0 - 50.0 = -250.0

        System.out.println("\nFinal Balance for Sarah D: " + decoratedAccountWithFee.getBalance());

        // يمكنك أيضاً تزيينها بـ PremiumServiceDecorator:
        AccountComponent fullyDecorated = new PremiumServiceDecorator(decoratedAccountWithFee);

        System.out.println("\n[Test 9.3] Deposit with Premium Service:");
        fullyDecorated.deposit(500.0);
        // الرصيد: -250 + 500 = 250.0


        System.out.println("\n--- 4. Testing Factory Logic (Loan & Investment Accounts) ---");

        // [Test 10.1] Create a Loan Account (Principal: 5000.0)
        bank.createAccount("loan", "LON-999", "Khaled M", 5000.0);

        // [Test 10.2] Create an Investment Account
        bank.createAccount("investment", "INV-100", "Lama H", 10000.0);

        // استرجاع حساب القرض لإجراء اختبار السلوك
        Account khaledLoan = bank.getAccount("LON-999");

        if (khaledLoan != null) {
            System.out.println("\n[Test 10.3] Loan Account: Attempting to Withdraw 1000.0");
            khaledLoan.withdraw(1000.0); // يجب أن يفشل (منطق LoanAccount)

            System.out.println("\n[Test 10.4] Loan Account: Making Payment (Deposit 500.0)");
            khaledLoan.deposit(500.0); // يجب أن ينجح ويقلل رصيد القرض (منطق LoanAccount)

            System.out.println("\n[Test 10.5] Final Status Check:");
            System.out.println("Current Loan Balance (Liability): " + khaledLoan.getBalance());
            // يجب أن يكون الرصيد 5000 - 500 = 4500
        }


        // في ملف Main.java - القسم 5
        System.out.println("\n--- 5. Testing Modification & Closure ---");

// [Test 11.1] تعديل اسم صاحب حساب
        bank.updateAccountInfo("SAV-001", "Ahmad Al-Saeed");

// [Test 11.2] محاولة إغلاق حساب قرض عليه ديون (يجب أن يفشل)
        bank.closeAccount("LON-999"); // حساب خالد الذي أنشأناه سابقاً ورصيده 4500

// [Test 11.3] تسوية القرض ثم إغلاقه
        Account khaledLoan1 = bank.getAccount("LON-999");
        khaledLoan1.deposit(4500.0); // سداد القرض بالكامل
        bank.closeAccount("LON-999"); // الآن يجب أن ينجح الإغلاق

// [Test 11.4] إغلاق حساب توفير فيه رصيد (يجب أن يسحب المال ثم يغلق)
        bank.closeAccount("SAV-001");


        System.out.println("\n--- 6. Testing State Pattern (Account Transitions) ---");

// 1. تجميد حساب سامر
        bank.freezeAccount("CHK-002");

// 2. محاولة السحب من الحساب المجمد (يجب أن يفشل)
        System.out.println("\n[Test 12.1] Attempting to withdraw from Frozen account:");
        bank.withdraw("CHK-002", 100.0);

// 3. محاولة الإيداع في الحساب المجمد (يجب أن ينجح)
        System.out.println("\n[Test 12.2] Attempting to deposit into Frozen account:");
        bank.deposit("CHK-002", 300.0);

// 4. إعادة تفعيل الحساب والسحب منه
        bank.activateAccount("CHK-002");
        bank.withdraw("CHK-002", 100.0);


        System.out.println("\n--- 7. Testing Chain of Responsibility (Security Checks) ---");

// [Test 13.1] مبلغ صغير (يمر من كل الفحوصات)
        System.out.println("\n[Test 13.1] Withdrawal of 100:");
        bank.withdraw("CHK-002", 100.0);

// [Test 13.2] مبلغ يحتاج موافقة مدير (> 5000)
        System.out.println("\n[Test 13.2] Withdrawal of 6000 (Manager Approval):");
        bank.withdraw("CHK-002", 6000.0);

// [Test 13.3] مبلغ مشبوه جداً (> 10000)
        System.out.println("\n[Test 13.3] Withdrawal of 15000 (Fraud Alert):");
        bank.withdraw("CHK-002", 15000.0);

        System.out.println("\n--- 8. Testing Command Pattern (Scheduled Transactions) ---");

// [Test 14.1] جدولة عملية إيداع لحساب سامر
        bank.scheduleDeposit("CHK-002", 500.0);

        System.out.println("Current Balance before scheduled execution: " + bank.getAccount("CHK-002").getBalance());

// [Test 14.2] تنفيذ العمليات المجدولة (كأننا في نهاية الشهر)
        bank.executeAllScheduled();

        System.out.println("Current Balance after scheduled execution: " + bank.getAccount("CHK-002").getBalance());
    }



}