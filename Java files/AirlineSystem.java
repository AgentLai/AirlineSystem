import java.util.*;
import java.io.IOException;

public class AirlineSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Graph graph = new Graph();
    private static User loggedInUser = null;
    private static List<Staff> staffList = new ArrayList<>();

    public static void main(String[] args) {
        initializeGraph();
        int userInput;

        do {
            clearScreen();
            displayLoggedInUser();
            userInput = mainMenu();
            handleUserInput(userInput);
        } while (userInput != 0);
    }

    public static void initializeGraph() {
        String[] airports = {
            "KUL", "SIN", "BKK", "HKG", "NRT", "ICN", "SYD", "MEL", "PER", "ADL",
            "AKL", "WLG", "BNE", "CNS", "DRW", "HBA", "CBR", "OOL", "NTL", "MKY",
            "TSV", "ROK", "PPP", "HTI", "MOV", "ARM", "GFF", "PLO", "KGC", "MQL"
        };

        for (String airport : airports) {
            graph.addVertex(airport);
        }

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

    public static void displayLoggedInUser() {
        if (loggedInUser != null) {
            System.out.println("Logged in as: " + loggedInUser.getUsername());
        } else {
            System.out.println("Logged in as: User");
        }
    }

    public static int mainMenu() {
        printDashes();
        System.out.println("Welcome to AirAsia Airline!");
        printDashes();
        System.out.println("1. View AirAsia Flight Network");
        System.out.println("2. Create Graph");
        System.out.println("3. Search for an airport");
        System.out.println("4. Login");
        System.out.println("5. Create new staff (Admins only)");
        System.out.println("0. Exit Program");
        System.out.print("Selection: ");

        int userInput = getInput();
        scanner.nextLine(); // Consume newline left-over
        return userInput;
    }

    public static void handleUserInput(int userInput) {
        switch (userInput) {
            case 1:
                viewFlightNetwork();
                break;
            case 2:
                if (loggedInUser != null && (loggedInUser.getRole().equals("Admin") || loggedInUser.getRole().equals("Staff"))) {
                    createGraph();
                } else {
                    System.out.println("Access denied. Please login as Admin or Staff.");
                }
                break;
            case 3:
                searchAirport();
                break;
            case 4:
                login();
                break;
            case 5:
                if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
                    createNewStaff();
                } else {
                    System.out.println("Access denied. Admins only.");
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

    public static void viewFlightNetwork() {
        clearScreen();
        printDashes();
        System.out.println("AirAsia Flight Network:");
        printDashes();
        System.out.println("Airport Codes: KUL - Kuala Lumpur, SIN - Singapore, BKK - Bangkok, HKG - Hong Kong, NRT - Tokyo Narita, ICN - Seoul Incheon, SYD - Sydney, MEL - Melbourne, PER - Perth, ADL - Adelaide, AKL - Auckland, WLG - Wellington, BNE - Brisbane, CNS - Cairns, DRW - Darwin, HBA - Hobart, CBR - Canberra, OOL - Gold Coast, NTL - Newcastle, MKY - Mackay, TSV - Townsville, ROK - Rockhampton, PPP - Proserpine, HTI - Hamilton Island, MOV - Moranbah, ARM - Armidale, GFF - Griffith, PLO - Port Lincoln, KGC - Kingscote, MQL - Mildura");
        System.out.println("Flight Connections:");
        printDashes();
        for (String vertex : graph.getVertices()) {
            List<String> edges = graph.getEdges(vertex);
            for (String edge : edges) {
                System.out.println(vertex + " -> " + edge);
            }
            System.out.println();
        }
        printDashes();
        System.out.println("Press Enter to exit.");
        scanner.nextLine(); // Wait for user input to exit
    }

    public static void createGraph() {
        int graphInput;
        do {
            graphInput = graphMenu();
            handleGraphInput(graphInput);
        } while (graphInput != 0);
    }

    public static int graphMenu() {
        clearScreen();
        printDashes();
        System.out.println("Graph Creation Screen");
        printDashes();
        System.out.println("1. Add Vertex");
        System.out.println("2. Add Edge");
        System.out.println("3. Remove Vertex (Admins only)");
        System.out.println("4. Remove Edge (Admins only)");
        System.out.println("0. Exit Graph Creation");
        System.out.print("Selection: ");

        int userInput = getInput();
        scanner.nextLine(); // Consume newline left-over
        return userInput;
    }

    public static void handleGraphInput(int graphInput) {
        clearScreen();
        switch (graphInput) {
            case 1:
                addVertex();
                break;
            case 2:
                addEdge();
                break;
            case 3:
                if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
                    removeVertex();
                } else {
                    System.out.println("Access denied. Admins only.");
                }
                break;
            case 4:
                if (loggedInUser != null && loggedInUser.getRole().equals("Admin")) {
                    removeEdge();
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
        pauseScreen(1000);
    }

    public static void searchAirport() {
        clearScreen();
        System.out.print("Enter airport code to search: ");
        String airport = scanner.nextLine().toUpperCase();
        if (graph.getVertices().contains(airport)) {
            System.out.println("Airport " + airport + " found. Flight paths:");
            List<String> paths = graph.dfsTraversal(airport);
            for (String path : paths) {
                System.out.println(airport + " -> " + path);
            }
        } else {
            System.out.println("Airport not found.");
        }
        pauseScreen(2000);
    }

    public static void login() {
        clearScreen();
        System.out.print("Enter username (Admin or Staff ID): ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (username.equals("Admin") && password.equals("AirAsiaAdmin2024")) {
            loggedInUser = new Admin("Admin", "AirAsiaAdmin2024");
            System.out.println("Login successful. Welcome, Admin!");
        } else if (username.equals("F001") && password.equals("AirAsia2024")) {
            loggedInUser = new Staff("F001", "AirAsia2024");
            System.out.println("Login successful. Welcome, Staff F001!");
        } else {
            System.out.println("Invalid login. Please try again.");
        }
        pauseScreen(2000);
    }

    public static void createNewStaff() {
        clearScreen();
        System.out.print("Enter new staff ID: ");
        String staffId = scanner.nextLine();
        System.out.print("Enter password for new staff: ");
        String password = scanner.nextLine();

        if (loggedInUser instanceof Admin) {
            ((Admin) loggedInUser).createStaff(staffId, password, new AirlineSystem());
            System.out.println("New staff created successfully.");
        } else {
            System.out.println("Access denied. Admins only.");
        }
        pauseScreen(2000);
    }

    public void addStaff(Staff newStaff) {
        staffList.add(newStaff);
    }

    public static void addVertex() {
        System.out.print("Enter the name of the airport to add: ");
        String airport = scanner.nextLine().toUpperCase();
        graph.addVertex(airport);
        System.out.println("Vertex " + airport + " added successfully.");
    }

    public static void addEdge() {
        System.out.print("Enter the name of the starting airport: ");
        String start = scanner.nextLine().toUpperCase();
        System.out.print("Enter the name of the destination airport: ");
        String end = scanner.nextLine().toUpperCase();
        graph.addEdge(start, end);
        System.out.println("Edge " + start + " -> " + "end added successfully.");
    }

    public static void removeVertex() {
        System.out.print("Enter the name of the airport to remove: ");
        String airport = scanner.nextLine().toUpperCase();
        graph.removeVertex(airport);
        System.out.println("Vertex " + airport + " removed successfully.");
    }

    public static void removeEdge() {
        System.out.print("Enter the name of the starting airport: ");
        String start = scanner.nextLine().toUpperCase();
        System.out.print("Enter the name of the destination airport: ");
        String end = scanner.nextLine().toUpperCase();
        graph.removeEdge(start, end);
        System.out.println("Edge " + start + " -> " + "end removed successfully.");
    }

    public static void printDashes() {
        System.out.println("-------------------------------------------------");
    }

    public static void pauseScreen(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Pause interrupted.");
        }
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Clear screen interrupted.");
        }
    }

    public static int getInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
