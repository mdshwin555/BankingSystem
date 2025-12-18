package accounts;

import java.util.ArrayList;
import java.util.List;
import notifications.NotificationObserver;

/**
 * Account Class
 * -------------
 * يمثل الحساب الأساسي. 
 * تم الحفاظ على كافة الدوال القديمة (handleDeposit, handleWithdraw) لضمان عمل CheckingAccount و LoanAccount.
 */
public class Account implements AccountComponent {
    protected String accountNumber;
    protected double balance;
    protected String ownerName;
    private AccountState currentState;
    
    // سجل العمليات (Audit Log)
    protected List<String> transactionHistory = new ArrayList<>();
    
    // المراقبون (Observer Pattern)
    private List<NotificationObserver> observers = new ArrayList<>();

    // ميزة المستفيدين (Beneficiary Management) - ضرورية لعمل BankFacade
    protected List<Beneficiary> beneficiaries = new ArrayList<>();

    public Account(String accountNumber, String ownerName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        addTransaction("Initial Balance: " + initialBalance);
        this.currentState = new ActiveState();
    }

    // --- ميزة إدارة المستفيدين ---
    public void addBeneficiary(Beneficiary b) {
        beneficiaries.add(b);
        addTransaction("Added beneficiary: " + b.getNickname());
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    // --- العمليات الأساسية ---
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    protected void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.update(message);
        }
    }

    protected void addTransaction(String message) {
        transactionHistory.add(message);
    }

    public void setState(AccountState state) {
        this.currentState = state;
        System.out.println("🔄 Account " + accountNumber + " state changed to: " + state.getStateName());
        addTransaction("State changed to " + state.getStateName());
    }

    public void deposit(double amount) {
        handleDeposit(amount);
    }

    public void withdraw(double amount) {
        handleWithdraw(amount);
    }

    // هذه الدوال ضرورية جداً لـ CheckingAccount و LoanAccount لأنهم يقومون بعمل @Override لها
    protected void handleDeposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            addTransaction("Deposit: +" + amount + " | New Balance: " + balance);
            notifyObservers("Deposit of " + amount + ". Current Balance: " + balance);
        }
    }

    protected void handleWithdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            this.balance -= amount;
            addTransaction("Withdraw: -" + amount + " | New Balance: " + balance);
            notifyObservers("Withdrawal of " + amount + ". Current Balance: " + balance);
        } else {
            System.out.println(">> Insufficient balance!");
            addTransaction("Failed Withdraw Attempt: " + amount);
        }
    }

    public String getStateName() { return currentState.getStateName(); }
    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String newName) {
        this.ownerName = newName;
        addTransaction("Owner name updated to: " + newName);
    }

    public void printTransactionHistory() {
        System.out.println("\n📜 --- Transaction History for " + ownerName + " (" + accountNumber + ") ---");
        for (String record : transactionHistory) {
            System.out.println("   • " + record);
        }
        System.out.println("------------------------------------------------");
    }

    @Override
    public void addComponent(AccountComponent component) {
        throw new UnsupportedOperationException("Individual accounts cannot have sub-components.");
    }

    @Override
    public void removeComponent(AccountComponent component) {
        throw new UnsupportedOperationException("Individual accounts cannot have sub-components.");
    }
}