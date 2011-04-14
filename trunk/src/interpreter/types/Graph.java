package interpreter.types;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.GraphNotLinearException;
import exceptions.NodesNotConnectedException;

/**
 * The Graph class does triple-duty.
 * 1) It represents Graphs as a list of nodes each with lists of adjacent nodes, 
 * 2) It represents Graphs as adjacency matrices
 * 3) with the linearPath field, it can represent a simple list that can also work as a double-ended queue
 */
public class Graph implements Type {
	
	//TODO: figure out if need to make indices of nodes static (since mutating array list changes indices)
	
	private ArrayList<Node> nodes;
	private ArrayList<EdgeWrapper> edges;
	private int numberOfNodes;
	private double updatedSignature = 0;
	private double returnedSignature = -1;
	
	private Type[][] adjacencyMatrix;
	private ArrayList<Node> linearPath = null;
	public Graph() {
		nodes = new ArrayList<Node>();
		this.numberOfNodes = 0;
	}
	
	
	
	public int getNumberOfNodes(){
		return numberOfNodes;
	}
	public void addNode(Node value) {
		nodes.add(value);
		updatedSignature = Math.random();
		this.numberOfNodes ++;
	}
	
	
	
	/*
	 * Removes a node as an element of the list
	 */
	public void listRemove(Node n){
		nodes.remove(n);
		linearPath.remove(n);
	}
	
	public void removeNode(int id) {
		nodes.remove(id);
		updatedSignature = Math.random();
	}
	
	public Node getNode(int id) {
		return nodes.get(id);
	}
	
	/**
	 * Get the linear path of the graph. 
	 */
	public void findLinearPath(){
		Graph temp= this;
		temp=temp.topologicalSort();
		/** TODO
		 Iterate through the edges and store the nodes in 
		   linear order if one exists. Populate linerPath.
		 */
		
	}
	
	public void print() throws GraphNotLinearException{
		findLinearPath();
		if(linearPath!=null){
			for(Node n: linearPath){
				System.out.println(n.getValue()+"-->");
			}
			return;
		}
		throw new GraphNotLinearException("The Graph is not linear");
	}
	
	public void addEdge(int node1, int node2, Type value) {
		edges.add(new EdgeWrapper(node1,node2,value));
		updatedSignature = Math.random();
	}
	
	public void removeEdge(int node1, int node2) throws NodesNotConnectedException {
		for (int i=0; i<edges.size(); i++) {
			if (edges.get(i).check(node1, node2)) edges.remove(i);
		}
		
		throw new NodesNotConnectedException("Nodes " + node1 + " and " + node2 + " are not connected!");
	}
	
	public void changeEdge(int node1, int node2, Type value) throws NodesNotConnectedException {
		for (EdgeWrapper e : edges) {
			if (e.check(node1, node2)) {
				e.setValue(value);
				return;
			}
		}
		
		throw new NodesNotConnectedException("Nodes " + node1 + " and " + node2 + " are not connected!");
	}
	
	public Type getEdge(int node1, int node2) throws NodesNotConnectedException {
		for (EdgeWrapper e : edges) {
			if (e.check(node1, node2)) return e.getValue();
		}
		
		throw new NodesNotConnectedException("Nodes " + node1 + " and " + node2 + " are not connected!");
	}
	
	/*
	 * Convert graph data into adjacency matrix, uses lazy evaluation
	 * so don't worry about calling this method multiple times
	 * NOTE: null is placed where no edge exists
	 */
	public Type[][] getAdjacencyMatrix() {
		
		if (updatedSignature==returnedSignature) return adjacencyMatrix;
		
		adjacencyMatrix = new Type[nodes.size()][nodes.size()];
		for (int i=0; i<nodes.size(); i++) {
			for (int j=0; j<nodes.size(); j++) {
				try {
					adjacencyMatrix[i][j] = this.getEdge(i, j);
				} catch (NodesNotConnectedException e) {
					adjacencyMatrix[i][j] = null;
				}
			}
		}
		
		returnedSignature = updatedSignature;
		return adjacencyMatrix;
	}
	
	/*
	 * TODO: had some problems with the design, will re-visit
	 * Nitin: Shouldn't top sort , dfs and BFS return a Graph?
	 */
	public Graph topologicalSort() {
		return this; // place holder
		
	}
	/**
	 * Call the bfs with the copy of the start node and the copy of the goal Node.
	 * Returns the resulting bfs graph. 
	 * @param start
	 * @param goal
	 * @return
	 */
	public Graph bfs(Node start , Node goal) {
		boolean pathFound = false;
		ArrayList<Node> queue = new ArrayList<Node>();
		HashMap<Node, Node> seen = new HashMap<Node, Node>();
		seen.put(start,start);
		queue.add(start);
		start.setDistance(0);
		start.setParent(null);
		while(queue.size()>0){
			if(pathFound) break;
			Node front = queue.get(0);
			ArrayList<Node> adjacent = new ArrayList<Node>(front.getAdjacent());
			for(Node adjacentNode : adjacent ){
				if(adjacentNode.equals(goal)){
					adjacentNode.setDistance(front.getDistance());
					adjacentNode.setParent(front);
					seen.put(adjacentNode,adjacentNode);
					pathFound=true;
				}
				if(seen.get(adjacentNode) == null){
					adjacentNode.setDistance(front.getDistance());
					adjacentNode.setParent(front);
					seen.put(adjacentNode, adjacentNode);
					queue.add(adjacentNode);
				}
			}
			queue.remove(0);
		}
		ArrayList<Node> bfsOrder = new ArrayList<Node>();
		Node current = goal;
		while(current != null){
			bfsOrder.add(current);
			current = seen.get(current).getParent();
		}
		for(int i=bfsOrder.size() - 1 ; i>=0 ; i--){
			System.out.println(bfsOrder.get(i).getID());
			
		}
		return this; // place holder
	}
	

	/**
	 * TODO
	 * @return
	 */
	public Graph minimumSpanningTree(){
		//place holder
		return this;
	}
	/**
	 * TODO
	 * @param start
	 * @param goal
	 * @return
	 */
	public Graph dfs(Node start, Node goal) {
		return this; // place holder
		
	}
	
	/**
	 * TODO
	 */
	public boolean hasCycles(){
		// place holder
		return true;
	}
	
	/**
	 * Simply creates "linearPath" in the order that the nodes exist in the nodes list
	 */
	public Graph linearize(){
		linearPath = new ArrayList<Node>();
		for(Node n: nodes){
			linearPath.add(n);
		}
		return this;
	}
	
	/**
	 * List Constructor
	 */
	public Graph(ArrayList<Type> listElements){
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
		linearPath.add(0,listElem);
	}
	
	/**
	 * Adds a node as an element of the list
	 */
	public void listAddEnd(Type value){
		Node listElem = new Node(value);
		nodes.add(listElem);
		linearPath.add(listElem);
	}
	
	/**
	 * Removes first node as element of list
	 */
	public Type listRemoveFront(){
		nodes.remove(nodes.size() -1);
		Node n = linearPath.remove(nodes.size() -1);
		return n.getValue();
	}
	
	/**
	 * Removes last node as element of list
	 */
	public Type listRemoveEnd(){
		nodes.remove(0);
		Node n = linearPath.remove(0);
		return n.getValue();
	}
	
}




