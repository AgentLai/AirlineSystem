import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import java.util.*;
import java.time.LocalDate;

public class AirlineSystem extends Application {
    private static Graph graph = new Graph();
    private static User loggedInUser = null;
    private static List<Staff> staffList = new ArrayList<>();
    private static List<Admin> adminList = new ArrayList<>();
    private Map<String, double[]> positions = new HashMap<>();
    private Map<String, double[]> savedPositions = new HashMap<>();
    private Canvas canvas;
    private Stage mainStage;
    private Label lblCurrentUser;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLoginLogout;
    private VBox loginBox;
    private boolean airportMovementEnabled = false;


    public static void main(String[] args) {
        initializeGraph();
        initializeUsers();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
	    mainStage = primaryStage;
	
	    // Set an explicit initial window size
	    mainStage.setWidth(1000);
	    mainStage.setHeight(800);
	
	    canvas = new Canvas(800, 600);
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    viewGraph(gc);
	    showMainMenu(gc);
	
	    // Make the canvas responsive
	    canvas.widthProperty().bind(mainStage.widthProperty().subtract(200));  // Adjust based on right VBox
	    canvas.heightProperty().bind(mainStage.heightProperty().subtract(100)); // Adjust based on top HBox
	
	    canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
	        viewGraph(gc);
	    });
	
	    canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
	        viewGraph(gc);
	    });
	
	    // Ensure the stage is not resizable to fullscreen on launch
	    mainStage.setResizable(true);
	    mainStage.setMaximized(false);  // Prevents the stage from launching maximized
	
	    // Display the stage
	    mainStage.show();
	}

	private void showMainMenu(GraphicsContext gc) {
	    mainStage.setTitle("Airline System");
	
	    Label lblStart = new Label("Start Airport Code:");
	    TextField txtStart = new TextField();
	    Label lblEnd = new Label("End Airport Code:");
	    TextField txtEnd = new TextField();
	
	    Button btnAlgorithms = new Button("Algorithms");
	    Button btnEditor = new Button("Editor");
	    Button btnAirportInfo = new Button("Airport Info");
	    btnLoginLogout = new Button(loggedInUser == null ? "Login" : "Logout");
	    lblCurrentUser = new Label(getCurrentUserText());
	
	    // Create Profile button only if the user is logged in as Staff
	    Button btnProfile = null;
	    if (loggedInUser != null && "Staff".equals(loggedInUser.getRole())) {
	        btnProfile = new Button("Profile");
	        VBox.setMargin(btnProfile, new Insets(5, 0, 5, 0));
	        btnProfile.setOnAction(e -> showProfilePage());
	    }
	
	    HBox inputBox = new HBox(10, lblStart, txtStart, lblEnd, txtEnd);
	
	    // Create and style the login box
	    createLoginBox();
	    VBox loginContainer = new VBox(10, loginBox);
	    if (btnProfile != null) {
	        loginContainer.getChildren().add(btnProfile);
	    }
	    loginContainer.getChildren().add(btnLoginLogout); // Add logout button after profile button, if present
	    loginContainer.setPadding(new Insets(10));
	    loginContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
	
	    // Create and style the button box
	    VBox buttonBox = new VBox(10, btnAlgorithms, btnEditor, btnAirportInfo);
	
	    if (loggedInUser != null && "Admin".equals(loggedInUser.getRole())) {
	        Button btnStaff = new Button("Staff Management");
	        VBox.setMargin(btnStaff, new Insets(5, 0, 5, 0));
	        btnStaff.setOnAction(e -> showStaffPage());
	        buttonBox.getChildren().add(btnStaff);
	    }
	
	    buttonBox.getChildren().add(lblCurrentUser);
	    buttonBox.setPadding(new Insets(10));
	    buttonBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
	
	    VBox rightContainer = new VBox(20, loginContainer, buttonBox);
	    rightContainer.setPadding(new Insets(10));
	
	    BorderPane root = new BorderPane();
	    root.setTop(inputBox);
	    root.setCenter(canvas);
	    root.setRight(rightContainer);
	
	    Scene scene = new Scene(root, 1000, 800);
	    mainStage.setScene(scene);
	
	    btnAlgorithms.setOnAction(e -> showAlgorithmsMenu(gc, txtStart, txtEnd));
	    btnEditor.setOnAction(e -> {
	        if (loggedInUser != null && ("Staff".equals(loggedInUser.getRole()) || "Admin".equals(loggedInUser.getRole()))) {
	            showEditorMenu(gc);
	        } else {
	            showAlert("Access Denied", "You must be logged in as a Staff or Admin to access the editor.");
	        }
	    });
	    btnAirportInfo.setOnAction(e -> showAirportInfo());
	    btnLoginLogout.setOnAction(e -> {
	        if (loggedInUser == null) {
	            loginUser();
	        } else {
	            logoutUser(gc);
	        }
	    });
	}

    
	private void showStaffPage() {
	    if (loggedInUser == null || !"Admin".equals(loggedInUser.getRole())) {
	        showAlert("Access Denied", "You must be an Admin to access the Staff Management page.");
	        return;
	    }
	
	    Stage staffStage = new Stage();
	    staffStage.setTitle("Staff Management");
	
	    Button btnCheckStaff = new Button("Check staff list");
	    btnCheckStaff.setOnAction(e -> checkStaffList());
	
	    Button btnCreateStaff = new Button("Create new staff");
	    btnCreateStaff.setOnAction(e -> createNewStaff());
	
	    Button btnDeleteStaff = new Button("Delete old staff");
	    btnDeleteStaff.setOnAction(e -> deleteOldStaff());
	
	    Button btnResetPassword = new Button("Reset password for staff");
	    btnResetPassword.setOnAction(e -> resetStaffPassword());
	
	    VBox vbox = new VBox(10, btnCheckStaff, btnCreateStaff, btnDeleteStaff, btnResetPassword);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 200);
	    staffStage.setScene(scene);
	    staffStage.show();
	}
	
	private void checkStaffList() {
	    Stage listStage = new Stage();
	    listStage.setTitle("Staff List");
	
	    ListView<String> staffListView = new ListView<>();
	    for (Staff staff : staffList) {
	        staffListView.getItems().add(staff.getUsername());
	    }
	
	    VBox vbox = new VBox(staffListView);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 400);
	    listStage.setScene(scene);
	    listStage.show();
	}

	private void createNewStaff() {
	    Stage createStage = new Stage();
	    createStage.setTitle("Create New Staff");
	
	    Label lblUsername = new Label("Enter Username:");
	    TextField txtUsername = new TextField();
	    Label lblPassword = new Label("Enter Password:");
	    TextField txtPassword = new TextField();
	    Label lblAge = new Label("Enter Age (Optional):");
	    TextField txtAge = new TextField();
	    Label lblGender = new Label("Select Gender (Optional):");
	    ComboBox<String> cmbGender = new ComboBox<>();
	    cmbGender.getItems().addAll("Male", "Female", "Other");
	    cmbGender.setValue("Other");
	    Label lblEmail = new Label("Enter Email (Optional):");
	    TextField txtEmail = new TextField();
	    Label lblPosition = new Label("Select Position (Optional):");
	    ComboBox<String> cmbPosition = new ComboBox<>();
	    cmbPosition.getItems().addAll("Sales Associate", "Cashier", "Warehouse Manager", "HR Specialist", "Marketing Manager", "Operations Manager", "IT Support", "Customer Service Representative");
	    cmbPosition.setValue("Customer Service Representative"); // Default value
	    Label lblSalary = new Label("Enter Salary (Optional):");
	    TextField txtSalary = new TextField();
	    Label lblWorkedSince = new Label("Worked Since (Optional):");
	    TextField txtWorkedSince = new TextField(LocalDate.now().getMonth().toString() + " " + LocalDate.now().getYear());
	
	    Button btnCreate = new Button("Create");
	
	    btnCreate.setOnAction(e -> {
	        String username = txtUsername.getText().trim();
	        String password = txtPassword.getText().trim();
	        if (!username.isEmpty() && !password.isEmpty()) {
	            Staff newStaff = new Staff(username, password);
	            if (!txtAge.getText().isEmpty()) newStaff.setAge(Integer.parseInt(txtAge.getText().trim()));
	            newStaff.setGender(cmbGender.getValue());
	            if (!txtEmail.getText().isEmpty()) newStaff.setEmail(txtEmail.getText().trim());
	            newStaff.setPosition(cmbPosition.getValue());
	            if (!txtSalary.getText().isEmpty()) newStaff.setSalary(Double.parseDouble(txtSalary.getText().trim()));
	            newStaff.setWorkedSince(txtWorkedSince.getText().trim());
	            staffList.add(newStaff);
	            showAlert("Create New Staff", "Staff member " + username + " created successfully.");
	            createStage.close();
	        } else {
	            showAlert("Error", "Username and password are mandatory.");
	        }
	    });
	
	    VBox vbox = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, lblAge, txtAge, lblGender, cmbGender, lblEmail, txtEmail, lblPosition, cmbPosition, lblSalary, txtSalary, lblWorkedSince, txtWorkedSince, btnCreate);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 400, 500);
	    createStage.setScene(scene);
	    createStage.show();
	}
	
	private void changeStaffDetails() {
	    Stage changeStage = new Stage();
	    changeStage.setTitle("Change Staff Details");
	
	    Label lblUsername = new Label("Enter Username:");
	    TextField txtUsername = new TextField();
	    Label lblSalary = new Label("New Salary:");
	    TextField txtSalary = new TextField();
	    Label lblPosition = new Label("New Position:");
	    ComboBox<String> cmbPosition = new ComboBox<>();
	    cmbPosition.getItems().addAll("Sales Associate", "Cashier", "Warehouse Manager", "HR Specialist", "Marketing Manager", "Operations Manager", "IT Support", "Customer Service Representative");
	
	    Button btnChange = new Button("Change");
	
	    btnChange.setOnAction(e -> {
	        String username = txtUsername.getText().trim();
	        Staff staff = findStaffByUsername(username);
	        if (staff == null) {
	            showAlert("Error", "Staff member not found.");
	            return;
	        }
	        if (!txtSalary.getText().isEmpty()) staff.setSalary(Double.parseDouble(txtSalary.getText().trim()));
	        staff.setPosition(cmbPosition.getValue());
	        showAlert("Change Staff Details", "Details updated successfully.");
	        changeStage.close();
	    });
	
	    VBox vbox = new VBox(10, lblUsername, txtUsername, lblSalary, txtSalary, lblPosition, cmbPosition, btnChange);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 300);
	    changeStage.setScene(scene);
	    changeStage.show();
	}
	
	private Staff findStaffByUsername(String username) {
	    for (Staff staff : staffList) {
	        if (staff.getUsername().equals(username)) {
	            return staff;
	        }
	    }
	    return null;
	}
	
	private void addNewStaff(String username, String password) {
	    Staff newStaff = new Staff(username, password);
	    staffList.add(newStaff);
	}

	private void deleteOldStaff() {
	    Stage deleteStage = new Stage();
	    deleteStage.setTitle("Delete Staff");
	
	    Label lblUsername = new Label("Enter Username:");
	    TextField txtUsername = new TextField();
	    Button btnDelete = new Button("Delete");
	
	    btnDelete.setOnAction(e -> {
	        String username = txtUsername.getText().trim();
	        if (!username.isEmpty()) {
	            deleteStaff(username);
	            showAlert("Delete Staff", "Staff member " + username + " deleted successfully.");
	            deleteStage.close();
	        } else {
	            showAlert("Error", "Username cannot be empty.");
	        }
	    });
	
	    VBox vbox = new VBox(10, lblUsername, txtUsername, btnDelete);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 150);
	    deleteStage.setScene(scene);
	    deleteStage.show();
	}
	
	private void deleteStaff(String username) {
	    staffList.removeIf(staff -> staff.getUsername().equals(username));
	}

	private void resetStaffPassword() {
	    Stage resetStage = new Stage();
	    resetStage.setTitle("Reset Staff Password");
	
	    Label lblUsername = new Label("Enter Staff Username:");
	    TextField txtUsername = new TextField();
	    Button btnReset = new Button("Reset Password");
	
	    btnReset.setOnAction(e -> {
	        String username = txtUsername.getText().trim();
	        if (!username.isEmpty()) {
	            resetPasswordForStaff(username);
	            showAlert("Reset Password", "Password for " + username + " has been reset to 'Staff123'.");
	            resetStage.close();
	        } else {
	            showAlert("Error", "Username cannot be empty.");
	        }
	    });
	
	    VBox vbox = new VBox(10, lblUsername, txtUsername, btnReset);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 150);
	    resetStage.setScene(scene);
	    resetStage.show();
	}
	
	private void resetPasswordForStaff(String username) {
	    for (Staff staff : staffList) {
	        if (staff.getUsername().equals(username)) {
	            staff.setPassword("Staff123");
	            break;
	        }
	    }
	}
	
	private void showProfilePage() {
	    if (loggedInUser == null || !"Staff".equals(loggedInUser.getRole())) {
	        showAlert("Access Denied", "You must be logged in as a Staff to view the profile.");
	        return;
	    }
	
	    Staff staff = (Staff) loggedInUser; // Cast loggedInUser to Staff
	
	    Stage profileStage = new Stage();
	    profileStage.setTitle("Profile");
	
	    // Profile details
	    Label lblName = new Label("Name: " + staff.getUsername());
	    Label lblAge = new Label("Age: " + staff.getAge());
	    Label lblGender = new Label("Gender: " + staff.getGender());
	    Label lblEmail = new Label("Email: " + staff.getEmail());
	    Label lblPosition = new Label("Position: " + staff.getRole());
	    Label lblSalary = new Label("Salary: $" + staff.getSalary());
	    Label lblWorkedSince = new Label("Worked since: " + staff.getWorkedSince());
	
	    // Buttons for profile actions
	    Button btnReturnToMenu = new Button("Return to main menu");
	    btnReturnToMenu.setOnAction(e -> profileStage.close());
	
	    Button btnEditProfile = new Button("Edit profile");
	    btnEditProfile.setOnAction(e -> editProfile());
	
	    Button btnChangePassword = new Button("Change password");
	    btnChangePassword.setOnAction(e -> changePassword());
	
	    VBox vbox = new VBox(10, lblName, lblAge, lblGender, lblEmail, lblPosition, lblSalary, lblWorkedSince, 
	                         btnReturnToMenu, btnEditProfile, btnChangePassword);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 400);
	    profileStage.setScene(scene);
	    profileStage.show();
	}

	private void editProfile() {
	    if (loggedInUser == null || !"Staff".equals(loggedInUser.getRole())) {
	        showAlert("Access Denied", "You must be logged in as a Staff to edit the profile.");
	        return;
	    }
	
	    Staff staff = (Staff) loggedInUser; // Correctly cast loggedInUser to Staff
	
	    Stage editStage = new Stage();
	    editStage.setTitle("Edit Profile");
	
	    TextField txtName = new TextField(staff.getUsername());
	    TextField txtAge = new TextField(String.valueOf(staff.getAge()));
	    
	    ComboBox<String> cmbGender = new ComboBox<>();
	    cmbGender.getItems().addAll("Male", "Female", "Other");
	    cmbGender.setValue(staff.getGender()); // Use correctly initialized staff reference
	    
	    TextField txtEmail = new TextField(staff.getEmail());
	
	    Button btnSave = new Button("Save");
	    btnSave.setOnAction(e -> {
	        staff.setUsername(txtName.getText().trim());
	        staff.setAge(Integer.parseInt(txtAge.getText().trim()));
	        staff.setGender(cmbGender.getValue());
	        staff.setEmail(txtEmail.getText().trim());
	        showAlert("Edit Profile", "Profile updated successfully.");
	        editStage.close();
	    });
	
	    VBox vbox = new VBox(10, new Label("Name:"), txtName, new Label("Age:"), txtAge,
	                         new Label("Gender:"), cmbGender, new Label("Email:"), txtEmail, btnSave);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 300);
	    editStage.setScene(scene);
	    editStage.show();
	}


	private void changePassword() {
	    if (loggedInUser == null || !"Staff".equals(loggedInUser.getRole())) {
	        showAlert("Access Denied", "You must be logged in as a Staff to change the password.");
	        return;
	    }
	
	    Staff staff = (Staff) loggedInUser; // Cast loggedInUser to Staff
	
	    Stage passwordStage = new Stage();
	    passwordStage.setTitle("Change Password");
	
	    PasswordField txtOldPassword = new PasswordField();
	    txtOldPassword.setPromptText("Enter old password");
	    PasswordField txtNewPassword = new PasswordField();
	    txtNewPassword.setPromptText("Enter new password");
	    PasswordField txtConfirmPassword = new PasswordField();
	    txtConfirmPassword.setPromptText("Confirm new password");
	
	    Button btnChange = new Button("Change Password");
	    btnChange.setOnAction(e -> {
	        String oldPassword = txtOldPassword.getText();
	        String newPassword = txtNewPassword.getText();
	        String confirmPassword = txtConfirmPassword.getText();
	
	        if (!staff.getPassword().equals(oldPassword)) {
	            showAlert("Change Password", "Old password is incorrect.");
	        } else if (!newPassword.equals(confirmPassword)) {
	            showAlert("Change Password", "New passwords do not match.");
	        } else {
	            staff.setPassword(newPassword);
	            showAlert("Change Password", "Password changed successfully.");
	            passwordStage.close();
	        }
	    });
	
	    VBox vbox = new VBox(10, txtOldPassword, txtNewPassword, txtConfirmPassword, btnChange);
	    vbox.setPadding(new Insets(10));
	
	    Scene scene = new Scene(vbox, 300, 200);
	    passwordStage.setScene(scene);
	    passwordStage.show();
	}

    private void createLoginBox() {
        txtUsername = new TextField();
        txtPassword = new PasswordField();

        Label lblUsername = new Label("Username:");
        Label lblPassword = new Label("Password:");

        if (loggedInUser != null) {
            lblUsername.setText("Logged in as: " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
            loginBox = new VBox(10, lblUsername, btnLoginLogout);
        } else {
            loginBox = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, btnLoginLogout);
        }

        loginBox.setSpacing(10);
    }

    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        if (authenticate(username, password)) {
            showAlert("Login Successful", "Successfully logged in as " + loggedInUser.getUsername());
            lblCurrentUser.setText(getCurrentUserText());
            createLoginBox();  // Update the login box to reflect the logged-in state
            showMainMenu(canvas.getGraphicsContext2D()); // Refresh the main menu to show/hide buttons
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    private void logoutUser(GraphicsContext gc) {
        loggedInUser = null;
        showAlert("Logout Successful", "Successfully logged out.");
        lblCurrentUser.setText(getCurrentUserText());
        createLoginBox();  // Update the login box to reflect the logged-out state
        airportMovementEnabled = false;  // Disable airport movement
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        showMainMenu(gc);  // Refresh the main menu
    }

    private boolean authenticate(String username, String password) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                loggedInUser = admin;
                return true;
            }
        }
        for (Staff staff : staffList) {
            if (staff.getUsername().equals(username) && staff.getPassword().equals(password)) {
                loggedInUser = staff;
                return true;
            }
        }
        return false;
    }

    private void showAlgorithmsMenu(GraphicsContext gc, TextField txtStart, TextField txtEnd) {
        resetAirportHighlighting(gc);

        VBox dialogBox = new VBox(10);

        Button btnDFS = new Button("DFS Traversal");
        Button btnBFS = new Button("BFS Traversal");
        Button btnShortestPath = new Button("Find Shortest Path");

        btnDFS.setOnAction(e -> {
            String start = txtStart.getText().trim().toUpperCase();
            if (start.isEmpty()) {
                showAlert("DFS Traversal", "Please enter the start airport code.");
            } else {
                dfsTraversal(gc, start);
            }
        });

        btnBFS.setOnAction(e -> {
            String start = txtStart.getText().trim().toUpperCase();
            if (start.isEmpty()) {
                showAlert("BFS Traversal", "Please enter the start airport code.");
            } else {
                bfsTraversal(gc, start);
            }
        });

        btnShortestPath.setOnAction(e -> {
            String start = txtStart.getText().trim().toUpperCase();
            String end = txtEnd.getText().trim().toUpperCase();
            if (start.isEmpty() || end.isEmpty()) {
                showAlert("Find Shortest Path", "Please enter both start and end airport codes.");
            } else {
                findShortestPath(gc, start, end);
            }
        });

        dialogBox.getChildren().addAll(btnDFS, btnBFS, btnShortestPath);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Algorithms");
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void resetAirportHighlighting(GraphicsContext gc) {
        viewGraph(gc);
    }

    private void showEditorMenu(GraphicsContext gc) {
        mainStage.setTitle("Editor");

        Button btnAddAirport = new Button("Add Airport");
        Button btnDeleteAirport = new Button("Delete Airport");
        Button btnAddConnection = new Button("Add Connection");
        Button btnRemoveConnection = new Button("Remove Connection");
        Button btnMoveAirport = new Button("Move Airport");
        Button btnSaveChanges = new Button("Stop moving airports");
        Button btnReturn = new Button("Return to Main Menu");

        VBox editorBox = new VBox(10, btnAddAirport, btnAddConnection, btnMoveAirport, btnSaveChanges, btnDeleteAirport, btnRemoveConnection, btnReturn);

        BorderPane editorRoot = new BorderPane();
        editorRoot.setCenter(canvas);
        editorRoot.setRight(editorBox);

        Scene editorScene = new Scene(editorRoot, 1000, 800);
        mainStage.setScene(editorScene);

        btnAddAirport.setOnAction(e -> addAirport());
        btnDeleteAirport.setOnAction(e -> deleteAirport());
        btnAddConnection.setOnAction(e -> addConnection());
        btnRemoveConnection.setOnAction(e -> removeConnection());
        btnMoveAirport.setOnAction(e -> {
            airportMovementEnabled = true;
            savedPositions = new HashMap<>(positions); 
            enableAirportMovement(gc);
        });
        btnSaveChanges.setOnAction(e -> {
            airportMovementEnabled = false;
            disableAirportMovement(gc);
        });
        btnReturn.setOnAction(e -> {
            airportMovementEnabled = false;
            disableAirportMovement(gc);
            showMainMenu(gc);
        });

        if (loggedInUser != null && loggedInUser.getRole().equals("Staff")) {
            editorBox.getChildren().remove(btnDeleteAirport);
            editorBox.getChildren().remove(btnRemoveConnection);
        }
    }

    private void showAirportInfo() {
        Stage infoStage = new Stage();
        infoStage.setTitle("Airport Information");

        VBox airportInfoBox = new VBox(10);
        for (String code : positions.keySet()) {
            Label lblAirport = new Label(code + " - " + graph.getCityName(code));
            airportInfoBox.getChildren().add(lblAirport);
        }

        Scene infoScene = new Scene(airportInfoBox, 300, 400);
        infoStage.setScene(infoScene);
        infoStage.show();
    }

    private void addAirport() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add Airport");

        Label lblCode = new Label("Airport Code:");
        TextField txtCode = new TextField();
        Label lblName = new Label("Airport Name:");
        TextField txtName = new TextField();
        Button btnAdd = new Button("Add");

        btnAdd.setOnAction(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            String name = txtName.getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                showAlert("Add Airport", "Please enter both code and name.");
            } else if (graph.hasVertex(code)) {
                showAlert("Add Airport", "Airport code already exists.");
            } else {
                graph.addVertex(code, name);
                positions.put(code, new double[]{100, 100});  // Add default position
                showAlert("Add Airport", "Airport " + code + " (" + name + ") added.");
                viewGraph(canvas.getGraphicsContext2D()); // Refresh graph view
                dialogStage.close();
            }
        });

        VBox dialogBox = new VBox(10, lblCode, txtCode, lblName, txtName, btnAdd);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void deleteAirport() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Delete Airport");

        Label lblCode = new Label("Airport Code:");
        TextField txtCode = new TextField();
        Button btnDelete = new Button("Delete");

        btnDelete.setOnAction(e -> {
            String code = txtCode.getText().trim().toUpperCase();
            if (code.isEmpty()) {
                showAlert("Delete Airport", "Please enter the airport code.");
            } else if (!graph.hasVertex(code)) {
                showAlert("Delete Airport", "Airport code does not exist.");
            } else {
                graph.removeVertex(code);
                positions.remove(code);  // Remove position
                showAlert("Delete Airport", "Airport " + code + " removed.");
                viewGraph(canvas.getGraphicsContext2D()); // Refresh graph view
                dialogStage.close();
            }
        });

        VBox dialogBox = new VBox(10, lblCode, txtCode, btnDelete);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

	private void addConnection() {
	    Stage dialogStage = new Stage();
	    dialogStage.setTitle("Add Connection");
	
	    Label lblFrom = new Label("From Airport:");
	    TextField txtFrom = new TextField();
	    Label lblTo = new Label("To Airport:");
	    TextField txtTo = new TextField();
	    Button btnAdd = new Button("Add");
	
	    btnAdd.setOnAction(e -> {
	        String from = txtFrom.getText().trim().toUpperCase();
	        String to = txtTo.getText().trim().toUpperCase();
	        if (from.isEmpty() || to.isEmpty()) {
	            showAlert("Add Connection", "Please enter both airport codes.");
	        } else if (!graph.hasVertex(from) || !graph.hasVertex(to)) {
	            showAlert("Add Connection", "One or both airport codes do not exist.");
	        } else {
	            graph.addConnection(from, to);  // Adding connection without distance
	            showAlert("Add Connection", "Connection from " + from + " to " + to + " added.");
	            viewGraph(canvas.getGraphicsContext2D()); // Refresh graph view
	            dialogStage.close();
	        }
	    });
	
	    VBox dialogBox = new VBox(10, lblFrom, txtFrom, lblTo, txtTo, btnAdd);
	    Scene dialogScene = new Scene(dialogBox, 300, 200);
	    dialogStage.setScene(dialogScene);
	    dialogStage.show();
	}

    private void removeConnection() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Remove Connection");

        Label lblFrom = new Label("From Airport:");
        TextField txtFrom = new TextField();
        Label lblTo = new Label("To Airport:");
        TextField txtTo = new TextField();
        Button btnRemove = new Button("Remove");

        btnRemove.setOnAction(e -> {
            String from = txtFrom.getText().trim().toUpperCase();
            String to = txtTo.getText().trim().toUpperCase();
            if (from.isEmpty() || to.isEmpty()) {
                showAlert("Remove Connection", "Please enter valid airport codes.");
            } else if (!graph.hasVertex(from) || !graph.hasVertex(to)) {
                showAlert("Remove Connection", "One or both airport codes do not exist.");
            } else if (!graph.hasConnection(from, to)) {
                showAlert("Remove Connection", "No connection exists between " + from + " and " + to + ".");
            } else {
                graph.removeConnection(from, to);
                showAlert("Remove Connection", "Connection from " + from + " to " + to + " removed.");
                viewGraph(canvas.getGraphicsContext2D()); // Refresh graph view
                dialogStage.close();
            }
        });

        VBox dialogBox = new VBox(10, lblFrom, txtFrom, lblTo, txtTo, btnRemove);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void enableAirportMovement(GraphicsContext gc) {
        canvas.setOnMousePressed(e -> startMovingAirport(e));
        canvas.setOnMouseDragged(e -> dragAirport(e, gc));
    }

    private void disableAirportMovement(GraphicsContext gc) {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        movingAirportCode = null;
    }

    private String movingAirportCode;

    private void startMovingAirport(MouseEvent e) {
        movingAirportCode = findAirportAtPosition(e.getX(), e.getY());
    }

    private void dragAirport(MouseEvent e, GraphicsContext gc) {
        if (movingAirportCode != null && e.getButton() == MouseButton.PRIMARY) {
            double[] position = positions.get(movingAirportCode);

            // Enforce boundary conditions
            double newX = Math.max(0, Math.min(e.getX(), canvas.getWidth() - 50));
            double newY = Math.max(0, Math.min(e.getY(), canvas.getHeight() - 50));

            position[0] = newX;
            position[1] = newY;

            viewGraph(gc);  // Refresh graph view
        }
    }

    private String findAirportAtPosition(double x, double y) {
        final double radius = 20;
        for (Map.Entry<String, double[]> entry : positions.entrySet()) {
            double[] position = entry.getValue();
            if (Math.abs(position[0] - x) < radius && Math.abs(position[1] - y) < radius) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void createStaff() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create Staff");

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Button btnCreate = new Button("Create");

        btnCreate.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Create Staff", "Please enter both username and password.");
            } else if (isUsernameTaken(username)) {
                showAlert("Create Staff", "Username already taken.");
            } else {
                Staff newStaff = new Staff(username, password);
                staffList.add(newStaff);
                showAlert("Create Staff", "New staff member created.");
                dialogStage.close();
            }
        });

        VBox dialogBox = new VBox(10, lblUsername, txtUsername, lblPassword, txtPassword, btnCreate);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private boolean isUsernameTaken(String username) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username)) {
                return true;
            }
        }
        for (Staff staff : staffList) {
            if (staff.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private String getCurrentUserText() {
        if (loggedInUser == null) {
            return "Not logged in";
        } else {
            return "Logged in as: " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")";
        }
    }

    private static void initializeGraph() {
        String[][] airports = {
            {"KUL", "Kuala Lumpur"}, {"SIN", "Singapore"}, {"BKK", "Bangkok"}, {"HKG", "Hong Kong"},
            {"NRT", "Tokyo Narita"}, {"SYD", "Sydney"}, {"LAX", "Los Angeles"}, {"JFK", "New York JFK"}, 
            {"LHR", "London Heathrow"}, {"CDG", "Paris Charles de Gaulle"}
        };
        
        for (String[] airport : airports) {
            graph.addVertex(airport[0].toUpperCase(), airport[1]);
        }
        
        String[][] connections = {
            {"KUL", "SIN"}, {"SIN", "HKG"}, {"HKG", "NRT"}, {"NRT", "SYD"}, 
            {"SYD", "LAX"}, {"LAX", "JFK"}, {"JFK", "LHR"}, {"LHR", "CDG"}, {"CDG", "BKK"}, {"BKK", "KUL"}
        };
        
        for (String[] connection : connections) {
            graph.addConnection(connection[0].toUpperCase(), connection[1].toUpperCase());
        }
    }

    private static void initializeUsers() {
        Admin admin = new Admin("Admin", "Admin123", "Admin");
        adminList.add(admin);

        Staff staff = new Staff("Staff", "Staff123");
        staffList.add(staff);
    }

    private void viewGraph(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
    
        // Initialize positions only if they haven't been set yet
        if (!positions.containsKey("KUL")) {
            double xOffset = width * 0.1;
            double yOffset = height * 0.1;
            double xSpacing = width * 0.1;
            double ySpacing = height * 0.2;
    
            positions.put("KUL", new double[]{xOffset, yOffset});
            positions.put("SIN", new double[]{xOffset + xSpacing, yOffset});
            positions.put("BKK", new double[]{xOffset + 2 * xSpacing, yOffset});
            positions.put("HKG", new double[]{xOffset + 3 * xSpacing, yOffset});
            positions.put("NRT", new double[]{xOffset + 4 * xSpacing, yOffset});
            positions.put("SYD", new double[]{xOffset + 2 * xSpacing, yOffset + ySpacing});
            positions.put("LAX", new double[]{xOffset + 3 * xSpacing, yOffset + ySpacing});
            positions.put("JFK", new double[]{xOffset + 4 * xSpacing, yOffset + ySpacing});
            positions.put("LHR", new double[]{xOffset + 5 * xSpacing, yOffset});
            positions.put("CDG", new double[]{xOffset + 5 * xSpacing, yOffset + ySpacing});
        }
    
        // Clear the canvas
        gc.clearRect(0, 0, width, height);
    
        // Set font size relative to the canvas size
        gc.setFont(Font.font("Arial", Math.min(width, height) * 0.025));
        gc.setFill(Color.BLACK);
    
        // Redraw the graph based on updated positions
        graph.draw(gc, positions);
    }

    private void dfsTraversal(GraphicsContext gc, String start) {
        List<String> path = graph.dfs(start);
        viewGraph(gc); // Refresh graph view
        graph.animateTraversal(gc, positions, path);
    
        // Delay resetting the highlights by 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> resetAirportHighlighting(gc));
        pause.play();
    }

    private void bfsTraversal(GraphicsContext gc, String start) {
        List<String> path = graph.bfs(start);
        viewGraph(gc); // Refresh graph view
        graph.animateTraversal(gc, positions, path);

        // Delay resetting the highlights by 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> resetAirportHighlighting(gc));
        pause.play();
    }

    private void findShortestPath(GraphicsContext gc, String start, String end) {
        List<String> path = graph.findShortestPath(start, end);
        if (path == null || path.isEmpty()) {
            showAlert("Find Shortest Path", "No path found between " + start + " and " + end + ".");
        } else {
            viewGraph(gc); // Refresh graph view
            graph.animateTraversal(gc, positions, path);
    
            // Delay resetting the highlights by 3 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> resetAirportHighlighting(gc));
            pause.play();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
