
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import accounts.AccountComponent;
import accounts.CheckingAccount;
import accounts.OverdraftProtectionDecorator;

class DecoratorTest {

    @Test
    void testOverdraftProtection_ShouldApplyFee_WhenBalanceGoesNegative() {
        // 1. الإعداد: حساب جاري برصيد 100
        AccountComponent checkingAccount = new CheckingAccount("CHK-DEC-1", "User 1", 100, 500);

        // 2. التزيين: أضف ميزة الحماية من السحب على المكشوف مع رسوم 25
        AccountComponent decoratedAccount = new OverdraftProtectionDecorator(checkingAccount, 25);

        // 3. التنفيذ: اسحب 200 (100 من الرصيد و 100 من حد السحب على المكشوف)
        decoratedAccount.withdraw(200);

        // 4. التحقق: الرصيد يجب أن يكون -125
        // (100 الرصيد - 200 السحب = -100) ثم (-100 - 25 الرسوم = -125)
        assertEquals(-125.0, decoratedAccount.getBalance(), "يجب تطبيق رسوم السحب على المكشوف");
    }

    @Test
    void testOverdraftProtection_ShouldNotApplyFee_IfNotOverdraft() {
        // 1. الإعداد: حساب جاري برصيد 300
        AccountComponent checkingAccount = new CheckingAccount("CHK-DEC-2", "User 2", 300, 500);
        AccountComponent decoratedAccount = new OverdraftProtectionDecorator(checkingAccount, 25);

        // 2. التنفيذ: اسحب 200 (لا يوجد سحب على المكشوف)
        decoratedAccount.withdraw(200);

        // 3. التحقق: الرصيد يجب أن يكون 100 فقط، بدون أي رسوم
        assertEquals(100.0, decoratedAccount.getBalance(), "يجب ألا يتم تطبيق الرسوم إذا لم يكن هناك سحب على المكشوف");
    }
}
