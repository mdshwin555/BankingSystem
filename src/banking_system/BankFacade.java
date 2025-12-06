package banking_system;

import java.util.HashMap;
import java.util.Map;
import accounts.Account;
import accounts.CheckingAccount;
import accounts.SavingsAccount;
import interest.SimpleInterestStrategy;
import notifications.EmailNotifier;
import notifications.SMSNotifier;

/**
 * BankFacade Class
 * -----------------
 * This class implements the **Facade Design Pattern**.
 * It provides a simplified interface for clients (like Main class) to interact with the complex banking system.
 * It hides the complexities of account creation, notification registration, and transaction handling.
 */
public class BankFacade {
    // A simple in-memory database to store account objects using their Account Number as the key.
    private Map<String, Account> accountsDatabase = new HashMap<>();

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

        // Factory logic: Determine account type at runtime
        switch (type.toLowerCase()) {
            case "savings":
                // Savings account with a default interest rate of 3.0%
                newAccount = new SavingsAccount(accountNumber, owner, initialBalance, 3.0, new SimpleInterestStrategy());
                break;
            case "checking":
                // Checking account with a default overdraft limit of 1000.0
                newAccount = new CheckingAccount(accountNumber, owner, initialBalance, 1000.0);
                break;
            default:
                // Standard account
                newAccount = new Account(accountNumber, owner, initialBalance);
                break;
        }

        // Observer Pattern: Automatically attach notification services
        newAccount.addObserver(new EmailNotifier(owner + "@example.com"));
        newAccount.addObserver(new SMSNotifier("0955555555"));

        // Store the account in the database
        accountsDatabase.put(accountNumber, newAccount);
        System.out.println("✅ Account created [" + type + "] successfully for: " + owner);
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
    public void withdraw(String accountNumber, double amount) {
        Account account = accountsDatabase.get(accountNumber);
        if (account != null) {
            account.withdraw(amount);
        } else {
            System.out.println("❌ Account not found!");
        }
    }

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
}