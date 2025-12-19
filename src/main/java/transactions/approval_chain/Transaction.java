package transactions.approval_chain;

public class Transaction {
    private final double amount;
    private final String fromAccount;
    private final String toAccount;
    private String status; // e.g., "PENDING", "APPROVED", "REJECTED"

    public Transaction(double amount, String from, String to) {
        this.amount = amount;
        this.fromAccount = from;
        this.toAccount = to;
        this.status = "PENDING";
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        System.out.println("Transaction status for " + amount + " is now: " + status);
    }
    
    @Override
    public String toString() {
        return "Transaction of " + amount + " from " + fromAccount + " to " + toAccount;
    }
}
