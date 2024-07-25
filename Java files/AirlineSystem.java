import java.util.*;
import java.io.IOException;

public class AirlineSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Graph graph = new Graph();
    private static User loggedInUser = null;
    private static List<Staff> staffList = new ArrayList<>();
    private static List<Admin> adminList = new ArrayList<>();

    public static void main(String[] args) {
        initializeGraph();
        initializeAdmins();
        int userInput;

        do {
            clearScreen();
            displayLoggedInUser();
            userInput = mainMenu();
            handleUserInput(userInput);
        } while (userInput != 0);
    }

    public static void initializeGraph() {
        String[][] airports = {
            {"KUL", "Kuala Lumpur"}, {"SIN", "Singapore"}, {"BKK", "Bangkok"}, {"HKG", "Hong Kong"},
            {"NRT", "Tokyo Narita"}, {"ICN", "Seoul Incheon"}, {"SYD", "Sydney"}, {"MEL", "Melbourne"},
            {"PER", "Perth"}, {"ADL", "Adelaide"}, {"AKL", "Auckland"}, {"WLG", "Wellington"},
            {"BNE", "Brisbane"}, {"CNS", "Cairns"}, {"DRW", "Darwin"}, {"HBA", "Hobart"},
            {"CBR", "Canberra"}, {"OOL", "Gold Coast"}, {"NTL", "Newcastle"}, {"MKY", "Mackay"},
            {"TSV", "Townsville"}, {"ROK", "Rockhampton"}, {"PPP", "Proserpine"}, {"HTI", "Hamilton Island"},
            {"MOV", "Moranbah"}, {"ARM", "Armidale"}, {"GFF", "Griffith"}, {"PLO", "Port Lincoln"},
            {"KGC", "Kingscote"}, {"MQL", "Mildura"}
        };

        for (String[] airport : airports) {
            graph.addVertex(airport[0], airport[1]); // Pass both airport code and city name
        }

        // Add edges
        graph.addEdge("KUL", "SIN");
        graph.addEdge("KUL", "BKK");
        graph.addEdge("SIN", "HKG");
        graph.addEdge("BKK", "NRT");
        graph.addEdge("HKG", "ICN");
        graph.addEdge("NRT", "SYD");
        graph.addEdge("ICN", "MEL");
        graph.addEdge("SYD", "PER");
        graph.addEdge("MEL", "ADL");
        graph.addEdge("PER", "AKL");
        graph.addEdge("ADL", "WLG");
        graph.addEdge("AKL", "BNE");
        graph.addEdge("WLG", "CNS");
        graph.addEdge("BNE", "DRW");
        graph.addEdge("CNS", "HBA");
        graph.addEdge("DRW", "CBR");
        graph.addEdge("HBA", "OOL");
        graph.addEdge("CBR", "NTL");
        graph.addEdge("OOL", "MKY");
        graph.addEdge("NTL", "TSV");
        graph.addEdge("MKY", "ROK");
        graph.addEdge("TSV", "PPP");
        graph.addEdge("ROK", "HTI");
        graph.addEdge("PPP", "MOV");
        graph.addEdge("HTI", "ARM");
        graph.addEdge("MOV", "GFF");
        graph.addEdge("ARM", "PLO");
        graph.addEdge("GFF", "KGC");
        graph.addEdge("PLO", "MQL");
    } 

    public static void initializeAdmins() {
        adminList.add(new Admin("Admin", "Admin123"));
    }

    public static void displayLoggedInUser() {
        if (loggedInUser != null) {
            System.out.println("Logged in as: " + loggedInUser.getUsername());
            System.out.println("Role: " + loggedInUser.getRole());
        } else {
            System.out.println("Logged in as: User");
        }
    }

    public static int mainMenu() {
        clearScreen();  // Ensure the screen is cleared before displaying the menu
        printDashes();
        System.out.println("Welcome to AirAsia Airline!");
        printDashes();
        System.out.println(getLoggedInUserInfo());  // Display user info
        printDashes();
        
        System.out.println("1. View AirAsia Flight Network");
        System.out.println("2. Search for an airport");
        
        if (loggedInUser != null) {
            // User is logged in
            if (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Staff")) {
                System.out.println("3. Create Graph");  // Only visible to logged-in users (Admin or Staff)
            }
            System.out.println("4. Logout");         // Logout option if logged in
            if (loggedInUser.getRole().equals("Admin")) {
                System.out.println("5. Create new staff (Admins only)");
            }
            System.out.println("6. Traversal Algorithms"); // Visible to all logged-in users
        } else {
            // User is not logged in
            System.out.println("3. Login");          // Login option if not logged in
        }
        
        System.out.println("0. Exit Program");
        System.out.print("Selection: ");
        
        return getIntInput();
    }
    
    
    public static void handleUserInput(int userInput) {
        switch (userInput) {
            case 1:
                viewFlightNetwork();
                break;
            case 2:
                searchAirport();
                break;
            case 3:
                clearScreen();  // Clear screen before login or graph creation
                if (loggedInUser != null && (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Staff"))) {
                    createGraph();
                } else {
                    login();
                }
                break;
            case 4:
                if (loggedInUser != null) {
                    logout();
                } else {
                    login();
                }
                clearScreen();  // Clear screen after login
                break;
            case 5:
                if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
                    createNewStaff();
                } else {
                    System.out.println("Access denied. Admins only.");
                }
                break;
            case 6:
                if (loggedInUser != null) {
                    traversalAlgorithmsMenu();
                } else {
                    System.out.println("Please log in to access traversal algorithms.");
                }
                break;
            case 0:
                System.out.println("Exiting program, thanks for using our program!");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
        pauseScreen(1000);
    } 
    
    public static void traversalAlgorithmsMenu() {
        clearScreen();
        printDashes();
        System.out.println("Traversal Algorithms");
        printDashes();
        System.out.println("1. Depth First Search (DFS)");
        System.out.println("2. Breadth First Search (BFS)");
        System.out.println("0. Return to Main Menu");
        System.out.print("Selection: ");
    
        int userInput = getIntInput();
        switch (userInput) {
            case 1:
                dfsTraversal();
                break;
            case 2:
                bfsTraversal();
                break;
            case 0:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
        pauseScreen(1000);
    }
    

    public static void viewFlightNetwork() {
        clearScreen();
        printDashes();
        System.out.println("AirAsia Flight Network:");
        printDashes();
        
        // Get all airport codes and sort them
        List<String> sortedVertices = new ArrayList<>(graph.getVertices());
        Collections.sort(sortedVertices);
        
        // Print airport codes with city names
        for (String vertex : sortedVertices) {
            String city = graph.getCityName(vertex);
            if (city != null) {
                System.out.println(vertex + " - " + city);
            } else {
                System.out.println(vertex + " - City not found");
            }
        }
        
        System.out.println("Flight Connections:");
        printDashes();
        
        // Print connections in sorted order
        for (String vertex : sortedVertices) {
            List<String> edges = graph.getEdges(vertex);
            Collections.sort(edges); 
            for (String edge : edges) {
                System.out.println(vertex + " -> " + edge);
            }
            System.out.println();
        }
        
        printDashes();
        System.out.println("Press Enter to exit.");
        scanner.nextLine(); 
    }
    
    
    
    public static void createGraph() {
        clearScreen();  
        int graphInput;
        do {
            graphInput = graphMenu();
            if (graphInput != 0) {
                handleGraphInput(graphInput);
            }
        } while (graphInput != 0);
        clearScreen();  
    }
    

    public static int graphMenu() {
        clearScreen();
        printDashes();
        System.out.println("Graph Creation Screen");
        printDashes();
        System.out.println("1. Add Airport");
        System.out.println("2. Add Connection");
        
        // Admins only options
        if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
            System.out.println("3. Remove Airport");
            System.out.println("4. Remove Connection");
        }

        System.out.println("0. Return to Main Menu");
        System.out.print("Selection: ");
        return getIntInput();
    }
    
    public static void handleGraphInput(int graphInput) {
        switch (graphInput) {
            case 1:
                addAirport();
                break;
            case 2:
                addConnection();
                break;
            case 3:
                if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
                    removeAirport();
                } else {
                    System.out.println("Access denied. Admins only.");
                }
                break;
            case 4:
                if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
                    removeConnection();
                } else {
                    System.out.println("Access denied. Admins only.");
                }
                break;
            case 0:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
    }
    
    public static void addAirport() {
        System.out.print("Enter airport code: ");
        String code = scanner.nextLine();
        System.out.print("Enter airport city: ");
        String city = scanner.nextLine();
        graph.addVertex(code, city);
        System.out.println("Airport added.");
    }
    
    public static void addConnection() {
        System.out.print("Enter source airport code: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination airport code: ");
        String destination = scanner.nextLine();
        graph.addEdge(source, destination);
        System.out.println("Connection added.");
    }
    
    public static void removeAirport() {
        System.out.print("Enter airport code to remove: ");
        String code = scanner.nextLine();
        graph.removeVertex(code);
        System.out.println("Airport removed.");
    }
    
    public static void removeConnection() {
        System.out.print("Enter source airport code: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination airport code: ");
        String destination = scanner.nextLine();
        graph.removeEdge(source, destination);
        System.out.println("Connection removed.");
    }

    public static void searchAirport() {
    clearScreen();
    printDashes();
    System.out.println("Search for an Airport");
    printDashes();
    
    System.out.print("Enter airport code to search: ");
    String code = scanner.nextLine();
    
    String city = graph.getCityName(code);
    if (city != null) {
        System.out.println("Airport Code: " + code);
        System.out.println("City: " + city);
        printDashes();
        
        // Display direct flight path
        System.out.println("Direct Flight Path from " + code + ":");
        List<String> directFlights = graph.getEdges(code);
        if (directFlights.isEmpty()) {
            System.out.println("NONE");
        } else {
            for (String destination : directFlights) {
                System.out.println(destination + " - " + graph.getCityName(destination));
            }
        }
        printDashes();
        
        // Find unreachable locations using BFS
        System.out.println("Unreachable locations from " + code + ":");
        Set<String> reachable = bfs(code);
        List<String> allAirports = new ArrayList<>(graph.getVertices());
        allAirports.remove(code); // Remove the current airport from the list of unreachable locations
        allAirports.removeAll(reachable);
        
        if (allAirports.isEmpty()) {
            System.out.println("NONE");
        } else {
            for (String airport : allAirports) {
                System.out.println(airport + " - " + graph.getCityName(airport));
            }
        }
    } else {
        System.out.println("Airport not found.");
    }
    
    printDashes();
    System.out.println("Press Enter to exit.");
    scanner.nextLine(); // Wait for user input to exit
}

    public static Set<String> bfs(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        
        visited.add(start);
        queue.add(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : graph.getEdges(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return visited;
    }

    

    public static void dfsTraversal() {
        clearScreen();
        printDashes();
        System.out.println("Depth First Search (DFS)");
        printDashes();
    
        System.out.print("Enter starting airport code: ");
        String startCode = scanner.nextLine();
        List<String> result = graph.dfs(startCode);
        System.out.println("DFS result:");
        for (String airport : result) {
            System.out.println(airport + " - " + graph.getCityName(airport));
        }
        System.out.println("Press Enter to return.");
        scanner.nextLine();
    }
    
    public static void bfsTraversal() {
        clearScreen();
        printDashes();
        System.out.println("Breadth First Search (BFS)");
        printDashes();
    
        System.out.print("Enter starting airport code: ");
        String startCode = scanner.nextLine();
        List<String> result = graph.bfs(startCode); 
        System.out.println("BFS result:");
        for (String airport : result) {
            System.out.println(airport + " - " + graph.getCityName(airport));
        }
        System.out.println("Press Enter to return.");
        scanner.nextLine();
    }
    

    public static void createNewStaff() {
        clearScreen(); 
        System.out.println("Create New Staff");
        printDashes();
        System.out.print("Enter new staff username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new staff password: ");
        String password = scanner.nextLine();
        staffList.add(new Staff(username, password));
        System.out.println("Staff created.");
        pauseScreen(1000);  
        clearScreen();  
    }
    
    
    public static void login() {
        clearScreen();  
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
    
        User user = authenticate(username, password);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Login successful.");
            pauseScreen(1000);  
            clearScreen();  
        } else {
            System.out.println("Invalid credentials.");
            pauseScreen(1000);  
        }
    }
    

    public static void logout() {
        loggedInUser = null;
        System.out.println("Logout successful.");
    }

    public static User authenticate(String username, String password) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        for (Staff staff : staffList) {
            if (staff.getUsername().equals(username) && staff.getPassword().equals(password)) {
                return staff;
            }
        }
        return null;
    }

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error clearing the screen.");
        }
    }
    
    public static void printDashes() {
        System.out.println("------------------------------");
    }
    
    public static int getInput() {
        return getIntInput();
    }

    public static String getLoggedInUserInfo() {
        if (loggedInUser != null) {
            return "You are logged in as " + loggedInUser.getUsername() + "\nRole: " + loggedInUser.getRole();
        } else {
            return "You are not logged in";
        }
    }
    
    public static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Clear invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return input;
    }
    
    
    public static void pauseScreen(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
