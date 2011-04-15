package interpreter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import interpreter.types.Edge;
import interpreter.types.MyGraph;
import interpreter.types.Node;
import interpreter.types.Number;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MyGraphTest {
	MyGraph testGraph;
	@Before
	public void setUp(){
		testGraph = new MyGraph();
	}
	@After
	public void tearDown(){
		testGraph = null;
	}
	@Test
	public void testGraphCreation(){
		Node n1 = new Node(new Number(1.0));
		Node n2 = new Node(new Number(2.0));
		testGraph.addNode(n1);
		testGraph.addNode(n2);
		assertEquals(2,testGraph.getNumberOfNodes());
	}
	
	@Test
	public void testEdgeAddition(){
		Node n1 = new Node(new Number(1.0));
		Node n2 = new Node(new Number(2.0));
		testGraph.addNode(n1);
		testGraph.addNode(n2);
		Edge e = new Edge(n1, n2, new Number(1.0));
		testGraph.addEdge(e);
		assertTrue(n1.getAdjacent().contains(n2));
		assertTrue(n2.getAdjacent().contains(n1));
	}
	
	@Test
	public void testBFS(){
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
		testGraph.bfs(n1 , n4).printNodes();
		assertEquals(3,testGraph.bfs(n1 , n4).getNumberOfEdges());
		assertEquals(4,testGraph.bfs(n1).getNumberOfEdges());
	}
	
	@Test
	public void testPrintNodes(){
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
		String expected = "1.0 2.0 3.0 4.0 5.0 ";
		assertEquals(expected, testGraph.printNodes());
	}
	

}
