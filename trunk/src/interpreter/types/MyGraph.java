package interpreter.types;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * new graph class
 * @author nitin
 *
 */
public class MyGraph {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private Integer numberOfNodes;
	private Integer numberOfEdges;
	
	public MyGraph(){
		this.nodes = new ArrayList<Node>();
		this. edges = new ArrayList<Edge>();
		this. numberOfNodes = 0;
		this. numberOfEdges = 0;
	}
	
	public void addNode(Node n){
		nodes.add(n);
		this.numberOfNodes ++;
	}
	
	public void addEdge(Edge e){
		edges.add(e);
		e.getFirstNode().addAdjacentNode(e.getSecondNode());
		e.getSecondNode().addAdjacentNode(e.getFirstNode());
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
	
	
}
