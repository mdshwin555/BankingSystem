package accounts;
// File: accounts/AccountGroup.java

import java.util.ArrayList;
import java.util.List;

public class AccountGroup implements AccountComponent {
    private String groupId;
    private String groupName;
    // The list holds both individual accounts (Leaves) and other groups (Composites)
    private List<AccountComponent> components = new ArrayList<>();

    public AccountGroup(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        System.out.println("📦 Group created: " + groupName + " (" + groupId + ")");
    }

    // Implementation of Composite-specific methods (Managing the hierarchy)
    @Override
    public void addComponent(AccountComponent component) {
        components.add(component);
        System.out.println("   -> Added component: " + component.getAccountNumber());
    }

    @Override
    public void removeComponent(AccountComponent component) {
        components.remove(component);
        System.out.println("   -> Removed component: " + component.getAccountNumber());
    }

    // Implementation of Shared Operations (Delegation)
    @Override
    public void deposit(double amount) {
        System.out.println("\n[Group Deposit] Depositing " + amount + " into all accounts in " + groupName);
        // Delegate the deposit operation to all children
        for (AccountComponent component : components) {
            component.deposit(amount);
        }
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("\n[Group Withdraw] Attempting to withdraw " + amount + " from " + groupName);
        // Implement a common strategy, e.g., withdraw from each account sequentially
        double remainingAmount = amount;
        for (AccountComponent component : components) {
            if (remainingAmount <= 0) break;

            // Only attempt to withdraw the remaining amount
            double componentBalance = component.getBalance();
            double withdrawable = Math.min(remainingAmount, componentBalance);

            if (withdrawable > 0) {
                component.withdraw(withdrawable);
                remainingAmount -= withdrawable;
            }
        }

        if (remainingAmount > 0) {
            System.out.println("❌ Group Withdrawal failed! Could not withdraw the total amount. Remaining: " + remainingAmount);
        } else {
            System.out.println("✅ Group Withdrawal successful.");
        }
    }

    @Override
    public double getBalance() {
        // Calculate the total balance by summing up balances of all children
        double totalBalance = 0;
        for (AccountComponent component : components) {
            totalBalance += component.getBalance();
        }
        System.out.println("📊 Total balance for " + groupName + ": " + totalBalance);
        return totalBalance;
    }

    @Override
    public String getAccountNumber() {
        return this.groupId;
    }
}
