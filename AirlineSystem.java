import java.util.*;
import java.io.IOException;

public class AirlineSystem {
    private Graph graph = new Graph();
    private Map<String, Admin> admins = new HashMap<>();
    private Map<String, Staff> staff = new HashMap<>();
    private User loggedInUser = null;

    public AirlineSystem() {
        // Default credentials
        admins.put("Admin", new Admin("Admin", "AirAsiaAdmin2024"));
        staff.put("F001", new Staff("F001", "AirAsia2024"));
        
        // Add default airports and edges
        addDefaultAirportsAndEdges();
    }

    public void addStaff(Staff staff) {
        this.staff.put(staff.getUsername(), staff);
    }

    public void login(String username, String password) {
        if (admins.containsKey(username) && admins.get(username).authenticate(password)) {
            loggedInUser = admins.get(username);
            System.out.println("Login successful! Welcome, " + username + ".");
        } else if (staff.containsKey(username) && staff.get(username).authenticate(password)) {
            loggedInUser = staff.get(username);
            System.out.println("Login successful! Welcome, " + username + ".");
        } else {
            System.out.println("Invalid credentials.");
        }
        pauseScreen(2000);
    }

    public void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully.");
        pauseScreen(2000);
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        int userInput;

        do {
            clearScreen();
            printDashes();
            System.out.println("Welcome to Airasia Airline!");
            printDashes();
            System.out.println("Logged in as: " + (loggedInUser != null ? loggedInUser.getUsername() : "User"));
            printDashes();
            System.out.println("1 View Airasia Flight Network");
            System.out.println("2 Create Graph");
            System.out.println("3 Search for an airport");
            System.out.println("4 " + (loggedInUser == null ? "Login" : "Logout"));
            if (loggedInUser instanceof Admin) {
                System.out.println("5 Create new Staff");
            }
            System.out.println("0 Exit Program");
            System.out.print("Selection: ");
            
            userInput = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            handleUserInput(userInput);
        } while (userInput != 0);
        
