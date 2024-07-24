public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }

    public void createStaff(String staffID, String password, AirlineSystem system) {
        system.addStaff(new Staff(staffID, password));
    }
}
