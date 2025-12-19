package transactions.approval_chain;


public class AutoApprovalHandler extends ApprovalHandler {

    private static final double MAX_AUTO_APPROVAL_AMOUNT = 1000.0;

    @Override
    public void process(Transaction transaction) {
        if (transaction.getAmount() <= MAX_AUTO_APPROVAL_AMOUNT) {
            System.out.println("✅ [Auto Approver] Transaction approved automatically.");
            transaction.setStatus("APPROVED by AutoApprover");
        } else if (nextHandler != null) {
            System.out.println("-> [Auto Approver] Amount too high. Passing to the next handler...");
            nextHandler.process(transaction);
        } else {
            System.out.println("❌ [Auto Approver] Transaction cannot be processed further.");
            transaction.setStatus("REJECTED - Exceeds all limits");
        }
    }
}
