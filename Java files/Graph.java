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
        if (edgesVertex1 != null) edgesVertex1.remove(vertex2);
        if (edgesVertex2 != null) edgesVertex2.remove(vertex1);
    }

    public List<String> getVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    public List<String> getEdges(String vertex) {
        return adjacencyList.get(vertex);
    }

    public List<String> dfsTraversal(String startVertex) {
        List<String> visitedVertices = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        stack.push(startVertex);

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                visitedVertices.add(vertex);

                List<String> neighbours = adjacencyList.get(vertex);
                if (neighbours != null) {
                    for (String neighbour : neighbours) {
                        if (!visited.contains(neighbour)) {
                            stack.push(neighbour);
                        }
                    }
                }
            }
        }

        return visitedVertices;
    }
}
