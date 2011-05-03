package interpreter;

import interpreter.types.Edge;
import interpreter.types.MyGraph;
import interpreter.types.Node;
import interpreter.types.Number;

public class Main {
	public static void main(String args[]){
		MyGraph testGraph = new MyGraph();
		Node n1 = new Node(new Number(1.0));
        Node n2 = new Node(new Number(2.0));
        Node n3 = new Node(new Number(3.0));
        Node n4 = new Node(new Number(4.0));
        Node n5 = new Node(new Number(5.0));
        testGraph.addNode(n1);
        testGraph.addNode(n2);
        testGraph.addNode(n3);
        testGraph.addNode(n4);
        testGraph.addNode(n5);
        Edge e = new Edge(n1, n2, new Number(1.0));
        testGraph.addEdge(e);
        e = new Edge(n1, n5, new Number(1.0));
        testGraph.addEdge(e);
        e = new Edge(n2, n4, new Number(1.0));
        testGraph.addEdge(e);
        e = new Edge(n2, n3, new Number(1.0));
        testGraph.addEdge(e);
        e = new Edge(n5, n4, new Number(1.0));
        testGraph.addEdge(e);
        testGraph.bfs(n1,n4).visualize();
	}

}
