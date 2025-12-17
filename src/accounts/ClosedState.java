package accounts;

public class ClosedState implements AccountState {
    @Override
    public void deposit(Account account, double amount) {
        System.out.println("❌ Error: Cannot deposit to a CLOSED account.");
    }

    @Override
    public void withdraw(Account account, double amount) {
        System.out.println("❌ Error: Cannot withdraw from a CLOSED account.");
    }

    @Override
    public String getStateName() { return "CLOSED"; }
}