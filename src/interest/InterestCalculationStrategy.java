package interest;

/**
 * Strategy Interface: Defines the common method for all interest calculation algorithms.
 */
public interface InterestCalculationStrategy {

    /**
     * Calculates the interest amount based on the current balance.
     * @param balance The current account balance.
     * @param rate The annual interest rate percentage.
     * @return The calculated interest amount.
     */
    double calculateInterest(double balance, double rate);
}