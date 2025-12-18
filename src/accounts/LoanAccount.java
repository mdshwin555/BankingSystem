package accounts;

public class LoanAccount extends Account {
    private double loanPrincipal;

    public LoanAccount(String accountNumber, String ownerName, double loanAmount) {
        super(accountNumber, ownerName, loanAmount);
        this.loanPrincipal = loanAmount;
        System.out.println("💰 Loan Account Created. Principal: " + loanAmount);
    }

    @Override
    protected void handleDeposit(double amount) {
        if (amount > 0) {
            this.balance -= amount; // سداد من أصل القرض
            this.loanPrincipal -= amount;
            System.out.println("💵 Loan payment successful: " + amount + ". Remaining Principal: " + loanPrincipal);

            addTransaction("Loan Payment: -" + amount + " | Balance: " + balance);
            notifyObservers("Loan Payment of " + amount + ". Current Balance: " + balance);
        }
    }

    @Override
    protected void handleWithdraw(double amount) {
        System.out.println("❌ Loan Account: Cannot withdraw funds from a loan account.");
        addTransaction("Denied Withdrawal on Loan Account: " + amount);
    }
}