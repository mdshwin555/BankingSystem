package accounts;

/**
 * OverdraftProtectionDecorator (Concrete Decorator)
 * ---------------------------------------------------
 * Adds a penalty fee if the decorated account enters a negative balance state
 * after a withdrawal. This assumes the decoratedAccount (e.g., CheckingAccount)
 * already handles the overdraft mechanism.
 */
public class OverdraftProtectionDecorator extends AccountDecorator {
    private double feeAmount;

    public OverdraftProtectionDecorator(AccountComponent decoratedAccount, double feeAmount) {
        super(decoratedAccount);
        this.feeAmount = feeAmount;
        System.out.println("🛡️ Added Overdraft Penalty Fee (" + feeAmount + ") to account " + decoratedAccount.getAccountNumber());
    }

    // سنركز على تزيين عملية السحب
    @Override
    public void withdraw(double amount) {
        double balanceBefore = decoratedAccount.getBalance();

        // 1. تفويض عملية السحب إلى المكون المغلف (Account, CheckingAccount, etc.)
        super.withdraw(amount);

        double balanceAfter = decoratedAccount.getBalance();

        // 2. التحقق من التغيير في الرصيد (منطق التزيين)
        // إذا كان الرصيد قبل السحب إيجابياً، وأصبح سلبياً بعد السحب:
        if (balanceBefore >= 0 && balanceAfter < 0) {

            System.out.println("⚠️ Overdraft Protection Triggered! Applying penalty fee of " + feeAmount);

            // 3. خصم الرسوم عن طريق استدعاء .withdraw() مرة أخرى على المبلغ
            // يجب أن نستخدم طريقة لا تتحقق من الرصيد أو نفترض أن الحساب يدعم ذلك.

            // **الأفضل:** نستخدم عملية إيداع سالبة إذا كان مسموحاً (لكن deposit في Account لا تسمح بالسالب)
            // **الحل العملي الوحيد (في هذا القيد):** نعتمد على أن الحساب الأساسي الآن هو CheckingAccount
            // ونقوم بالسحب منه مرة أخرى.

            // بما أننا لا نستطيع استخدام adjustBalance، سنعتمد على أن الحساب الأساسي (CheckingAccount)
            // سيسمح بسحب الرسوم ما دام ضمن حد المكشوف الخاص به.

            // نحتاج للتأكد من أننا نغلف CheckingAccount.
            if (decoratedAccount instanceof CheckingAccount) {
                System.out.println("💰 Applying Overdraft Fee: " + feeAmount);
                super.withdraw(feeAmount); // سحب الرسوم
            } else {
                System.out.println("❌ Cannot apply fee: Decorated account does not support necessary overdraft logic.");
            }
        }
    }
}