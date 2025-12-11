package loan_processing;

public class JuniorLoanOfficer extends LoanApprovalHandler {
    private static final double MAX_AMOUNT = 10000; // 10,000 دولار
    private static final int MIN_CREDIT_SCORE = 700;

    @Override
    public void processRequest(LoanApplication application) {
        if (application.getAmount() <= MAX_AMOUNT && application.getCreditScore() >= MIN_CREDIT_SCORE) {
            application.setStatus("APPROVED by Junior Loan Officer");
        } else if (nextHandler != null) {
            System.out.println("-> [Junior Officer] Cannot approve. Passing to Senior Officer...");
            nextHandler.processRequest(application);
        }
    }
}
