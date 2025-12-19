package accounts;

public class ActiveState implements AccountState {
    @Override
    public void deposit(Account account, double amount) {
        account.handleDeposit(amount); // تنفيذ الإيداع الفعلي
    }

    @Override
    public void withdraw(Account account, double amount) {
        account.handleWithdraw(amount); // تنفيذ السحب الفعلي
    }

    @Override
    public String getStateName() { return "ACTIVE"; }
}