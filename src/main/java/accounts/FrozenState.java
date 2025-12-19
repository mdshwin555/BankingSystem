package accounts;

public class FrozenState implements AccountState {
    @Override
    public void deposit(Account account, double amount) {
        account.handleDeposit(amount);
    }

    @Override
    public void withdraw(Account account, double amount) {
        System.out.println("❌ Transaction Denied: Account " + account.getAccountNumber() + " is FROZEN.");
    }

    @Override
    public String getStateName() { return "FROZEN"; }
}