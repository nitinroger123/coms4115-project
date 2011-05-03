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
        Edge e = new Edge(n1, n2, 10.0);
        testGraph.addEdge(e);
        e = new Edge(n1, n5, 2.0);
        testGraph.addEdge(e);
        e = new Edge(n2, n4, 20.0);
        testGraph.addEdge(e);
        e = new Edge(n2, n3, 7.0);
        testGraph.addEdge(e);
        e = new Edge(n5, n4, 8.0);
        testGraph.addEdge(e);
        testGraph.visualize(testGraph.minimumSpanningTree());
	}

}
