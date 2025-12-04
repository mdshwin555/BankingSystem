package accounts;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String ownerName, double initialBalance, double overdraftLimit) {
        super(accountNumber, ownerName, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (balance + overdraftLimit) >= amount) {
            balance -= amount;
            System.out.println(">> Withdrawal successful: " + amount + " (Overdraft Used)");
            addTransaction("Overdraft Withdraw: -" + amount + " | New Balance: " + balance);
            notifyObservers("Overdraft Withdrawal of " + amount + ". Current Balance: " + balance);
        } else {
            System.out.println(">> Transaction Declined! Overdraft limit exceeded.");
            addTransaction("Failed Overdraft Attempt: " + amount);
        }
    }
}