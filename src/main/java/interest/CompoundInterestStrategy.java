package interest;

public class CompoundInterestStrategy implements InterestCalculationStrategy {

    @Override
    public double calculateInterest(double balance, double rate) {
        // Compound Interest Calculation (Simplified for one period):
        // A = P(1 + r/n)^(nt) -> Simplified to: Interest = P * (1 + r)^t - P
        // Assuming t=1 (one period) and n=1 (compounded once)
        double interest = balance * (Math.pow(1 + (rate / 100), 1) - 1);
        System.out.println("📈 [Strategy] Calculated Compound Interest: " + interest);
        return interest;
    }
}