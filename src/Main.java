import java.io.*;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Graph g=new EdgeGraph();
        //Graph g = new AdjListGraph();
        Graph g=new Graph();
        //Graph g=new MatrixGraph();

        //   for(String s: loadStrings("src\\combi.txt")){
        for(String s: loadStrings("combi.txt")){
            String[] a= s.split(" , ");
            g.addEdge(a[0],a[1],Integer.parseInt(a[2]));
            g.addEdge(a[1],a[0],Integer.parseInt(a[2]));
        }
        //g.printGraph();
        //boolean connected = g.isConnected();

        Graph groups = g.createExclusiveGraph();
        print(g, groups);
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

    public static void print(Graph graphCourse, Graph exclusiveGraph) {
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




}