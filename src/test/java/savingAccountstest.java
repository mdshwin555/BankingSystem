

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import accounts.SavingsAccount;
// استيراد الواجهة والاستراتيجيات المختلفة من حزمة interest
import interest.CompoundInterestStrategy;
import interest.SimpleInterestStrategy;

class SavingsAccountTest {

    @Test
    void testAddInterest_WithSimpleInterestStrategy() {
        // 1. الإعداد (Arrange): حساب توفير باستراتيجية فائدة بسيطة
        SavingsAccount account = new SavingsAccount("SAV1", "User Simple", 1000.0, 5.0, new SimpleInterestStrategy());
        
        // 2. التنفيذ (Act): قم بإضافة الفائدة
        account.addInterest();
        
        // 3. التحقق (Assert): الفائدة البسيطة هي 5% من 1000 = 50. الرصيد الجديد يجب أن يكون 1050
        assertEquals(1050.0, account.getBalance(), "يجب حساب الفائدة البسيطة بشكل صحيح");
    }

    @Test
    void testAddInterest_WithCompoundInterestStrategy() {
        // 1. الإعداد (Arrange): حساب توفير باستراتيجية فائدة مركبة
        SavingsAccount account = new SavingsAccount("SAV2", "User Compound", 1000.0, 5.0, new CompoundInterestStrategy());
        
        // 2. التنفيذ (Act): قم بإضافة الفائدة
        account.addInterest();
        
        // 3. التحقق (Assert): الفائدة المركبة (لفترة واحدة) هي نفسها البسيطة. 1000 + 50 = 1050
        assertEquals(1050.0, account.getBalance(), "يجب حساب الفائدة المركبة بشكل صحيح");
    }

    @Test
    void testSwitchingInterestStrategy_AtRuntime() {
        // 1. الإعداد (Arrange): نبدأ بحساب ذي فائدة بسيطة
        SavingsAccount account = new SavingsAccount("SAV3", "User Switcher", 1000.0, 10.0, new SimpleInterestStrategy());
        
        // 2. التنفيذ (Act) - المرحلة الأولى: تطبيق الفائدة البسيطة
        account.addInterest();
        assertEquals(1100.0, account.getBalance(), "الرصيد بعد الفائدة البسيطة يجب أن يكون 1100");

        // 3. التنفيذ (Act) - المرحلة الثانية: تبديل الاستراتيجية إلى مركبة وتطبيقها مرة أخرى
        account.setInterestStrategy(new CompoundInterestStrategy());
        account.addInterest(); // الآن سيتم تطبيق فائدة 10% على الرصيد الجديد (1100)
        
        // 4. التحقق (Assert): الفائدة الجديدة هي 10% من 1100 = 110. الرصيد النهائي = 1100 + 110 = 1210
        assertEquals(1210.0, account.getBalance(), "يجب أن يتم تحديث الرصيد بشكل صحيح بعد تبديل الاستراتيجية");
    }
}
