import java.util.*;

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
        // Sort nodes in descending order of the number of edges
        List<Node> sortedNodes = new ArrayList<>(adjacencyList.keySet());

        //Collections.sort(sortedNodes, Comparator.comparing(Node::getName)); //Alphabetical order
        sortedNodes.sort((node1, node2) -> Integer.compare(adjacencyList.get(node2).size(), adjacencyList.get(node1).size())); //Edges order

        for (Node node : sortedNodes) {
            int edgeCount = adjacencyList.get(node).size();
            System.out.print(node + " (" + edgeCount + " edges) -> ");

            // Sort edges alphabetically by the destination node's name
            List<Edge> sortedEdges = new ArrayList<>(adjacencyList.get(node));
            sortedEdges.sort(Comparator.comparing(Edge::GetTo));

            for (Edge edge : sortedEdges) {
                System.out.print(edge.GetTo() + "(" + edge.GetWeight() + "), ");
            }
            System.out.println("\n");
        }
    }
}
