package security;

import accounts.Account;

public abstract class TransactionHandler {
    protected TransactionHandler nextHandler;

    public void setNextHandler(TransactionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract boolean handle(Account account, double amount);

    protected boolean passToNext(Account account, double amount) {
        if (nextHandler != null) {
            return nextHandler.handle(account, amount);
        }
        return true; // إذا وصلنا لنهاية السلسلة ولم يعترض أحد، المعاملة مقبولة
    }
}