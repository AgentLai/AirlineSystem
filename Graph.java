import java.util.*;

public class Graph {
    private Map<String, List<String>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void removeVertex(String vertex) {
        adjacencyList.values().forEach(e -> e.remove(vertex));
        adjacencyList.remove(vertex);
    }

    public void addEdge(String vertex1, String vertex2) {
        adjacencyList.get(vertex1).add(vertex2);
        adjacencyList.get(vertex2).add(vertex1);
    }

    public void removeEdge(String vertex1, String vertex2) {
        List<String> edgesVertex1 = adjacencyList.get(vertex1);
        List<String> edgesVertex2 = adjacencyList.get(vertex2);

        if (edgesVertex1 != null) {
            edgesVertex1.remove(vertex2);
        }
        if (edgesVertex2 != null) {
            edgesVertex2.remove(vertex1);
        }
    }

    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    public List<String> getEdges(String vertex) {
        return adjacencyList.get(vertex);
    }

    public void printGraph() {
        System.out.println("AirAsia Flight Network:");
        for (String vertex : adjacencyList.keySet()) {
            System.out.print(vertex + " -> ");
            List<String> edges = adjacencyList.get(vertex);
            for (String edge : edges) {
                System.out.print(edge + " ");
            }
            System.out.println();
        }
    }

    public void printGraphAsDiagram() {
        for (String vertex : adjacencyList.keySet()) {
            System.out.print(vertex + " -> ");
            List<String> edges = adjacencyList.get(vertex);
            for (String edge : edges) {
                System.out.print(edge + " ");
            }
            System.out.println("\n");
        }
    }
}
