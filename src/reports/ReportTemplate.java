package reports;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Behavioral Pattern: Template Method
 * يحدد الهيكل العام للتقرير (ترويسة، محتوى، خاتمة)
 * ويترك التفاصيل للكلاسات الوارثة.
 */
public abstract class ReportTemplate {

    // الـ Template Method: نهائية لا يمكن تغيير ترتيبها
    public final void generateReport() {
        printHeader();
        printBody();
        printFooter();
    }

    private void printHeader() {
        System.out.println("\n================================================");
        System.out.println("🏦 BANKING SYSTEM - OFFICIAL REPORT");
        System.out.println("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("------------------------------------------------");
    }

    // هذه الدالة ستختلف من تقرير لآخر
    protected abstract void printBody();

    private void printFooter() {
        System.out.println("------------------------------------------------");
        System.out.println("CONFIDENTIAL - End of Report");
        System.out.println("================================================\n");
    }
}