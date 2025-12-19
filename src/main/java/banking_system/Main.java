package banking_system;

import security.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("🚀 [SYSTEM START] Advanced Banking System - Final Functional Build");
        System.out.println("==========================================================\n");

        BankFacade bank = new BankFacade();
        SessionManager session = SessionManager.getInstance();

        // -----------------------------------------------------------------------
        // المرحلة 1: تسجيل دخول المدير لتهيئة النظام (MANAGER ROLE)
        // -----------------------------------------------------------------------
        System.out.println("--- 🟢 PHASE 1: Manager Operations (Account Creation) ---");
        
        User manager = new User("Samer_Manager", "admin123", UserRole.MANAGER);
        session.login(manager);

        // حسابات برصيد عالي لتجربة كل الميزات
        bank.createAccount("savings", "SAV-101", "Ahmad", 15000.0);
        bank.createAccount("checking", "CHK-202", "Sarah", 5000.0);
        bank.createAccount("loan", "LON-303", "Khaled", 10000.0);

        System.out.println("\n📊 [Report] Manager generating system status:");
        bank.generateSummaryReport();
        
        session.logout();

        // -----------------------------------------------------------------------
        // المرحلة 2: تجربة العميل (CUSTOMER ROLE) - العملات، المستفيدين، التذاكر، التوصيات
        // -----------------------------------------------------------------------
        System.out.println("\n--- 🔵 PHASE 2: Customer Ahmad testing Features ---");
        
        User customer = new User("Ahmad_User", "pass123", UserRole.CUSTOMER);
        session.login(customer);

        // 2.1 إيداع عملات أجنبية (Currency Conversion)
        System.out.println("\n[Test 2.2] Foreign Currency Deposit:");
        bank.depositForeignCurrency("SAV-101", 100.0, "EUR");

        // 2.2 إدارة المستفيدين (Beneficiary Management)
        System.out.println("\n[Test 2.3] Beneficiary Management:");
        bank.addBeneficiary("SAV-101", "Sarah", "CHK-202", "Sister");
        bank.printBeneficiaries("SAV-101");

        // 2.3 فتح تذكرة دعم (Support Tickets)
        System.out.println("\n[Test 2.4] Support Ticket System:");
        bank.openSupportTicket("SAV-101", "Transaction Delay", "My deposit from EUR is taking too long to reflect.");

        // 2.4 التوصيات المصرفية الذكية (Smart Recommendations)
        System.out.println("\n[Test 2.5] Personal Banking Recommendations:");
        bank.getMyBankingAdvice("SAV-101");
        
        session.logout();

        // -----------------------------------------------------------------------
        // المرحلة 3: اختبار الأمان وصلاحيات الوصول (Security Violation Test)
        // -----------------------------------------------------------------------
        System.out.println("\n--- 🚫 PHASE 3: Security & Violation Test ---");
        session.login(customer);
        
        System.out.println("[Violation Test] Customer trying to access Admin Dashboard (Should fail):");
        bank.showAdminDashboard();

        System.out.println("\n[Violation Test] Customer trying to resolve tickets (Should fail):");
        bank.resolveSupportTicket(1);
        
        session.logout();

        // -----------------------------------------------------------------------
        // المرحلة 4: المسؤول الأعلى - الإحصائيات الشاملة وحل المشاكل (ADMIN ROLE)
        // -----------------------------------------------------------------------
        System.out.println("\n--- 🔴 PHASE 4: Admin Operations ---");
        
        User admin = new User("The_Admin", "root", UserRole.ADMIN);
        session.login(admin);

        // ✨ [Test 4.1] لوحة التحكم الإدارية (Dashboard & Monitoring)
        System.out.println("\n[Test 4.1] Admin Dashboard (System Liquidity & Activity):");
        bank.showAdminDashboard();

        // [Test 4.2] استعراض وحل التذاكر
        System.out.println("\n[Test 4.2] Support Ticket Resolution:");
        bank.viewAllTickets();
        bank.resolveSupportTicket(1); 
        bank.viewAllTickets();

        // [Test 4.3] إغلاق حساب
        System.out.println("\n[Test 4.3] Closing settled account:");
        bank.closeAccount("CHK-202");

        // [Test 4.4] التقرير النهائي
        System.out.println("\n[Test 4.4] Final System Audit Report:");
        bank.generateSummaryReport();

        session.logout();

        System.out.println("\n==========================================================");
        System.out.println("🏁 [SYSTEM SHUTDOWN] All functional features and security tests passed.");
        System.out.println("==========================================================");
    }
}