import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

import java.util.*;

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
        canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        viewGraph(gc);
        showMainMenu(gc);
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

        HBox inputBox = new HBox(10, lblStart, txtStart, lblEnd, txtEnd);
        VBox buttonBox = new VBox(10, btnAlgorithms, btnEditor, btnAirportInfo, lblCurrentUser, btnLoginLogout);

        if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
            Button btnCreateStaff = new Button("Create Staff");
            buttonBox.getChildren().add(3, btnCreateStaff);
            btnCreateStaff.setOnAction(e -> createStaff());
        }

        createLoginBox();

        BorderPane root = new BorderPane();
        root.setTop(inputBox);
        root.setCenter(canvas);
        root.setRight(buttonBox);
        root.setBottom(loginBox);

        Scene scene = new Scene(root, 1000, 800);
        mainStage.setScene(scene);
        mainStage.show();

        btnAlgorithms.setOnAction(e -> showAlgorithmsMenu(gc, txtStart, txtEnd));
        btnEditor.setOnAction(e -> {
            if (loggedInUser != null && (loggedInUser.getRole().equals("Staff") || loggedInUser.getRole().equals("Admin"))) {
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
        Label lblDistance = new Label("Distance:");
        TextField txtDistance = new TextField();
        Button btnAdd = new Button("Add");

        btnAdd.setOnAction(e -> {
            String from = txtFrom.getText().trim().toUpperCase();
            String to = txtTo.getText().trim().toUpperCase();
            int distance;
            try {
                distance = Integer.parseInt(txtDistance.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert("Add Connection", "Invalid distance. Please enter a valid number.");
                return;
            }
            if (from.isEmpty() || to.isEmpty() || distance <= 0) {
                showAlert("Add Connection", "Please enter valid airport codes and a positive distance.");
            } else if (!graph.hasVertex(from) || !graph.hasVertex(to)) {
                showAlert("Add Connection", "One or both airport codes do not exist.");
            } else {
                graph.addConnection(from, to, distance);
                showAlert("Add Connection", "Connection from " + from + " to " + to + " added.");
                viewGraph(canvas.getGraphicsContext2D()); // Refresh graph view
                dialogStage.close();
            }
        });

        VBox dialogBox = new VBox(10, lblFrom, txtFrom, lblTo, txtTo, lblDistance, txtDistance, btnAdd);
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
            graph.addConnection(connection[0].toUpperCase(), connection[1].toUpperCase(), 1);
        }
    }

    private static void initializeUsers() {
        Admin admin = new Admin("Admin", "Admin123", "Admin");
        adminList.add(admin);

        Staff staff = new Staff("Staff", "Staff123");
        staffList.add(staff);
    }

    private void viewGraph(GraphicsContext gc) {
        if (!positions.containsKey("KUL")) {  // Check if positions are already initialized
            positions.put("KUL", new double[]{100, 100});
            positions.put("SIN", new double[]{200, 100});
            positions.put("BKK", new double[]{300, 100});
            positions.put("HKG", new double[]{400, 100});
            positions.put("NRT", new double[]{500, 100});
            positions.put("SYD", new double[]{300, 200});
            positions.put("LAX", new double[]{400, 200});
            positions.put("JFK", new double[]{500, 200});
            positions.put("LHR", new double[]{600, 100});
            positions.put("CDG", new double[]{600, 200});
        }

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFont(Font.font("Arial", Font.getDefault().getSize() * 1.5));
        gc.setFill(Color.BLACK);
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
