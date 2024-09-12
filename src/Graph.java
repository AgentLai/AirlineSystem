import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.*;

public class Graph {
    private Map<String, Map<String, Integer>> adjList = new HashMap<>();
    private Map<String, String> vertexData = new HashMap<>();
    
    public void addVertex(String code, String name) {
        adjList.putIfAbsent(code, new HashMap<>());
        vertexData.put(code, name);
    }

    public String getCityName(String code){
        return vertexData.get(code);
    }

    public void removeVertex(String code) {
        adjList.remove(code);
        vertexData.remove(code);
        for (Map<String, Integer> connections : adjList.values()) {
            connections.remove(code);
        }
    }
    
	public void addConnection(String from, String to) {
	    // Ensure both airports exist in the graph before adding a connection
	    if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
	        throw new IllegalArgumentException("One or both airports do not exist.");
	    }
	
	    // Add the connection without distance
	    adjList.get(from).put(to, 0); // Use a default or placeholder value (e.g., 0) for distance
	    adjList.get(to).put(from, 0); // Use a default or placeholder value (e.g., 0) for distance
	}


    public void removeConnection(String from, String to) {
        if (adjList.containsKey(from)) {
            adjList.get(from).remove(to);
        }
        if (adjList.containsKey(to)) {
            adjList.get(to).remove(from);
        }
    }

    public boolean hasVertex(String code) {
        return adjList.containsKey(code);
    }

    public boolean hasConnection(String from, String to) {
        return adjList.containsKey(from) && adjList.get(from).containsKey(to);
    }

    public List<String> dfs(String start) {
        List<String> visited = new ArrayList<>();
        Set<String> visitedSet = new HashSet<>();
        Stack<String> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (!visitedSet.contains(current)) {
                visited.add(current);
                visitedSet.add(current);
                for (String neighbor : adjList.get(current).keySet()) {
                    stack.push(neighbor);
                }
            }
        }
        return visited;
    }

    public List<String> bfs(String start) {
        List<String> visited = new ArrayList<>();
        Set<String> visitedSet = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (!visitedSet.contains(current)) {
                visited.add(current);
                visitedSet.add(current);
                for (String neighbor : adjList.get(current).keySet()) {
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

	public List<String> findShortestPath(String start, String end) {
	    Map<String, String> previous = new HashMap<>(); 
	    Set<String> visited = new HashSet<>(); 
	    Queue<String> queue = new LinkedList<>(); 
	
	    queue.add(start);
	    visited.add(start);
	
	    while (!queue.isEmpty()) {
	        String current = queue.poll();
	
	        if (current.equals(end)) {
	            break;
	        }
	
	        for (String neighbor : adjList.get(current).keySet()) {
	            if (!visited.contains(neighbor)) {
	                queue.add(neighbor);
	                visited.add(neighbor);
	                previous.put(neighbor, current);
	
	                if (neighbor.equals(end)) {
	                    break;
	                }
	            }
	        }
	    }
	
	    List<String> path = new ArrayList<>();
	    for (String at = end; at != null; at = previous.get(at)) {
	        path.add(at);
	    }
	    Collections.reverse(path);
	
	    // Check if the start is in the path; if not, return an empty list
	    if (path.get(0).equals(start)) {
	        return path;
	    } else {
	        return new ArrayList<>();
	    }
	}



    public void draw(GraphicsContext gc, Map<String, double[]> positions) {
        for (Map.Entry<String, Map<String, Integer>> entry : adjList.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue().keySet()) {
                gc.setStroke(Color.BLACK);
                gc.strokeLine(
                        positions.get(from)[0], positions.get(from)[1],
                        positions.get(to)[0], positions.get(to)[1]
                );
            }
        }
        for (String vertex : positions.keySet()) {
            gc.setFill(Color.BLUE);
            gc.fillOval(positions.get(vertex)[0] - 5, positions.get(vertex)[1] - 5, 10, 10);
            gc.setFill(Color.BLACK);
            gc.fillText(vertex, positions.get(vertex)[0] + 5, positions.get(vertex)[1] - 5);
        }
    }

    public void animateTraversal(GraphicsContext gc, Map<String, double[]> positions, List<String> path) {
        gc.setStroke(Color.RED);
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            gc.strokeLine(
                    positions.get(from)[0], positions.get(from)[1],
                    positions.get(to)[0], positions.get(to)[1]
            );
            gc.setFill(Color.RED);
            gc.fillOval(positions.get(from)[0] - 5, positions.get(from)[1] - 5, 10, 10);
            gc.fillText(from, positions.get(from)[0] + 5, positions.get(from)[1] - 5);
        }
        
        String last = path.get(path.size() - 1);
        gc.setFill(Color.RED);
        gc.fillOval(positions.get(last)[0] - 5, positions.get(last)[1] - 5, 10, 10);
        gc.fillText(last, positions.get(last)[0] + 5, positions.get(last)[1] - 5);
    }
}
