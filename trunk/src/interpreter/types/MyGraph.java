package interpreter.types;


import helper.DisplayGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Graph Class to handle all the graph functions 
 * @author nitin, mohan, jacob
 *
 */
public class MyGraph implements Type{
    private List<Node> nodes;
    private List<Edge> edges;
    private Integer numberOfNodes;
    private Integer numberOfEdges;
    private Double adj[][];
    private Integer index;
    public MyGraph(){
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
        adj = new Double[1000][1000];
        this.index = 0;
    }

    /**
     * Returns a 2D Double array which represents the adjacency matrix.
     * @return
     */
    public Double[][] getAdjMatrix(){
        for(int i=0;i<numberOfNodes;i++){
            for(int j=0;j<numberOfNodes;j++){
                adj[i][j] =0.0;
            }
        }
        for(Edge e : edges){
            Node n1 = e.getFirstNode();
            Node n2 = e.getSecondNode();
            Double val = e.cost;
            adj[n1.index][n2.index] = val;
        }
        return adj;
    }

    /**
     * Constructor which accepts a graph as input and does a copy of it.
     * @param other
     */
    public MyGraph(MyGraph other){
        this.nodes = other.nodes;
        this.edges = other.edges;
        this.numberOfNodes = nodes.size();
        this.numberOfEdges = edges.size();

        adj = new Double[1000][1000];
        this.index = 0;
    }

    /**
     * Adds a node to the graph
     * @param n
     */
    public void addNode2(Node n){
        n.index = index;
        nodes.add(n);
        this.numberOfNodes ++;
        index ++;
    }

    /**
     * Adds a node to the graph
     * @param n
     */
    public void addNode(NumberType value){
        Node n = new Node(value);
        addNode2(n);
    }
    
    /**
     * Adds the list of nodes to this graph.
     * @param nodes
     */
    public void addAllNodes(List<Node> nodes){
        this.nodes.addAll(nodes);
        numberOfNodes += nodes.size();
    }

    /**
     * Adds a new edge to the Graph.
     * @param e
     */
    public void addEdge(NumberType t1, NumberType t2, NumberType t3){
        Edge e = new Edge(new Node(t1), new Node(t2), t3);
        addEdge2(e);
    }
    
    /**
     * Adds a new edge to the Graph.
     * @param e
     */
    public void addEdge2(Edge e){
        edges.add(e);
        e.getFirstNode().addAdjacentNode(e.getSecondNode());
        e.getSecondNode().addAdjacentNode(e.getFirstNode());
        this.numberOfEdges ++;
    }

    /**
     * Removes the edge from the graph.
     * @param e
     */
    public void removeEdge(Edge e){
        edges.remove(e);
        e.getFirstNode().removeAdjacentNode(e.getSecondNode());
        e.getSecondNode().removeAdjacentNode(e.getFirstNode());
        this.numberOfEdges--;
    }

    /**
     * Adds the list of edges to the graph.
     * @param edges
     */
    public void addAllEdges(List<Edge> edges){
        for(Edge e: edges) {
            addEdge2(e);
        }
    }

    /**
     * adds a directed edge from node1 to node2
     * @param e
     */
    public void addDirectedEdge(Edge e){
        edges.add(e);
        e.getFirstNode().addAdjacentNode(e.getSecondNode());
        this.numberOfEdges ++;
    }

    /**
     * Returns the number of nodes present in this graph.
     * @return
     */
    public int getNumberOfNodes(){
        return numberOfNodes;
    }

    /**
     * Returns the number of edges present in this graph.
     * @return
     */
    public int getNumberOfEdges(){
        return numberOfEdges;
    }

    public MyGraph bfs(NumberType startNode) {
        Node n = nodes.get((int)startNode.getDouble());
        return bfs2(n);
    }
    
