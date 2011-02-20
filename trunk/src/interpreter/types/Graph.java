package interpreter.types;

import java.util.ArrayList;

import exceptions.NodesNotConnectedException;

public class Graph implements Type {
	
	//TODO: figure out if need to make indices of nodes static (since mutating array list changes indices)
	
	private ArrayList<Node> nodes;
	private ArrayList<EdgeWrapper> edges;
	
	private double updatedSignature = 0;
	private double returnedSignature = -1;
	
	private Type[][] adjacencyMatrix;
	
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
	 */
	public void topologicalSort() {
		
	}
	
	public void bfs() {
		
	}
	
	public void dfs() {
		
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




