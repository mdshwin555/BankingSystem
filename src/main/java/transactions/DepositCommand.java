package transactions;

import accounts.Account;

public class DepositCommand implements TransactionCommand {
    private Account account;
    private double amount;

    public DepositCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        System.out.println("🕒 [Scheduled Action] Executing scheduled deposit...");
        account.deposit(amount);
    }
}