    /**
     * Call the bfs with the copy of the start node.
     * Returns the resulting bfs graph. 
     * @param start
     * @return
     */
    public MyGraph bfs2(Node start) {
        MyGraph bfsGraph = new MyGraph();
        ArrayList<Node> queue = new ArrayList<Node>();
        HashMap<Double, Boolean> seen = new HashMap<Double, Boolean>();
        seen.put(start.getID(), true);
        queue.add(start);
        start.setDistance(0);
        start.setParent(null);
        bfsGraph.addNode2(start);
        while(!queue.isEmpty()){
            Node front = queue.remove(0);
            ArrayList<Node> adjacent = new ArrayList<Node>(front.getAdjacent());
            for(Node adjNode :adjacent){
                if(!seen.containsKey(adjNode.getID())){
                    adjNode.setDistance(front.getDistance()+1);
                    adjNode.setParent(front);
                    bfsGraph.addNode2(adjNode);
                    bfsGraph.addEdge2(new Edge(front, adjNode, 1.0));
                    queue.add(adjNode);
                    seen.put(adjNode.getID(), true);
                }
            }

        }
        return bfsGraph;
    }

    public MyGraph bfs(NumberType startNode, NumberType goalNode) {
        return bfs(nodes.get(startNode.getInt()), nodes.get(goalNode.getInt()));
    }
    
    /**
     * Call the bfs with the copy of the start node and copy of the goal node.
     * Returns the resulting bfs graph. 
     * @param start
     * @return
     */
    public MyGraph bfs(Node start, Node goal) {
        MyGraph bfsGraph = new MyGraph();
        ArrayList<Node> queue = new ArrayList<Node>();
        HashMap<Double, Boolean> seen = new HashMap<Double, Boolean>();
        seen.put(start.getID(), true);
        queue.add(start);
        start.setDistance(0);
        start.setParent(null);
        bfsGraph.addNode2(start);
        while(!queue.isEmpty()){
            Node front = queue.remove(0);
            ArrayList<Node> adjacent = new ArrayList<Node>(front.getAdjacent());
            for(Node adjNode :adjacent){
                if(!seen.containsKey(adjNode.getID())){
                    adjNode.setDistance(front.getDistance()+1);
                    adjNode.setParent(front);
                    bfsGraph.addNode2(adjNode);
                    bfsGraph.addEdge2(new Edge(front, adjNode, 1.0)); //1.0 is a dummy cost
                    queue.add(adjNode);
                    seen.put(adjNode.getID(), true);
                    /**
                     * We have reached our goal, Now break
                     */
                    if(adjNode.getID() == goal.getID()){
                        return bfsGraph;
                    }
                }
            }

        }
        return bfsGraph;
    }

    /**
     * Prints nodes in index order
     */
    public StringType printNodes(){
        String output="";
        for(Node node : nodes){
            output+=(node.getValue()+" ");
        }
        return new StringType(output);
    }

    /**
     * Returns the miniumum spanning tree; Uses Kruskal's algo.
     * @return
     */
    public MyGraph minimumSpanningTree(){
        // initialize a priority queue for the MST
        PriorityQueue<Edge> queue = new PriorityQueue<Edge>(); 
        queue.addAll(edges); 
        // initialize an array list for the edges of the MST
        ArrayList<Edge> edgesMST = new ArrayList<Edge>(); 
        HashMap<String, Integer> map = new HashMap<String, Integer>(); 
        Integer counter = 1;
        for(Node node: nodes) {
            map.put(node.id.toString(), counter); 
            counter++;
        }
        while(!queue.isEmpty()) { 
            Edge e = queue.remove();
            Integer idx1 = map.get(e.node2.id.toString());
            Integer idx2 = map.get(e.node1.id.toString());
            if(!idx1.equals(idx2)) {
                edgesMST.add(e);
                for(String node: map.keySet()) {
                    if(map.get(node).equals(idx2)) {
                        map.put(node, idx1);
                    }
                }
            }
        }
        for(Edge e: edgesMST) {
            System.out.println(e.node1.toString() + " " + e.node2.id.toString() + " " + e.cost); 
        }
        System.out.println("mst!");
        MyGraph mst = new MyGraph();
        mst.nodes = this.nodes;
        mst.edges = edgesMST;
        return mst;
    }

