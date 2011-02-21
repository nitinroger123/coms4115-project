package interpreter.types;

import java.util.ArrayList;

public class Node implements Type {
	private Type value;
	private ArrayList<Node> adjacentNodes;
	
	/**
	 * Constructor to create a new node with a set value and set its adj nodes
	 * @param value
	 * @param adjacentNodes
	 */
	public Node(Type value, ArrayList<Node> adjacentNodes){
		this.value=value;
		this.adjacentNodes=adjacentNodes;
	}
	/**
	 * returns the nodes adjacent to this particular node
	 * @return
	 */
	public ArrayList<Node> getAdjacent(){
		return adjacentNodes;
	}
	
	public void addAdjacentNode(Node other){
		this.adjacentNodes.add(other);
	}
	
	public void addAdjacentNodes(ArrayList<Node> otherAdjacentNodes){
		this.adjacentNodes.addAll(otherAdjacentNodes);
	}

	public void setValue(Type value) {
		this.value = value;
	}

	public Type getValue() {
		return value;
	}
	
}
