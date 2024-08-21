public class Admin extends User {
    private String role;

    public Admin(String username, String password, String role) {
        super(username, password, role);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
