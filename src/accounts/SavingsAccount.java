package accounts;

import interest.InterestCalculationStrategy; // Import the Strategy Interface

public class SavingsAccount extends Account {
    private double interestRate;
    // New: Reference to the strategy object (Context holds the Strategy)
    private InterestCalculationStrategy interestStrategy;

    public SavingsAccount(String accountNumber, String ownerName, double initialBalance, double interestRate, InterestCalculationStrategy strategy) {
        super(accountNumber, ownerName, initialBalance);
        this.interestRate = interestRate;
        this.interestStrategy = strategy;
    }

    // New: Setter to change strategy at runtime (key feature of the pattern)
    public void setInterestStrategy(InterestCalculationStrategy interestStrategy) {
        this.interestStrategy = interestStrategy;
        System.out.println("[Savings] Interest strategy updated.");
    }

    // Modified Method: Delegation to the current strategy
    public void addInterest() {
        if (interestStrategy == null) {
            System.out.println("❌ [Savings] Cannot calculate interest: No strategy configured.");
            return;
        }

        // Delegate the calculation to the current strategy object
        double interest = interestStrategy.calculateInterest(this.balance, this.interestRate);

        deposit(interest);
        System.out.println("💰 [Savings] Interest added: " + interest);
    }
}