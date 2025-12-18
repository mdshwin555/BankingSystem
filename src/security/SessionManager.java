package security;

/**
 * Singleton Pattern: SessionManager
 * يتتبع المستخدم الحالي المسجل دخوله في النظام.
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
        System.out.println("👤 User @" + user.getUsername() + " logged in as " + user.getRole());
    }

    public void logout() {
        this.currentUser = null;
        System.out.println("🔒 Logged out.");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}