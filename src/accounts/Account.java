package accounts;

import java.util.ArrayList;
import java.util.List;
import notifications.NotificationObserver;

/**
 * Account Class
 * -------------
 * Represents a generic bank account.
 * Acts as the **Subject** in the **Observer Design Pattern**.
 * Maintains a list of observers and notifies them of any state changes (deposit/withdraw).
 * Also handles **Audit Logging** for transaction history.
 */
public class Account {
    protected String accountNumber;
    protected double balance;
    protected String ownerName;
    
    // Stores the history of all transactions (Audit Log)
    protected List<String> transactionHistory = new ArrayList<>();
    
    // Observer Pattern: List of subscribers (e.g., Email, SMS)
    private List<NotificationObserver> observers = new ArrayList<>();

    public Account(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        // Log the creation of the account
        addTransaction("Initial Balance: " + initialBalance);
    }

    /**
     * Attaches an observer to this account.
     * @param observer The observer instance (Email or SMS notifier).
     */
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all attached observers with a specific message.
     * This method is protected so subclasses can use it.
     */
    protected void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Helper method to record a transaction in the history log.
     */
    protected void addTransaction(String message) {
        transactionHistory.add(message);
    }

    /**
     * Deposits money into the account.
     * Triggers a notification and logs the transaction.
     */
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println(">> Deposit successful: " + amount);
            
            // Log & Notify
            addTransaction("Deposit: +" + amount + " | New Balance: " + balance);
            notifyObservers("Deposit of " + amount + ". Current Balance: " + balance);
        }
    }

    /**
     * Withdraws money from the account if sufficient balance exists.
     * Triggers a notification and logs the transaction.
     */
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            this.balance -= amount;
            System.out.println(">> Withdrawal successful: " + amount);
            
            // Log & Notify
            addTransaction("Withdraw: -" + amount + " | New Balance: " + balance);
            notifyObservers("Withdrawal of " + amount + ". Current Balance: " + balance);
        } else {
            System.out.println(">> Insufficient balance!");
            addTransaction("Failed Withdraw Attempt: " + amount);
        }
    }

    /**
     * Prints all recorded transactions for this account.
     * Fulfills the 'Audit Logging' requirement.
     */
    public void printTransactionHistory() {
        System.out.println("\n📜 --- Transaction History for " + ownerName + " (" + accountNumber + ") ---");
        for (String record : transactionHistory) {
            System.out.println("   • " + record);
        }
        System.out.println("------------------------------------------------");
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}