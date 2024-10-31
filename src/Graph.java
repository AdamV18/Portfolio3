import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
    private HashMap<Node, List<Edge>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    // Methode, to get either an exisiting Node or generates one and adds it to the AdjacencyList
    public Node getOrCreateNode(String name) {

        // is there an exisiting Node?
        for (Node node : adjacencyList.keySet()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        // Node is not exiting, Node is created
        Node newNode = new Node(name);
        adjacencyList.put(newNode, new ArrayList<>());
        return newNode;
    }

    // Method to add a Edge
    public void addEdge(String from, String to, int weight) {
        Node fromNode = getOrCreateNode(from);
        Node toNode = getOrCreateNode(to);

        Edge edge = new Edge(fromNode, toNode, weight);
        adjacencyList.get(fromNode).add(edge);
    }


    public void printGraph() {
        for (Node node : adjacencyList.keySet()) {
            System.out.print(node + " -> ");
            for (Edge edge : adjacencyList.get(node)) {
                System.out.print(edge.GetTo() + "(" + edge.GetWeight()+"), ");
            }
            System.out.println();
        }
    }
}
