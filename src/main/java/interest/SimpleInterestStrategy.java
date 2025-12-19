package interest;

public class SimpleInterestStrategy implements InterestCalculationStrategy {

    @Override
    public double calculateInterest(double balance, double rate) {
        // Simple Interest Calculation: Interest = Balance * (Rate / 100)
        double interest = balance * (rate / 100);
        System.out.println("📈 [Strategy] Calculated Simple Interest: " + interest);
        return interest;
    }
}