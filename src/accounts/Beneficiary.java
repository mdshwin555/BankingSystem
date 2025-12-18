package accounts;

/**
 * يمثل شخصاً مضافاً لقائمة المستفيدين للتحويل السريع.
 */
public class Beneficiary {
    private String name;
    private String accountNumber;
    private String nickname;

    public Beneficiary(String name, String accountNumber, String nickname) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.nickname = nickname;
    }

    public String getName() { return name; }
    public String getAccountNumber() { return accountNumber; }
    public String getNickname() { return nickname; }

    @Override
    public String toString() {
        return nickname + " (" + name + ") - Acc: " + accountNumber;
    }
}