        scanner.close();
    }

    public void handleUserInput(int userInput) {
        int graphInput;
        
        switch (userInput) {
            case 1:
                viewFlightNetwork();
                break;
            case 2:
                if (loggedInUser instanceof Admin || loggedInUser instanceof Staff) {
                    do {
                        graphInput = createGraph();
                        handleGraphInput(graphInput);
                    } while (graphInput != 0);
                } else {
                    System.out.println("Please login as Admin or Staff to access this feature.");
                    pauseScreen(2000);
                }
                break;
            case 3:
                searchAirport();
                break;
            case 4:
                if (loggedInUser == null) {
                    loginUser();
                } else {
                    logout();
                }
                break;
            case 5:
                if (loggedInUser instanceof Admin) {
                    createNewStaff();
                } else {
                    System.out.println("Invalid selection. Please try again.");
                    pauseScreen(2000);
                }
                break;
            case 0:
                System.out.println("Exiting program, thanks for using our program!");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                pauseScreen(2000);
        }
    }

    public void viewFlightNetwork() {
        clearScreen();
        graph.printGraphAsDiagram();
        System.out.println("\nPress Enter to return to the main menu...");
        new Scanner(System.in).nextLine(); // Wait for user input
    }

    public int createGraph() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        printDashes();
        System.out.println("Graph Creation Screen");
        printDashes();
        System.out.println("1 Add Vertex");
        System.out.println("2 Add Edge");
        System.out.println("3 Remove Vertex");
        System.out.println("4 Remove Edge");
        System.out.println("0 Exit Graph Creation");
        System.out.print("Selection: ");
        
        int userInput = scanner.nextInt();
        return userInput;
    }

    public void handleGraphInput(int graphInput) {
        switch (graphInput) {
            case 1:
                addVertex();
                break;
            case 2:
                addEdge();
                break;
            case 3:
                removeVertex();
                break;
            case 4:
                removeEdge();
                break;
            case 0:
                System.out.println("Returning to main menu...");
                pauseScreen(2000);
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                pauseScreen(2000);
        }
    }

    public void searchAirport() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the airport to search: ");
        String airport = scanner.nextLine();
        
        if (graph.getVertices().contains(airport)) {
            System.out.println("Airport " + airport + " is in the network.");
        } else {
            System.out.println("Airport " + airport + " is not in the network.");
        }
        
        pauseScreen(2000);
    }

    public void addVertex() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the new airport: ");
        String vertex = scanner.nextLine();
        
        graph.addVertex(vertex);
        System.out.println("Airport " + vertex + " added.");
        
        pauseScreen(2000);
    }

    public void addEdge() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the source airport: ");
        String source = scanner.nextLine();
        System.out.print("Enter the destination airport: ");
        String destination = scanner.nextLine();
        
        graph.addEdge(source, destination);
        System.out.println("Flight added from " + source + " to " + destination);
        
        pauseScreen(2000);
    }

    public void removeVertex() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the airport to remove: ");
        String vertex = scanner.nextLine();
        
        graph.removeVertex(vertex);
        System.out.println("Airport " + vertex + " removed.");
        
        pauseScreen(2000);
    }

    public void removeEdge() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the source airport: ");
        String source = scanner.nextLine();
        System.out.print("Enter the destination airport: ");
        String destination = scanner.nextLine();
        
        graph.removeEdge(source, destination);
        System.out.println("Flight removed from " + source + " to " + destination);
        
        pauseScreen(2000);
    }

    public void createNewStaff() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new Staff ID: ");
        String staffID = scanner.nextLine();
        System.out.print("Enter password for new Staff: ");
        String password = scanner.nextLine();
        
        Admin admin = (Admin) loggedInUser;
        admin.createStaff(staffID, password, this);
        System.out.println("New staff " + staffID + " created.");
        
        pauseScreen(2000);
    }

    public void loginUser() {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username (or Staff ID): ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        login(username, password);
    }

    public static void printDashes() {
        for (int i = 0; i < 30; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void pauseScreen(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to pause the thread.");
        }
    }

    private void addDefaultAirportsAndEdges() {
        // Add 30 airports
        String[] airports = {
            "KUL", "SIN", "BKK", "HKG", "NRT", "PEK", "SYD", "MEL", "LAX", "JFK",
            "LHR", "CDG", "AMS", "FRA", "DXB", "DOH", "ICN", "IST", "BCN", "MAD",
            "FCO", "VIE", "ZRH", "CPT", "JNB", "GRU", "EZE", "YYZ", "YVR", "SFO" 
        };

        for (String airport : airports) {
            graph.addVertex(airport);
        }

        // Add some edges to connect the airports
        String[][] edges = {
            {"KUL", "SIN"}, {"KUL", "BKK"}, {"SIN", "HKG"}, {"HKG", "NRT"}, {"NRT", "PEK"},
            {"PEK", "SYD"}, {"SYD", "MEL"}, {"MEL", "LAX"}, {"LAX", "JFK"}, {"JFK", "LHR"},
            {"LHR", "CDG"}, {"CDG", "AMS"}, {"AMS", "FRA"}, {"FRA", "DXB"}, {"DXB", "DOH"},
            {"DOH", "ICN"}, {"ICN", "IST"}, {"IST", "BCN"}, {"BCN", "MAD"}, {"MAD", "FCO"},
            {"FCO", "VIE"}, {"VIE", "ZRH"}, {"ZRH", "CPT"}, {"CPT", "JNB"}, {"JNB", "GRU"},
            {"GRU", "EZE"}, {"EZE", "YYZ"}, {"YYZ", "YVR"}, {"YVR", "SFO"}, {"SFO", "KUL"}
        };

        for (String[] edge : edges) {
            graph.addEdge(edge[0], edge[1]);
        }
    }

    public static void main(String[] args) {
        AirlineSystem system = new AirlineSystem();
        system.mainMenu();
    }
}
