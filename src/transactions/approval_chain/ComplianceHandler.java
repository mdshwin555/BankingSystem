package transactions.approval_chain;

public class ComplianceHandler extends ApprovalHandler {

    @Override
    public void process(Transaction transaction) {
        // Any transaction that reaches this level is considered large enough for compliance review.
        System.out.println("🚨 [Compliance Dept] Transaction requires special compliance check. Approved.");
        transaction.setStatus("APPROVED by Compliance");
    }
}
