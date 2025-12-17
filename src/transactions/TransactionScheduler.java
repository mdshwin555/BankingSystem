package transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionScheduler {
    private List<TransactionCommand> pendingTransactions = new ArrayList<>();

    // إضافة عملية للجدولة
    public void scheduleTransaction(TransactionCommand command) {
        pendingTransactions.add(command);
        System.out.println("📅 Transaction scheduled successfully.");
    }

    // تنفيذ جميع العمليات المجدولة (محاكاة لمرور الوقت أو دفعات نهاية الشهر)
    public void runScheduledTransactions() {
        System.out.println("\n🚀 [Scheduler] Running all pending transactions...");
        for (TransactionCommand command : pendingTransactions) {
            command.execute();
        }
        pendingTransactions.clear(); // تنظيف القائمة بعد التنفيذ
    }
}