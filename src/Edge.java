class Edge{
    private Node from,to;
    private int weight;
    private boolean path = false;

    Edge(Node from,Node to,int w){this.from=from; this.to=to; weight=w;}
    public String toString(){return from.getName()+" - "+weight+" -> "+to.getName(); }
    public String getTo(){return to.getName();}
    public Node getToNode(){return to;}
    public int getWeight(){return weight;}
    public boolean getPath(){return path;}
    public void setPath(){this.path=true;}
}