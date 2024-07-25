import java.util.*;

public class Graph {
    private Map<String, List<String>> adjVertices = new HashMap<>();
    private Map<String, String> airportCities = new HashMap<>();
    
    public void addVertex(String code, String city) {
        adjVertices.putIfAbsent(code, new ArrayList<>());
        airportCities.putIfAbsent(code, city);
    }

    public void addEdge(String source, String destination) {
        adjVertices.get(source).add(destination);
        adjVertices.get(destination).add(source); // Since it's an undirected graph
    }

    public void removeVertex(String code) {
        adjVertices.values().forEach(e -> e.remove(code));
        adjVertices.remove(code);
        airportCities.remove(code);
    }

    public void removeEdge(String source, String destination) {
        List<String> srcEdges = adjVertices.get(source);
        List<String> destEdges = adjVertices.get(destination);
        if (srcEdges != null) srcEdges.remove(destination);
        if (destEdges != null) destEdges.remove(source);
    }

    public List<String> getEdges(String code) {
        return adjVertices.getOrDefault(code, new ArrayList<>());
    }

    public Set<String> getVertices() {
        return adjVertices.keySet();
    }

    public String getCityName(String code) {
        return airportCities.get(code);
    }

    public List<String> dfs(String start) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsRecursive(start, visited, result);
        return result;
    }

    private void dfsRecursive(String vertex, Set<String> visited, List<String> result) {
        if (visited.contains(vertex)) return;
        visited.add(vertex);
        result.add(vertex);
        for (String neighbor : adjVertices.getOrDefault(vertex, new ArrayList<>())) {
            dfsRecursive(neighbor, visited, result);
        }
    }

    public List<String> bfs(String start) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
    
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);
            for (String neighbor : adjVertices.getOrDefault(vertex, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    public void displayFlightPath(String start) {
        List<String> path = bfs(start); 
        System.out.println("Flight Path from " + start + ":");
        for (String airport : path) {
            System.out.println(airport + " - " + airportCities.get(airport));
        }
    }
    
}
