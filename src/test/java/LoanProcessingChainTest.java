
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import loan_processing.CreditCommittee;
import loan_processing.JuniorLoanOfficer;
import loan_processing.LoanApplication;
import loan_processing.LoanApprovalHandler;
import loan_processing.SeniorLoanOfficer;

class LoanProcessingChainTest {

    private LoanApprovalHandler loanChain;

    @BeforeEach
    void setUp() {
        // 1. بناء السلسلة الكاملة
        loanChain = new JuniorLoanOfficer();
        LoanApprovalHandler seniorOfficer = new SeniorLoanOfficer();
        LoanApprovalHandler committee = new CreditCommittee();
        
        loanChain.setNext(seniorOfficer);
        seniorOfficer.setNext(committee);
    }

    @Test
    void testLoan_ShouldBeApprovedBy_JuniorOfficer() {
        // قرض صغير، يجب أن يوافق عليه الموظف المبتدئ
        LoanApplication app = new LoanApplication("Applicant 1", 5000, 750, 5000);
        loanChain.processRequest(app);
        assertEquals("APPROVED by Junior Loan Officer", app.getStatus());
    }

    @Test
    void testLoan_ShouldBeApprovedBy_SeniorOfficer() {
        // قرض متوسط، يجب أن يتم تمريره إلى الموظف الخبير
        LoanApplication app = new LoanApplication("Applicant 2", 25000, 680, 8000);
        loanChain.processRequest(app);
        assertEquals("APPROVED by Senior Loan Officer", app.getStatus());
    }

    @Test
    void testLoan_ShouldBeApprovedBy_CreditCommittee() {
        // قرض كبير، يجب أن يتم تمريره إلى لجنة الائتمان
        LoanApplication app = new LoanApplication("Applicant 3", 150000, 620, 15000);
        loanChain.processRequest(app);
        assertEquals("APPROVED by Credit Committee", app.getStatus());
    }

    @Test
    void testLoan_ShouldBeRejectedBy_CreditCommittee() {
        // قرض ضخم جداً ودرجة ائتمان منخفضة، يجب أن ترفضه اللجنة
        LoanApplication app = new LoanApplication("Applicant 4", 300000, 580, 20000);
        loanChain.processRequest(app);
        assertEquals("REJECTED by Credit Committee", app.getStatus());
    }
}
