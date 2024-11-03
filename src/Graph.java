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

    // Methode, to create Node and adds it to the AdjacencyList
    public void createNode(String name) {

        // is there an exisiting Node?
        for (Node node : adjacencyList.keySet()) {
            return;
        }
        // Node is not exiting, Node is created
        Node newNode = new Node(name);
        adjacencyList.put(newNode, new ArrayList<>());
        return;
    }

    // Method to add a Edge
    public void addEdge(String from, String to, int weight) {
        Node fromNode = getOrCreateNode(from);
        Node toNode = getOrCreateNode(to);

        Edge edge = new Edge(fromNode, toNode, weight);
        adjacencyList.get(fromNode).add(edge);
    }


    //       -------------------------------      Task 1      -------------------------------



    public void printGraph() {

        System.out.println("-Begin Task 1-\n");


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
        System.out.println("-------------------------------End Task 1------------------------------- \n");
    }



    //       -------------------------------      Task 2      -------------------------------


    // Method to check if the graph is connected using the DFS (depth first search) method from Lec16
    public boolean isConnected(boolean details) {

        System.out.println("-Begin Task 2-\n");

        if (adjacencyList.isEmpty()) {
            System.out.println("The graph is empty.");
            return true;
        }

        Set<Node> visitedNodes = new HashSet<>();
        Node startNode = adjacencyList.keySet().iterator().next(); // Start from any node
        System.out.println("Starting connectivity check from node: " + startNode.getName());

        // Using DFS (depth first search) traversal code
        visitDepthFirst(startNode, visitedNodes, details);

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
        System.out.println("-------------------------------End Task 2------------------------------- \n");
        return isConnected;
    }

    // Depth first search from Lec16 pdf
    private void visitDepthFirst(Node v, Set<Node> visited, boolean details) {
        if (visited.contains(v)) return;

        if(details){System.out.println("Visiting node: " + v.getName());}
        visited.add(v);

        for (Edge e : adjacencyList.get(v)) {
            Node neighbor = e.to;
            if (!visited.contains(neighbor)) {
                if(details){System.out.println("Visited, moving to neighbor: " + neighbor.getName());}
                visitDepthFirst(neighbor, visited, details);
            } else {
                if(details){System.out.println("Already visited neighbor: " + neighbor.getName());}
            }
        }
    }



    //       -------------------------------      Task 3      -------------------------------


    //generate the graph with Groups as Nodes and Edges the sum of Students between the groups
    public Graph createExclusiveGraph() {

        System.out.println("-Begin Task 3-\n");
        // First, divide the nodes in the original graph into groups
        divideGraphIntoGroups();

        Graph newGraph = new Graph();

        // Track groups that have already been added as nodes in the new graph
        Map<Integer, Node> groupNodes = new HashMap<>();

        //Create Group Nodes
        for (Node node : adjacencyList.keySet()) {
            int group = node.getGroup();

            // Check if the group node already exists in the new graph
            if (!groupNodes.containsKey(group)) {
                // Create a new node for this group in the new graph
                Node groupNode = new Node(Integer.toString(group));
                newGraph.createNode(groupNode.getName());
                newGraph.getOrCreateNode(groupNode.getName()).setGroup(group); //
                groupNodes.put(group, groupNode);
            }
        }

        // Calculate Edges between Groups
        List<Integer> processedGroups = new ArrayList<>();
        for (int group1 : groupNodes.keySet()) {
            for (int group2 : groupNodes.keySet()) {
                if (group1 < group2 && !processedGroups.contains(group2)) {
                    // use helper method to calculate NumOfStudents
                    int numberOfConnections = calculateStudentsBetweenGroups(group1, group2);

                    // add only Edge, when there is min. 1 Connection
                    if (numberOfConnections > 0) {
                        newGraph.addEdge(Integer.toString(group1), Integer.toString(group2), numberOfConnections);
                        newGraph.addEdge(Integer.toString(group2), Integer.toString(group1), numberOfConnections);
                    }
                }
            }
            processedGroups.add(group1);
        }
        System.out.println("-------------------------------End Task 3------------------------------- \n");
        return newGraph;

    }

    public void divideGraphIntoGroups() {
        // Sort nodes in descending order of the number of edges
        List<Node> sortedNodes = new ArrayList<>(adjacencyList.keySet());
        sortedNodes.sort((node1, node2) -> Integer.compare(adjacencyList.get(node2).size(), adjacencyList.get(node1).size()));

        int currentGroup = 1;
        sortedNodes.get(0).setGroup(currentGroup);
        //System.out.println(sortedNodes.get(0).getName() +" "+ sortedNodes.get(0).getGroup());

        for (int i = 1; i < sortedNodes.size(); i++) {
            int group = 1;
            while(!checkForGroup(group,sortedNodes.get(i))){
                group++;
            }
            sortedNodes.get(i).setGroup(group);
            //System.out.println(sortedNodes.get(i).getName() +" "+ sortedNodes.get(i).getGroup());
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

    //helper method to tell if a node has a connected node with the given group
    private boolean checkForGroup(int group, Node node){
        List<Edge> list  = adjacencyList.get(node);
        for (Edge edge : list) {
            if(edge.getToNode().getGroup()==group){
                return false;
            }
        }
        return true;
    }



    //helper method to calculate The weight between two groups
    private int calculateStudentsBetweenGroups(int group1, int group2) {
        List<Node> group1Nodes = new ArrayList<>();
        List<Node> group2Nodes = new ArrayList<>();

        //iterate through nodes to store groups
        for (Node node : adjacencyList.keySet()) {
            if (node.getGroup() == group1) {
                group1Nodes.add(node);
            } else if (node.getGroup() == group2) {
                group2Nodes.add(node);
            }
        }

        // Calculate, how many students are between these two groups
        int numberOfStudents = 0;
        for (Node node1 : group1Nodes) {
            for (Edge edge : adjacencyList.get(node1)) {
                if (group2Nodes.contains(edge.getToNode())) {
                    numberOfStudents+= edge.getWeight();
                }
            }
        }
        return numberOfStudents;

    }

    public HashMap<Node, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }


    // Methode, um den Pfad zu berechnen und nur die Knoten und Kanten im Pfad zurückzugeben
    /*public Graph calculatePathGraph(Node start) {

        Graph trenchGraph = duplicateGraph(); // Kopiert den ursprünglichen Graphen
        Set<Node> visitedNodes = new HashSet<>(); // Speichert die besuchten Nodes

        Node currentNode = start;

        while (currentNode != null) {
            currentNode.visit();
            //visitedNodes.add(currentNode); // Markiere den aktuellen Node als besucht

            // Finde den nächstgelegenen Nachbarn
            Node nearestNeighbor = findNearestNeighbor(currentNode);

            // Abbruch, wenn es keinen weiteren Nachbarn gibt oder der Nachbar bereits besucht wurde
            if (nearestNeighbor == null || nearestNeighbor.getVisited()) {
                break;
            }

            // Hole die Kantenliste aus trenchGraph, falls sie existiert
            List<Edge> edges = trenchGraph.getAdjacencyList().get(currentNode);
            System.out.println("Current node: " + currentNode.getName());
            System.out.println("Edges in trenchGraph for current node: " + trenchGraph.getAdjacencyList().size());

            if (edges == null) {
                //edges = new ArrayList<>(); // Initialisiere eine leere Liste, wenn keine Kanten existieren
                //trenchGraph.getAdjacencyList().put(currentNode, edges);
                System.out.println("Edge list is empty.");
                return trenchGraph;
            }

            // Behalte nur die Kante zum nächstgelegenen Nachbarn
            List<Edge> edgesToKeep = new ArrayList<>();
            for (Edge edge : edges) {
                if (edge.getTo().equals(nearestNeighbor.getName())) {
                    edgesToKeep.add(edge);
                }
            }
            trenchGraph.getAdjacencyList().put(currentNode, edgesToKeep);

            // Setze den nächsten Nachbarn als aktuellen Node für die nächste Iteration
            currentNode = nearestNeighbor;
        }

        return trenchGraph;
    }*/

    public Graph calculatePathGraph(Node start) {
        // Dupliziere den ursprünglichen Graphen
        Graph pathGraph = duplicateGraph();

        // Hole den Startknoten im duplizierten Graphen
        Node currentNode = pathGraph.getNodeByName(start.getName());

        // Set für besuchte Knoten, um Zyklen zu vermeiden
        Set<Node> visitedNodes = new HashSet<>();

        // Traverse durch den Graphen und behalte nur den Pfad
        while (currentNode != null) {
            currentNode.visit();

            // Finde den nächsten Nachbarn im Pfad
            Node nearestNeighbor = pathGraph.findNearestNeighbor(currentNode);

            if (nearestNeighbor == null || nearestNeighbor.getVisited()) {
                break;
            }

            // Nur die Kante zum nächsten Nachbarn behalten, alle anderen Kanten löschen
            pathGraph.keepEdgeToNearestNeighbor(currentNode, nearestNeighbor);

            // Entferne alle eingehenden Kanten zu currentNode, außer der Kante vom nearestNeighbor
            pathGraph.removeIncomingEdgesToCurrentNode(currentNode, nearestNeighbor);

            // Setze currentNode auf den nächsten Nachbarn
            currentNode = nearestNeighbor;
        }

        // Entferne die letzte verbleibende Kante, wenn es eine gibt
        if (currentNode != null) {
            pathGraph.removeLastEdgeToCurrentNode(currentNode);
        }

        // Gib den resultierenden Pfadgraphen zurück
        return pathGraph;
    }


    public int calculatePathWeight() {
        int totalWeight = 0;

        // Iteriere über alle Knoten im Graphen
        for (Node node : adjacencyList.keySet()) {
            List<Edge> edges = adjacencyList.get(node);

            // Addiere die Gewichte der Kanten, die als Teil des Pfads markiert sind
            for (Edge edge : edges) {
                if (edge.getPath()) {  // Nur Kanten im Pfad berücksichtigen
                    totalWeight += edge.getWeight();
                }
            }
        }

        //System.out.println("Total weight of the path: " + totalWeight);
        return totalWeight;
    }


    // help method, to just keep edge to nearest neighbor from current node
    private void keepEdgeToNearestNeighbor(Node currentNode, Node nearestNeighbor) {
        List<Edge> edges = adjacencyList.get(currentNode);
        List<Edge> edgesToKeep = new ArrayList<>();

        for (Edge edge : edges) {
            if (edge.getToNode().equals(nearestNeighbor)) {
                edge.setPath();  //mark as part of path
                edgesToKeep.add(edge);
            }
        }

        adjacencyList.put(currentNode, edgesToKeep);
    }

    // helpmethod, to delete all incoming edges to current Node
    private void removeIncomingEdgesToCurrentNode(Node currentNode, Node nearestNeighbor) {
        for (Node node : adjacencyList.keySet()) {
            if (!node.equals(currentNode) && !node.equals(nearestNeighbor)) {
                List<Edge> nodeEdges = adjacencyList.get(node);
                if (nodeEdges != null) {
                    nodeEdges.removeIf(edge -> edge.getToNode().equals(currentNode) && !edge.getPath());
                }
            }
        }
    }

    // helper method to remove last edge to current node
    private void removeLastEdgeToCurrentNode(Node currentNode) {
        for (Node node : adjacencyList.keySet()) {
            List<Edge> nodeEdges = adjacencyList.get(node);
            if (nodeEdges != null) {
                nodeEdges.removeIf(edge -> edge.getToNode().equals(currentNode));
            }
        }
    }


    // Methode, um den gesamten Graphen zu kopieren
    public Graph duplicateGraph() {
        Graph copyGraph = new Graph();

        // Kopiere Knoten und Kanten
        for (Node node : adjacencyList.keySet()) {
            copyGraph.createNode(node.getName());
            for (Edge edge : adjacencyList.get(node)) {
                copyGraph.addEdge(node.getName(), edge.getTo(), edge.getWeight());
            }
        }
        return copyGraph;
    }

    // Method to find the nearest neighbor of a given node based on edge weight
    public Node findNearestNeighbor(Node start) {
        Node nearestNeighbor = null;
        int minimumWeight = Integer.MAX_VALUE;

        for (Edge edge : adjacencyList.get(start)) {
            if(!edge.getToNode().getVisited()){
                if (edge.getWeight() < minimumWeight) {
                    minimumWeight = edge.getWeight();
                    nearestNeighbor = edge.getToNode();
                }
            }
        }

        return nearestNeighbor;
    }

    // Methode, um einen Node anhand seines Namens abzurufen
    public Node getNodeByName(String name) {
        for (Node node : adjacencyList.keySet()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null; // Falls kein Knoten mit dem angegebenen Namen existiert
    }
}