package loan_processing;

public abstract class LoanApprovalHandler {
    protected LoanApprovalHandler nextHandler;

    public void setNext(LoanApprovalHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void processRequest(LoanApplication application);
}
