package interpreter.types;

import helper.IDGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class Node implements Type{
	private Type value;
	private ArrayList<Node> adjacentNodes = new ArrayList<Node>();
	private long distance; //distance from source node. Useful for BFS, shortest path etc.
	private Node parent; //The parent Node in a bfs, dfs etc.
	private Double id;
	private HashMap<String, Type> tags = new HashMap<String, Type>();
	
	public String getValue(){
		return value.getValue();
	}
	
	public Type getContents(){
		return value;
	}
	
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
		tags.put("", value);
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
	
	public Node clone(){
		Node clone = new Node();
		clone.adjacentNodes = new ArrayList<Node>(this.getAdjacent());
		clone.value = this.value;
		return clone;
	}
	
}
