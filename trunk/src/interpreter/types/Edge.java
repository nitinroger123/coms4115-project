package interpreter.types;

import interpreter.types.*;

public class Edge {
	private Node node1;
	private Node node2;
	private Type value;
	 
	/**
	 * creates an undirected edge between node1 and node 2
	 * @param node1
	 * @param node2
	 * @param value
	 */
	public Edge(Node node1, Node node2, Type value ){
		this.node1 = node1;
		this.node2 = node2;
		this.value = value;
	}
	
	/**
	 * returns the edge label
	 * @return
	 */
	public Type getValue(){
		return value;
	}
	
	/**
	 * returns the first edge
	 * @return
	 */
	public Node getFirstNode(){
		return node1;
	}
	
	/**
	 * Returns the second node of the edge
	 * @return
	 */
	public Node getSecondNode(){
		return node2;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node1 == null) ? 0 : node1.hashCode());
		result = prime * result + ((node2 == null) ? 0 : node2.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (node1 == null) {
			if (other.node1 != null)
				return false;
		} else if (!node1.equals(other.node1))
			return false;
		if (node2 == null) {
			if (other.node2 != null)
				return false;
		} else if (!node2.equals(other.node2))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}
