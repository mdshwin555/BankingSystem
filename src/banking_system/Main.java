package banking_system;

import accounts.Account;
import accounts.AccountGroup;
import accounts.SavingsAccount;
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
    }
}