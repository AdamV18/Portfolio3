class Edge{
    Node from,to;
    int weight;

    Edge(Node from,Node to,int w){this.from=from; this.to=to; weight=w;}
    public String toString(){return from.getName()+" - "+weight+" -> "+to.getName(); }
    public String getTo(){return to.getName();}
    public int getWeight(){return weight;}
}