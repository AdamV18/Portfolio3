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
            System.out.print(node + " (" + edgeCount + " edges, group " + node.getGroup() + " ) -> ");

            // Sort edges alphabetically by the destination node's name
            List<Edge> sortedEdges = new ArrayList<>(adjacencyList.get(node));
            sortedEdges.sort(Comparator.comparing(Edge::getTo));

            for (Edge edge : sortedEdges) {
                System.out.print(edge.getTo() + "(" + edge.getWeight() + "), " );
            }
            System.out.println("\n");
        }
        System.out.println("-------------------------------End------------------------------- \n");
    }

    // Method to check if the graph is connected using the DFS (depth first search) method from Lec16
    public boolean isConnected() {
        if (adjacencyList.isEmpty()) {
            System.out.println("The graph is empty.");
            return true;
        }

        Set<Node> visitedNodes = new HashSet<>();
        Node startNode = adjacencyList.keySet().iterator().next(); // Start from any node
        System.out.println("Starting connectivity check from node: " + startNode.getName());

        // Using DFS (depth first search) traversal code
        visitDepthFirst(startNode, visitedNodes);

        // Display all visited nodes
        System.out.print("Visited nodes: ");
        for (Node node : visitedNodes) {
            System.out.print(node.getName() + " - ");
        }
        System.out.println();

        boolean isConnected = ( visitedNodes.size() == adjacencyList.size() );
        if (isConnected) {
            System.out.println("The graph is connected.");
        } else {
            System.out.println("The graph is NOT connected.");
            System.out.println("Unvisited nodes:");
            for (Node node : adjacencyList.keySet()) {
                if (!visitedNodes.contains(node)) {
                    System.out.println(" - " + node.getName());
                }
            }
        }
        return isConnected;
    }

    // Depth first search from Lec16 pdf
    private void visitDepthFirst(Node v, Set<Node> visited) {
        if (visited.contains(v)) return;

        System.out.println("Visiting node: " + v.getName());
        visited.add(v);

        for (Edge e : adjacencyList.get(v)) {
            Node neighbor = e.to;
            if (!visited.contains(neighbor)) {
                System.out.println("Visited, moving to neighbor: " + neighbor.getName());
                visitDepthFirst(neighbor, visited);
            } else {
                System.out.println("Already visited neighbor: " + neighbor.getName());
            }
        }
    }


    public void exclusiveGraph() {
        // Sort nodes in descending order of the number of edges
        List<Node> sortedNodes = new ArrayList<>(adjacencyList.keySet());
        sortedNodes.sort((node1, node2) -> Integer.compare(adjacencyList.get(node2).size(), adjacencyList.get(node1).size()));

        int currentGroup = 0;
        sortedNodes.get(0).setGroup(0);
        System.out.println(sortedNodes.get(0).getName() +" "+ sortedNodes.get(0).getGroup());

        for (int i = 1; i < sortedNodes.size()-1; i++) {


        }
    }


    public void printGroups() {
        // Create a map to store nodes by their groups
        Map<Integer, List<Node>> groups = new HashMap<>();

        // Organize nodes into groups
        for (Node node : adjacencyList.keySet()) {
            int group = node.getGroup();
            groups.putIfAbsent(group, new ArrayList<>());
            groups.get(group).add(node);
        }

        // Print each group with its nodes
        for (Map.Entry<Integer, List<Node>> entry : groups.entrySet()) {
            System.out.print("Group " + entry.getKey() + ": ");
            for (Node node : entry.getValue()) {
                System.out.print(node.getName() + " ");
            }
            System.out.println();
        }
    }



}
