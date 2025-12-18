package support;

import java.util.ArrayList;
import java.util.List;

public class SupportManager {
    private static SupportManager instance;
    private List<SupportTicket> tickets = new ArrayList<>();

    private SupportManager() {}

    public static synchronized SupportManager getInstance() {
        if (instance == null) {
            instance = new SupportManager();
        }
        return instance;
    }

    public void addTicket(SupportTicket ticket) {
        tickets.add(ticket);
    }

    public List<SupportTicket> getAllTickets() {
        return tickets;
    }
}