    /**
     * Returns a Topologically sorted Graph. The ordering of the nodes is in Topological Order.
     * @return Graph with nodes in topological fashion, nn empty graph if the graph has a cycle
     */
    public MyGraph topologicalSort() {
        Queue<Node> nodesList = new LinkedList<Node>(nodes);
        // Remove all nodes with incoming edges
        for(Node n: nodes) {
            nodesList.removeAll(n.getAdjacent());
        }
        MyGraph graph = new MyGraph();
        Node node1;
        Set<Edge> tempEdgesSet = new HashSet<Edge>(edges);
        List<Node> graphList = new ArrayList<Node>(); 
        while(!nodesList.isEmpty()) {
            node1 = nodesList.remove();
            graphList.add(node1);
            for(Node node2: node1.getAdjacent()) {
                tempEdgesSet.remove(getEdge(node1, node2));
                boolean hasIncoming = false;
                for(Edge edge: tempEdgesSet) {
                    if(edge.getSecondNode().equals(node2)) {
                        hasIncoming = true;
                    }
                }
                if(!hasIncoming) {
                    graphList.add(node2);
                }
            }
        }
        if(!tempEdgesSet.isEmpty()) {
            // Graph has cycle.
            return graph;
        }
        graph.addAllNodes(graphList);
        return graph;
    }

    /*
     * Helper method which retrieves the edge between two nodes.
     */
    private Edge getEdge(Node fromNode, Node toNode) {
        for(Edge e: edges) {
            if(e.getFirstNode().equals(fromNode) && e.getSecondNode().equals(toNode))
                return e;
        }
        return null;
    }

    /**
     * Returns the list of edges.
     * @return
     */
    public ArrayList<Edge> getEdgeList(){
        return (ArrayList<Edge>)edges;
    }

    public MyGraph dfs(NumberType startIndex, NumberType goalIndex) {
        return dfs2(nodes.get(startIndex.getInt()), nodes.get(goalIndex.getInt()));
    }
    
    /**
     * Implemented a DFS from the start node to the goal node.
     * @param start
     * @param goal
     * @return
     */
    public MyGraph dfs2(Node start, Node goal) {
        Set<Node> visitedNodeSet = new HashSet<Node>();
        Stack<Node> stack = new Stack<Node>();
        Stack<Node> dfsStack = new Stack<Node>();
        MyGraph graph = new MyGraph();
        Node head = new Node(start.getContents());
        head.setParent(null); 
        stack.add(start);
        graph.addNode2(start);
        dfsStack.add(head);
        Node temp, childNode;
        Edge e;
        while(!stack.isEmpty()) {
            temp = stack.pop();
            if(visitedNodeSet.contains(temp)) {
                continue;
            }
            childNode = new Node(temp.getContents());
            if(!temp.equals(start)) {
                childNode.setParent(dfsStack.peek());
                e = new Edge(childNode.getParent(), childNode, 1.0);
                dfsStack.add(childNode);
                graph.addEdge2(e);
            }
            // We have reached our destination.
            if(temp.equals(goal)) {
                break;
            }
            visitedNodeSet.add(temp);
            for(Node n: temp.getAdjacent()) {
                n.setParent(temp);
            }
            stack.addAll(temp.getAdjacent());
        }
        dfsStack.remove(0);
        graph.addAllNodes(dfsStack);
        return graph;
    }

    /**
     * 
     */
    public boolean hasCycles(){
        // place holder
        return true;
    }

    /**
     * Returns the node specified by the index
     * @param i
     * @return
     */
    public Node getNodeByInt(int i){
        return nodes.get(i);
    }

    /**
     * Mimics the getNodeByInt method, needed for the backend
     * @param i
     * @return
     */
    public Node getNode(NumberType i) {
        return getNodeByInt((int)i.getDouble());
    }

    /**
     * Simply creates "linearPath" in the order that the nodes exist in the nodes list
     */
    //  public MyGraph linearize(){
    //          linearPath = new ArrayList<Node>();
    //          for(Node n: nodes){
    //                  linearPath.add(n);
    //          }
    //          return this;
    //  }

    /**
     * List Constructor
     */
    public MyGraph(List<Type> listElements){
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
        adj = new Double[1000][1000];
        this.index = 0;
        for(Type elem: listElements){
            Node n = new Node(elem);
            addNode2(n);
        }
    }

    /**
     * Adds a node as an element at the front of the list. 
     */
    public void listAddFront(Type value){
        Node listElem = new Node(value);
        nodes.add(0,listElem);
        //linearPath.add(0,listElem);
    }

    /**
     * Adds a node as an element of the list
     */
    public void listAddEnd(Type value){
        Node listElem = new Node(value);
        nodes.add(listElem);
        //linearPath.add(listElem);
    }



    /**
     * gets value
     */
    public Type listGet(int i){
        return nodes.get(i);
    }

    /**
     * sets value
     */
    public void listSet(int i, Type v){
        nodes.get(i).setValue(v);
    }

