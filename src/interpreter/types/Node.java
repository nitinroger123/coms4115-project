package interpreter.types;

import helper.IDGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class Node implements Type {
	private Type value;
	private ArrayList<Node> adjacentNodes = new ArrayList<Node>();
	private long distance; //distance from source node. Useful for BFS, shortest path etc.
	private Node parent; //The parent Node in a bfs, dfs etc.
	private Double id;
	private HashMap<String, Type> tags = new HashMap<String, Type>();
	
	public Node(){
		this.id = IDGenerator.generateNodeId();
	}
	
	/*
	 * Constructor for a node with only a value
	 */
	public Node(Type value){
		this.value = value;
		this.id = IDGenerator.generateNodeId();
	}
	
	/**
	 * Constructor to create a new node with a set value and set its adj nodes
	 * @param value
	 * @param adjacentNodes
	 */
	public Node(Type value, ArrayList<Node> adjacentNodes){
		this.value=value;
		this.adjacentNodes=adjacentNodes;
		this.id=IDGenerator.generateNodeId();
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
	
	public void setTag(String name, Type tag){
		tags.put(name, tag);
	}
	
	public Type getTag(String name){
		return tags.get(name);
	}
	
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public long getDistance() {
		return distance;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public Node getParent() {
		return parent;
	}
	
	public Double getID(){
		return id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adjacentNodes == null) ? 0 : adjacentNodes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Node other = (Node) obj;
		if (adjacentNodes == null) {
			if (other.adjacentNodes != null)
				return false;
		} else if (!adjacentNodes.equals(other.adjacentNodes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
