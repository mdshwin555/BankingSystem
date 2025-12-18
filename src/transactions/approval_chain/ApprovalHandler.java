package transactions.approval_chain;

public abstract class ApprovalHandler {
    protected ApprovalHandler nextHandler;

    /**
     * Sets the next handler in the chain.
     * @param nextHandler The next handler to process the request if this one can't.
     */
    public void setNext(ApprovalHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * Processes the transaction. Each concrete handler will implement its own logic.
     * @param transaction The transaction to be processed.
     */
    public abstract void process(Transaction transaction);
}
