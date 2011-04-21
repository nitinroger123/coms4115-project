package interpreter.types;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * new graph class 
 * @author nitin
 *
 */
public class MyGraph {
    private List<Node> nodes;
    private List<Edge> edges;
    private Integer numberOfNodes;
    private Integer numberOfEdges;

    public MyGraph(){
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
        this.numberOfNodes = 0;
        this.numberOfEdges = 0;
    }

    public void addNode(Node n){
        nodes.add(n);
        this.numberOfNodes ++;
    }

    public void addAllNodes(List<Node> nodes){
        this.nodes.addAll(nodes);
    }

    public void addEdge(Edge e){
        edges.add(e);
        e.getFirstNode().addAdjacentNode(e.getSecondNode());
        e.getSecondNode().addAdjacentNode(e.getFirstNode());
        this.numberOfEdges ++;
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

    public int getNumberOfNodes(){
        return numberOfNodes;
    }

    public int getNumberOfEdges(){
        return numberOfEdges;
    }

    /**
     * Call the bfs with the copy of the start node.
     * Returns the resulting bfs graph. 
     * @param start
     * @return
     */
    public MyGraph bfs(Node start) {
        MyGraph bfsGraph = new MyGraph();
        ArrayList<Node> queue = new ArrayList<Node>();
        HashMap<Double, Boolean> seen = new HashMap<Double, Boolean>();
        seen.put(start.getID(), true);
        queue.add(start);
        start.setDistance(0);
        start.setParent(null);
        bfsGraph.addNode(start);
        while(!queue.isEmpty()){
            Node front = queue.remove(0);
            ArrayList<Node> adjacent = new ArrayList<Node>(front.getAdjacent());
            for(Node adjNode :adjacent){
                if(!seen.containsKey(adjNode.getID())){
                    adjNode.setDistance(front.getDistance()+1);
                    adjNode.setParent(front);
                    bfsGraph.addNode(adjNode);
                    bfsGraph.addEdge(new Edge(front, adjNode, null));
                    queue.add(adjNode);
                    seen.put(adjNode.getID(), true);
                }
            }

        }
        return bfsGraph;
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
        bfsGraph.addNode(start);
        while(!queue.isEmpty()){
            Node front = queue.remove(0);
            ArrayList<Node> adjacent = new ArrayList<Node>(front.getAdjacent());
            for(Node adjNode :adjacent){
                if(!seen.containsKey(adjNode.getID())){
                    adjNode.setDistance(front.getDistance()+1);
                    adjNode.setParent(front);
                    bfsGraph.addNode(adjNode);
                    bfsGraph.addEdge(new Edge(front, adjNode, null));
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
    public String printNodes(){
        String output="";
        for(Node node : nodes){
            output+=(node.getValue()+" ");
        }
        return output;
    }

    /**
     * TODO
     * @return
     */
    public MyGraph minimumSpanningTree(){
        //place holder
        return this;
    }
    
    /**
     * Returns a Topologically sorted Graph. The ordering of the nodes is in Topological Order.
     * @return
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
            // TODO Handle this more elegantly, throw exception of cycle etc.
            // Graph has cycle.
            return graph;
        }
        graph.addAllNodes(graphList);
        return graph; // place holder
    }

    private Edge getEdge(Node fromNode, Node toNode) {
        for(Edge e: edges) {
            if(e.getFirstNode().equals(fromNode) && e.getSecondNode().equals(toNode))
                return e;
        }
        return null;
    }
    
    /**
     * TODO
     * @param start
     * @param goal
     * @return
     */
    public MyGraph dfs(Node start, Node goal) {
        return this; // place holder

    }

    /**
     * TODO
     */
    public boolean hasCycles(){
        // place holder
        return true;
    }
    
    public Node getNode(int i){
    	return nodes.get(i);
    }

    /**
     * Simply creates "linearPath" in the order that the nodes exist in the nodes list
     */
    //	public MyGraph linearize(){
    //		linearPath = new ArrayList<Node>();
    //		for(Node n: nodes){
    //			linearPath.add(n);
    //		}
    //		return this;
    //	}

    /**
     * List Constructor
     */
    public MyGraph(List<Type> listElements){
        nodes = new ArrayList<Node>();
        for(Type elem: listElements){
            listAddEnd(elem);
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
    //	public ArrayList<Type> getUniTag(String name){
    //		ArrayList<Type> tagVals = new ArrayList<Type>();
    //		for(int i = 0; i < nodes.size(); i++){
    //			tagVals.add(nodes.get(i).getTag(name));
    //		}
    //		return tagVals;
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
		ArrayList<Number> degrees = new ArrayList<Number>();
		Matrix adjMat = new Matrix(this);
		for(int i = 0; i < n; i++){
			int sum = 0;
			for(int j = 0; j < n; j++){
				sum += adjMat.get(i,j).getDouble();
			}
			degrees.add(new Number(sum));
		}
		
		ArrayList<Number> temp = new ArrayList<Number>();
		for(int i = 0; i < n; i++){
			temp.add(new Number(1/n));
		}
		Vector prOld = new Vector(temp);
		Vector prNew = new Vector(prOld);
		
		Matrix googleMatrix = new Matrix(adjMat);
		
		
		return prNew;
	}
	
}
