package accounts;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String ownerName, double initialBalance, double overdraftLimit) {
        super(accountNumber, ownerName, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }


    @Override
    public void withdraw(double amount) {
        // هذا هو السر! إذا كان الحساب مجمداً، لا تسمح للدالة بإكمال عملها
        if (getStateName().equals("FROZEN")) {
            System.out.println("❌ Transaction Denied: Account " + accountNumber + " is FROZEN.");
            return; // توقف هنا ولا تنفذ السحب
        }

        // إذا لم يكن مجمداً، نفذ منطق السحب القديم الخاص بك
        if (amount > 0 && (balance + overdraftLimit) >= amount) {
            balance -= amount;
            System.out.println(">> Withdrawal successful: " + amount + " (Overdraft Used)");
            addTransaction("Overdraft Withdraw: -" + amount + " | New Balance: " + balance);
            notifyObservers("Overdraft Withdrawal of " + amount + ". Current Balance: " + balance);
        } else {
            System.out.println("❌ Transaction Declined! Overdraft limit exceeded.");
        }
    }

    // في ملف accounts/CheckingAccount.java
    @Override
    protected void handleWithdraw(double amount) {
        if (amount > 0 && (balance + overdraftLimit) >= amount) {
            balance -= amount;
            System.out.println(">> Withdrawal successful: " + amount + " (Overdraft Used)");
            addTransaction("Overdraft Withdraw: -" + amount + " | New Balance: " + balance);
            notifyObservers("Overdraft Withdrawal of " + amount + ". Current Balance: " + balance);
        } else {
            System.out.println("❌ Transaction Declined! Overdraft limit exceeded.");
        }
    }
}