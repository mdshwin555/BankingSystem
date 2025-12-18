package loan_processing;

public class SeniorLoanOfficer extends LoanApprovalHandler {
    private static final double MAX_AMOUNT = 50000; // 50,000 دولار
    private static final int MIN_CREDIT_SCORE = 650;

    @Override
    public void processRequest(LoanApplication application) {
        if (application.getAmount() <= MAX_AMOUNT && application.getCreditScore() >= MIN_CREDIT_SCORE) {
            application.setStatus("APPROVED by Senior Loan Officer");
        } else if (nextHandler != null) {
            System.out.println("-> [Senior Officer] Cannot approve. Passing to Credit Committee...");
            nextHandler.processRequest(application);
        }
    }
}
