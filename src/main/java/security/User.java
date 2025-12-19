package security;

public class User {
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
    
    // للتبسيط في هذا المشروع، سنتحقق من كلمة المرور نصياً
    public boolean authenticate(String pass) {
        return this.password.equals(pass);
    }
}