    /**
     * gets tag value
     */
    public Type listGet(int i, String tag){
        return nodes.get(i).getTag(tag);
    }

    /**
     * sets tag value
     */
    public void listSet(int i, String tag, Type v){
        nodes.get(i).setTag(tag, v);
    }

    /**
     * Adds a tag of the same name to all nodes. Does not set value
     */
    public void addUniTag(String name){
        for(Node n: nodes){
            n.setTag(name, null);
        }
    }

    /**
     * Adds a tag of the same name and type at given values for all nodes
     */
    public void setUniTag(String name, ArrayList<Type> vals){
        for(int i = 0; i < nodes.size(); i++){
            nodes.get(i).setTag(name, vals.get(i));
        }
    }

    /**
     * Returns a tag of the given name for all nodes that have it
     */
    //  public ArrayList<Type> getUniTag(String name){
    //          ArrayList<Type> tagVals = new ArrayList<Type>();
    //          for(int i = 0; i < nodes.size(); i++){
    //                  tagVals.add(nodes.get(i).getTag(name));
    //          }
    //          return tagVals;
    //  

    /**
     * Adds a tag of the same name and type at the given (vector) values for all nodes
     */
    public void setUniTag(String name, Vector vec){
        for(int i = 0; i < nodes.size(); i++){
            nodes.get(i).setTag(name, vec.get(i));
        }
    }

    /**
     * Returns a vector the PageRank each nodes
     */
    public Vector getPageRank(){
        int n = getNumberOfNodes();
        ArrayList<NumberType> invDegrees = new ArrayList<NumberType>();
        Matrix adjMat = new Matrix(this);
        for(int i = 0; i < n; i++){
            int sum = 0;
            for(int j = 0; j < n; j++){
                sum += adjMat.get(i,j).getDouble();
            }
            invDegrees.add(new NumberType(1/(double)sum));
        }

        Vector prOld = new Vector(n, new NumberType(1/(double)n));
        Vector prNew = new Vector(n, new NumberType(1/(double)n));
        Vector invDeg = new Vector(invDegrees);

        Matrix googleMatrix = new Matrix(adjMat);
        double dampFactor = 0.15;
        int iters = 0;
        while( 1 - prOld.normalize().dot(prNew.normalize()).getDouble() > 0.001 || iters < 100){
            Vector uniform = new Vector(n, new NumberType(1/(double)n));
            prOld = new Vector(prNew);
            //System.out.println(prOld.normalize().dot(prNew.normalize()).getValue());
            prOld.print();
            prNew = googleMatrix.vecMult((new Matrix(invDeg)).vecMult(prOld)).scalarMult(new NumberType(1-dampFactor)).add(uniform.scalarMult(new NumberType(dampFactor)));
            iters++;
        }

        return prOld;
    }

    /**
     * method to display the graph 
     */
    public void visualize(){
        DisplayGraph g = new DisplayGraph(this);
        g.setVisible(true);
    }

    /**
     * Visualizes the graph with the specified edges
     * @param specificEdges
     */
    public void visualize(ArrayList<Edge> specificEdges){
        MyGraph temp = new MyGraph(this);
        while(temp.edges.size() != 0){
            temp.removeEdge(temp.edges.get(temp.getNumberOfEdges()-1));
        }
        temp.addAllEdges(specificEdges);
        DisplayGraph g = new DisplayGraph(temp);
        g.setVisible(true);
    }

    /**
     * Traverses the nodes and applies the function passed in
     * @param f
     */
    public void traverseNodes(Function f){
        for(Node n: this.nodes){
            n.applyFunction(f);
        }
    }

    /**
     * Gets the all pairs shortest part adjacency matrix.
     * @return
     */
    public Double[][] allPairsShortestPath(){
        Double [][] adj = this.getAdjMatrix();
        for(int k = 0 ; k< numberOfNodes; k++){
            for(int i=0;i<numberOfNodes;i++){
                for(int j=0;j<numberOfNodes;j++){
                    adj[i][j] = Math.min(adj[i][j], adj[i][k]+adj[k][j]);
                }
            }
        }
        return adj;
    }

    @Override
    public String getValue() {
        String s="[";
        for(Node n: nodes){
            s=s+n.getValue();
        }
        s=s+"]";
        return s;
    }

}
