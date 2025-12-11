package accounts;

public interface AccountComponent {
        // Operations applicable to both individual accounts and groups
        void deposit(double amount);
        void withdraw(double amount);
        double getBalance();

        // Optional: Methods for managing the hierarchy (mainly used by AccountGroup)
        void addComponent(AccountComponent component);
        void removeComponent(AccountComponent component);
        String getAccountNumber(); // To identify the component
}

