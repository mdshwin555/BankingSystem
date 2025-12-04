package banking_system;

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
    }
}