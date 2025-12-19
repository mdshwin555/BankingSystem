


// هذه المكتبات يجب أن يتم استيرادها الآن بدون أي أخطاء
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import accounts.Account;

import java.util.List;

class AccountTest {

    private Account account;

    // هذه الدالة التي تحمل anootation @BeforeEach تعمل مرة واحدة قبل كل اختبار
    // هذا يضمن أن كل اختبار يبدأ بحساب نظيف ومعزول
    @BeforeEach
    void setUp() {
        account = new Account("ACC123", "Test User", 1000.0);
    }

    @Test
    void testDeposit_WithPositiveAmount_ShouldIncreaseBalance() {
        // التنفيذ (Act)
        account.deposit(500.0);
        // التحقق (Assert)
        assertEquals(1500.0, account.getBalance(), "الرصيد يجب أن يزيد بعد الإيداع");
    }

    @Test
    void testWithdraw_WithSufficientBalance_ShouldDecreaseBalance() {
        // التنفيذ (Act)
        account.withdraw(300.0);
        // التحقق (Assert)
        assertEquals(700.0, account.getBalance(), "الرصيد يجب أن ينقص بعد السحب");
    }

    @Test
    void testWithdraw_WithInsufficientBalance_ShouldNotChangeBalance() {
        // التنفيذ (Act)
        account.withdraw(1200.0); // محاولة سحب أكثر من الرصيد
        // التحقق (Assert)
        assertEquals(1000.0, account.getBalance(), "الرصيد يجب ألا يتغير عند عدم كفاية المبلغ");
    }

    @Test
    void testTransactionHistory_ShouldLogAllTransactions() {
        // التنفيذ (Act)
        account.deposit(200);
        account.withdraw(100);
        List<String> history = account.transactionHistory;
        
        // التحقق (Assert)
        // نتوقع 3 عمليات: الرصيد الأولي، الإيداع، السحب
        assertEquals(3, history.size());
        assertTrue(history.get(1).contains("Deposit: +200.0"), "سجل الإيداع غير صحيح");
        assertTrue(history.get(2).contains("Withdraw: -100.0"), "سجل السحب غير صحيح");
    }
}
