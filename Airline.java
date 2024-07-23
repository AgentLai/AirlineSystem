import java.util.*;
import java.io.IOException;

public class Airline{
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		Graph graph = new Graph();
		int userInput;
		
		do{
			userInput = mainMenu();
			handleUserInput(userInput);
		}while(userInput != 0);
		
	}
	
	public static int mainMenu(){
		clearScreen();
		Scanner scanner = new Scanner(System.in);
		printDashes();
		System.out.println("Welcome to Airasia Airline!");
		printDashes();
		System.out.println("1 View Airasia Flight Network");
		System.out.println("2 Create Graph");
		System.out.println("3 Search for an airport");
		System.out.println("0 Exit Program");
		System.out.print("Selection: ");
		
		int userInput = scanner.nextInt();
		return userInput;
	}
	
	public static void handleUserInput(int userInput){
		int graphInput;
		
		switch (userInput) {
            case 1:
                viewFlightNetwork();
                break;
            case 2:
            	do{
            		graphInput = createGraph();
            		handleGraphInput(graphInput);
            	}while(graphInput != 0);
                break;
            case 3:
                searchAirport();
                break;
            case 0:
                System.out.println("Exiting program, thanks for using our program!");
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
	}
	
	public static void handleGraphInput(int graphInput){
		clearScreen();
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
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
	}
	
	public static void viewFlightNetwork(){
		clearScreen();
	}
	
	public static int createGraph(){
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
	
	public static void searchAirport(){
		clearScreen();
	}

	public static void addVertex(){
		
	}
		
	public static void addEdge(){
		
	}
	
	public static void removeVertex(){
		
	}
	
	public static void removeEdge(){
		
	}
	
	public static void printDashes(){
		for(int i = 0; i <30; i++){
			System.out.print("-");
		}
		System.out.println();
	}
	
	public static void clearScreen(){
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
	
	public static void pauseScreen(int time){
		try {
			Thread.sleep(time);
			} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("Failed to pause the thread.");
			}
	}
}