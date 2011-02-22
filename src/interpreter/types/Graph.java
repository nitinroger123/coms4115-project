package interpreter.types;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.GraphNotLinearException;
import exceptions.NodesNotConnectedException;

public class Graph implements Type {
	
	//TODO: figure out if need to make indices of nodes static (since mutating array list changes indices)
	
	private ArrayList<Node> nodes;
	private ArrayList<EdgeWrapper> edges;
	
	private double updatedSignature = 0;
	private double returnedSignature = -1;
	
	private Type[][] adjacencyMatrix;
	private ArrayList<Node> linearPath = null;
	public Graph() {
		nodes = new ArrayList<Node>();
	}
	
	public void addNode(Node value) {
		nodes.add(value);
		updatedSignature = Math.random();
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
			for(Node adjacentNode : front.getAdjacent() ){
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
		
		return this; // place holder
	}
	
	public Graph dfs(Node start, Node goal) {
		return this; // place holder
		
	}
	
	/*
	 * Basically a three-tuple with added matching algorithm for searching
	 * through edges for both directed and undirected graphs
	 * 
	 * @author: Yufei Liu
	 */
	private class EdgeWrapper {
		private int first, second;
		private Type val;
		
		public EdgeWrapper(int first, int second, Type val) {
			this.first = first;
			this.second = second;
			this.val = val;
		}
		
		public int getFirst() {
			return first;
		}
		
		public int getSecond() {
			return second;
		}
		
		public void setValue(Type v) {
			val = v;
		}
		
		public Type getValue() {
			return val;
		}
		
		/*
		 * Undirected match
		 */
		public boolean check(int n1, int n2) {
			return this.checkDirected(n1,n2) && this.checkDirected(n2,n1);
		}
		
		/*
		 * Directed match
		 */
		public boolean checkDirected(int n1, int n2) {
			return first==n1 && second==n2;
		}
	}
}




