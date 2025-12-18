package loan_processing;

public class LoanApplication {
    private final String applicantName;
    private final double amount;
    private final int creditScore;
    private final double monthlyIncome;
    private String status;

    public LoanApplication(String applicantName, double amount, int creditScore, double monthlyIncome) {
        this.applicantName = applicantName;
        this.amount = amount;
        this.creditScore = creditScore;
        this.monthlyIncome = monthlyIncome;
        this.status = "PENDING";
    }

    // Getters for all fields
    public double getAmount() { return amount; }
    public int getCreditScore() { return creditScore; }
    public double getMonthlyIncome() { return monthlyIncome; }
    public String getApplicantName() { return applicantName; }
    public String getStatus() { return status; }

    // Setter only for the status
    public void setStatus(String status) {
        this.status = status;
        System.out.println("[Loan Status Update] Loan for " + applicantName + " is now: " + status);
    }
}
