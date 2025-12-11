package loan_processing;

public class CreditCommittee extends LoanApprovalHandler {
    private static final double MAX_AMOUNT = 250000; // 250,000 دولار
    private static final int MIN_CREDIT_SCORE = 600;

    @Override
    public void processRequest(LoanApplication application) {
        if (application.getAmount() <= MAX_AMOUNT && application.getCreditScore() >= MIN_CREDIT_SCORE) {
            application.setStatus("APPROVED by Credit Committee");
        } else {
            // هذه هي نهاية السلسلة، إذا لم تتم الموافقة هنا، يتم الرفض
            application.setStatus("REJECTED by Credit Committee");
        }
    }
}
