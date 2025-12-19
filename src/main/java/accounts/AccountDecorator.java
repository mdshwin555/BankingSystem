package accounts;

/**
 * AccountDecorator Class (Base Decorator)
 * -----------------------------------------
 * Implements AccountComponent and holds a reference to another AccountComponent.
 * Delegates all calls to the wrapped component.
 */
public abstract class AccountDecorator implements AccountComponent {
    // Reference to the wrapped component (Account or another Decorator)
    protected AccountComponent decoratedAccount;

    public AccountDecorator(AccountComponent decoratedAccount) {
        this.decoratedAccount = decoratedAccount;
    }

    // Delegation of core methods
    @Override
    public void deposit(double amount) {
        decoratedAccount.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        decoratedAccount.withdraw(amount);
    }

    @Override
    public double getBalance() {
        return decoratedAccount.getBalance();
    }

    // Delegation of hierarchy methods
    @Override
    public void addComponent(AccountComponent component) {
        decoratedAccount.addComponent(component);
    }

    @Override
    public void removeComponent(AccountComponent component) {
        decoratedAccount.removeComponent(component);
    }

    @Override
    public String getAccountNumber() {
        return decoratedAccount.getAccountNumber();
    }
}