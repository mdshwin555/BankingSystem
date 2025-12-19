package accounts;

/**
 * PremiumServiceDecorator (Concrete Decorator)
 * --------------------------------------------
 * Adds premium service features to the decorated account.
 */
public class PremiumServiceDecorator extends AccountDecorator {

    public PremiumServiceDecorator(AccountComponent decoratedAccount) {
        super(decoratedAccount);
        System.out.println("🌟 Added Premium Service features to account " + decoratedAccount.getAccountNumber());
    }

    // You can override any method to add pre/post logic.
    @Override
    public void deposit(double amount) {
        System.out.println("🔔 Premium Alert: High-speed deposit processing initiated.");
        decoratedAccount.deposit(amount);
        System.out.println("✅ Premium Alert: Deposit complete and instantly verified.");
    }
}