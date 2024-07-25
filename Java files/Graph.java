import java.util.*;

public class Graph {
    private Map<String, List<String>> adjacencyList = new HashMap<>();

    public void addVertex(String vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    public void addEdge(String source, String destination) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            adjacencyList.get(source).add(destination);
        }
    }

    public void removeVertex(String vertex) {
        adjacencyList.remove(vertex);
        for (List<String> edges : adjacencyList.values()) {
            edges.remove(vertex);
        }
    }

    public void removeEdge(String source, String destination) {
        if (adjacencyList.containsKey(source)) {
            adjacencyList.get(source).remove(destination);
        }
    }

    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    public List<String> dfsTraversal(String startVertex) {
        List<String> visited = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (String neighbor : adjacencyList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return visited;
    }

    public List<String> bfsTraversal(String startVertex) {
        List<String> visited = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (String neighbor : adjacencyList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return visited;
    }

    public void printGraphAsDiagram() {
        for (String vertex : adjacencyList.keySet()) {
            System.out.print(vertex + ": ");
            for (String edge : adjacencyList.get(vertex)) {
                System.out.print(edge + " ");
            }
            System.out.println("\n");
        }
    }
}
