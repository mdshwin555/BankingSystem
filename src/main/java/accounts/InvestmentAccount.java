package accounts;

public class InvestmentAccount extends Account {
    private String riskLevel; // e.g., Low, Medium, High

    public InvestmentAccount(String accountNumber, String ownerName, double initialBalance, String riskLevel) {
        super(accountNumber, ownerName, initialBalance);
        this.riskLevel = riskLevel;
        System.out.println("📈 Investment Account Created. Risk Level: " + riskLevel);
    }

    // يمكن إضافة وظائف فريدة هنا، مثل:
    public void rebalancePortfolio() {
        System.out.println("🔄 Investment Account " + accountNumber + ": Rebalancing portfolio based on " + riskLevel + " risk profile.");
    }

    // عمليات deposit و withdraw تورث السلوك العادي من Account
}