import java.io.*;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        boolean detail = true; //false if we just want the results for task 4

        //Task 1 - Generate Graph
        System.out.println("-------------------------------Begin Task 1------------------------------- \n");
        Graph graphCourse = new Graph();

        for (String s : loadStrings("combi.txt")) {
            String[] a = s.split(" , ");
            graphCourse.addEdge(a[0], a[1], Integer.parseInt(a[2]));
            graphCourse.addEdge(a[1], a[0], Integer.parseInt(a[2]));
        }
        if(detail) {graphCourse.printGraph(1);} // here can be also chosen which Order the graph should be printed - 1=Edges Order ; 2=Weight Order ; 3) Edge + Weight Order


        //Task 2 - Is Graph connected - boolean parameter to print or no the details
        System.out.println("-------------------------------Begin Task 2------------------------------- \n");
        if(detail) {boolean connected = graphCourse.isConnected(false);}

        for (int i = 1; i < 4; i++) {
            int methodvalue = i; // 1=Edges Order ; 2=Weight Order ; 3) Edge + Weight Order
            Graph graphDuplicate = graphCourse.duplicateGraph();
            System.out.println("-------------------------Method number: " + methodvalue+ " ------------------- \n");

            //Task 3 - Find Groups
            System.out.println("-------------------------------Begin Task 3------------------------------- \n");
            Graph groups = graphDuplicate.createExclusiveGraph(methodvalue);
            if(detail){printGroup(graphDuplicate, groups);}

            //Task 4 - Find Timeslots
            System.out.println("-------------------------------Begin Task 4------------------------------- \n");
            Graph bestPathGraph = null;
            int minimumWeight = Integer.MAX_VALUE;
            int startGroup = -1;

            // to calculate the path for each node as start Node -> find best solution
            for (Node startNode : groups.getAdjacencyList().keySet()) {
                Graph pathGraph = groups.calculatePathGraph(startNode);

                int pathWeight = pathGraph.calculatePathWeight();
                if(detail){System.out.println("Startgroup " + startNode.getGroup() + " has a weight of " + pathWeight);}

                if (pathWeight < minimumWeight) {
                    minimumWeight = pathWeight;
                    bestPathGraph = pathGraph;
                    startGroup = startNode.getGroup();
                }
            }

            // Print out the best solution
            if (bestPathGraph != null) {
                System.out.println("Path with the less consecutive time slots (Students: " + minimumWeight + ", StartGroup: " + startGroup + ")\n");
                printPath(bestPathGraph, graphDuplicate, startGroup);
                System.out.println("\n\n");
            } else {
                System.out.println("No Path found.");
            }
        }
    }



    static ArrayList<String> loadStrings(String f){
        ArrayList<String> list=new ArrayList<>();
        try{
            BufferedReader in=new BufferedReader(new FileReader(f));
            while(true){
                String s=in.readLine();
                if(s==null)break;
                list.add(s);
            }
            in.close();
        }catch(IOException e){
            return null;
        }
        return list;
    }

    //Print Method to print the Found Groups
    public static void printGroup(Graph graphCourse, Graph exclusiveGraph) {
        // Map to store subjects by group from the original graph (graphCourse)
        Map<Integer, List<String>> subjectsByGroup = new HashMap<>();

        // Populate the map with group IDs and corresponding subjects from graphCourse
        for (Node node : graphCourse.getAdjacencyList().keySet()) {
            int group = node.getGroup();
            subjectsByGroup.putIfAbsent(group, new ArrayList<>());
            subjectsByGroup.get(group).add(node.getName());
        }

        // Create a sorted list of group nodes in exclusiveGraph
        List<Node> sortedGroupNodes = new ArrayList<>(exclusiveGraph.getAdjacencyList().keySet());
        sortedGroupNodes.sort(Comparator.comparingInt(node -> Integer.parseInt(node.getName()))); // Sort by group ID

        // Print the exclusive graph with subjects instead of group names
        System.out.println("Exclusive Graph with subjects:");
        for (Node groupNode : sortedGroupNodes) {
            int groupId = Integer.parseInt(groupNode.getName()); // Get the group ID from exclusiveGraph

            // Get the list of subjects (courses) for the current group from graphCourse
            List<String> subjects = subjectsByGroup.getOrDefault(groupId, new ArrayList<>());

            // Print the group ID with its subjects
            System.out.println("Group " + groupId + " (" + subjects + ")");
            System.out.print("-> ");

            // Print edges in the exclusive graph
            for (Edge edge : exclusiveGraph.getAdjacencyList().get(groupNode)) {
                System.out.print("Group " + edge.getToNode().getName() + " (" + edge.getWeight() + "), ");
            }
            System.out.println("\n");

        }

    }

    //Print Method to print the final Path
    public static void printPath(Graph pathGraph, Graph kursGraph, int startGroupId) {
        // Map to store subjects by group from kursGraph
        Map<Integer, List<String>> subjectsByGroup = new HashMap<>();

        // Populate the map with group IDs and corresponding subjects from kursGraph
        for (Node node : kursGraph.getAdjacencyList().keySet()) {
            int group = node.getGroup();
            subjectsByGroup.putIfAbsent(group, new ArrayList<>());
            subjectsByGroup.get(group).add(node.getName());
        }

        // Find the starting node in pathGraph based on the startGroupId
        Node currentNode = pathGraph.getNodeByName(String.valueOf(startGroupId));
        if (currentNode == null) {
            System.out.println("Starting group not found in the path graph.");
            return;
        }

        System.out.println("Path Graph with subjects (starting from Group " + startGroupId + "):");

        // Traverse the path graph from the start node
        while (currentNode != null) {
            int groupId = Integer.parseInt(currentNode.getName());
            List<String> subjects = subjectsByGroup.getOrDefault(groupId, new ArrayList<>());

            // Print the current group and its subjects
            System.out.print("Group " + groupId + " (" + subjects + ")");

            // Get the next edge (if it exists) to move to the next group
            List<Edge> edges = pathGraph.getAdjacencyList().get(currentNode);
            if (edges != null && !edges.isEmpty()) {
                Edge edge = edges.get(0); // Get the single edge to the next group in the path
                Node nextNode = edge.getToNode();
                System.out.print(" --- " + edge.getWeight() + " ---> ");
                currentNode = nextNode; // Move to the next node
            } else {
                // If there are no more edges, we've reached the end
                currentNode = null;
            }

            System.out.println(); // New line for each group

        }
        System.out.println("Total weight of the path: " + pathGraph.calculatePathWeight());
    }

}