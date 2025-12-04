package accounts;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, String ownerName, double initialBalance, double interestRate) {
        super(accountNumber, ownerName, initialBalance);
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = this.balance * (interestRate / 100);
        deposit(interest);
        System.out.println("💰 [Savings] Interest added: " + interest);
    }
}