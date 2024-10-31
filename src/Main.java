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
        g.printGraph();
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
}