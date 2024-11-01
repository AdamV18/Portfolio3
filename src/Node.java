public class Node {
    private String name;
    private int group =-1;
    private boolean visited;

    Node(String s){name=s;}

    public String toString(){return name;}
    public String getName(){return name;}
    public void setGroup(int g){group=g;}
    public int getGroup(){return group;}

    public boolean getVisited() {
        return visited;
    }
    public void visit() {
        visited = true;
    }
}