package transactions.approval_chain;

public class ManagerApprovalHandler extends ApprovalHandler {

    private static final double MAX_MANAGER_APPROVAL_AMOUNT = 10000.0;

    @Override
    public void process(Transaction transaction) {
        if (transaction.getAmount() <= MAX_MANAGER_APPROVAL_AMOUNT) {
            System.out.println("👤 [Manager] Transaction approved by manager.");
            transaction.setStatus("APPROVED by Manager");
        } else if (nextHandler != null) {
            System.out.println("-> [Manager] Amount too high. Passing to the next handler...");
            nextHandler.process(transaction);
        } else {
            System.out.println("❌ [Manager] Transaction REJECTED. Amount exceeds manager's limit.");
            transaction.setStatus("REJECTED - Exceeds Manager Limit");
        }
    }
}
