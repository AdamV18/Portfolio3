class Edge{
    Node from,to;
    int weight;

    Edge(Node from,Node to,int w){this.from=from; this.to=to; weight=w;}
    public String toString(){return from.name+" - "+weight+" -> "+to.name; }
    public String GetTo(){return to.getName();}
    public int GetWeight(){return weight;}
}