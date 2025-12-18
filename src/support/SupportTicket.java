package support;

/**
 * يمثل تذكرة دعم فني أو شكوى من العميل.
 */
public class SupportTicket {
    private static int nextId = 1;
    private int ticketId;
    private String accountNumber;
    private String subject;
    private String message;
    private String status; // PENDING, RESOLVED

    public SupportTicket(String accountNumber, String subject, String message) {
        this.ticketId = nextId++;
        this.accountNumber = accountNumber;
        this.subject = subject;
        this.message = message;
        this.status = "PENDING";
    }

    public int getTicketId() { return ticketId; }
    public String getStatus() { return status; }
    public void resolve() { this.status = "RESOLVED"; }

    @Override
    public String toString() {
        return String.format("[Ticket #%d] Acc: %s | Subject: %s | Status: %s", 
                             ticketId, accountNumber, subject, status);
    }
}