package notifications;

public class SMSNotifier implements NotificationObserver {
    private String phoneNumber;

    public SMSNotifier(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(String message) {
        System.out.println("📱 [SMS Sent] to " + phoneNumber + ": " + message);
    }
}