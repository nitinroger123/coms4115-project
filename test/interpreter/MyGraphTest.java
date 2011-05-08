package interpreter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import interpreter.types.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MyGraphTest {
    MyGraph testGraph;
	@Test
	public void testPageRank(){
		Node n1 = new Node(new NumberType(1.0));
		Node n2 = new Node(new NumberType(2.0));
		Node n3 = new Node(new NumberType(3.0));
		Node n4 = new Node(new NumberType(4.0));
		testGraph.addNode(n1);
		testGraph.addNode(n2);
		testGraph.addNode(n3);
		testGraph.addNode(n4);
		Edge e = new Edge(n1, n2, new NumberType(1.0));
		testGraph.addEdge(e);
		e = new Edge(n1, n3, new NumberType(1.0));
		testGraph.addEdge(e);
		e = new Edge(n1, n4, new NumberType(1.0));
		testGraph.addEdge(e);
		Vector pr = testGraph.getPageRank();
		pr.print();
	}
	

    // This creates a test graph as shown here http://en.wikipedia.org/wiki/File:Graph.traversal.example.svg
    // TODO Change this graph to be a generic graph which can be a test case for all the graph methods,
   
    private MyGraph getTestGraph() {
        MyGraph myGraph = new MyGraph();
        Node[] arr = new Node[] {
                new Node(new NumberType(1)), new Node(new NumberType(2)), new Node(new NumberType(3)), new Node(new NumberType(4)),
                new Node(new NumberType(5)), new Node(new NumberType(6)) 
        };
        Edge[] arrEdge = new Edge[] {
                new Edge(arr[0], arr[1], 1.0), new Edge(arr[0], arr[4], 3.0), new Edge(arr[0], arr[3], 4.0), 
                new Edge(arr[1], arr[3], 4.0), new Edge(arr[1], arr[4], 2.0), new Edge(arr[2], arr[4], 4.0), 
                new Edge(arr[2], arr[5], 5.0), new Edge(arr[3], arr[4], 4.0), new Edge(arr[4], arr[5], 7.0) 
        };
        myGraph.addAllNodes(Arrays.asList(arr));
        myGraph.addAllEdges(Arrays.asList(arrEdge));
        return myGraph;
    }
    
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
        Node n1 = new Node(new NumberType(1.0));
        Node n2 = new Node(new NumberType(2.0));
        testGraph.addNode(n1);
        testGraph.addNode(n2);
        assertEquals(2,testGraph.getNumberOfNodes());
    }

    @Test
    public void testEdgeAddition(){
        Node n1 = new Node(new NumberType(1.0));
        Node n2 = new Node(new NumberType(2.0));
        testGraph.addNode(n1);
        testGraph.addNode(n2);
        Edge e = new Edge(n1, n2, new NumberType(1.0));
        testGraph.addEdge(e);
        assertTrue(n1.getAdjacent().contains(n2));
        assertTrue(n2.getAdjacent().contains(n1));
    }

    @Test
    public void testBFS(){
        Node n1 = new Node(new NumberType(1.0));
        Node n2 = new Node(new NumberType(2.0));
        Node n3 = new Node(new NumberType(3.0));
        Node n4 = new Node(new NumberType(4.0));
        Node n5 = new Node(new NumberType(5.0));
        testGraph.addNode(n1);
        testGraph.addNode(n2);
        testGraph.addNode(n3);
        testGraph.addNode(n4);
        testGraph.addNode(n5);
        Edge e = new Edge(n1, n2, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n1, n5, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n2, n4, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n2, n3, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n5, n4, new NumberType(1.0));
        testGraph.addEdge(e);
        testGraph.bfs(n1 , n4).printNodes();
        assertEquals(3,testGraph.bfs(n1 , n4).getNumberOfEdges());
        assertEquals(4,testGraph.bfs(n1).getNumberOfEdges());
    }

    @Test
    public void testPrintNodes(){
        Node n1 = new Node(new NumberType(1.0));
        Node n2 = new Node(new NumberType(2.0));
        Node n3 = new Node(new NumberType(3.0));
        Node n4 = new Node(new NumberType(4.0));
        Node n5 = new Node(new NumberType(5.0));
        Node n6 = n5.clone();
        System.out.println(n6.equals(n5));
        testGraph.addNode(n1);
        testGraph.addNode(n2);
        testGraph.addNode(n3);
        testGraph.addNode(n4);
        testGraph.addNode(n5);
        String expected = "1.0 2.0 3.0 4.0 5.0 ";
        assertEquals(expected, testGraph.printNodes());
    }

    @Test
    public void testDfs() {
        MyGraph graph = getTestGraph();
        Node start = graph.getNodeByInt(0);
        Node end = graph.getNodeByInt(2);
        graph = graph.dfs(start, end);
        String expected = "1.0 4.0 5.0 6.0 3.0 ";
        assertEquals(expected, graph.printNodes());
    }
    
    @Test
    public void testDisplay(){
    	testGraph = new MyGraph();
    	Node n1 = new Node(new NumberType(1.0));
        Node n2 = new Node(new NumberType(2.0));
        Node n3 = new Node(new NumberType(3.0));
        Node n4 = new Node(new NumberType(4.0));
        Node n5 = new Node(new NumberType(5.0));
        testGraph.addNode(n1);
        testGraph.addNode(n2);
        testGraph.addNode(n3);
        testGraph.addNode(n4);
        testGraph.addNode(n5);
        Edge e = new Edge(n1, n2, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n1, n5, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n2, n4, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n2, n3, new NumberType(1.0));
        testGraph.addEdge(e);
        e = new Edge(n5, n4, new NumberType(1.0));
        testGraph.addEdge(e);
        Double [][] adj = testGraph.getAdjMatrix();
        for(int i=0;i<testGraph.getNumberOfNodes();i++){
        	for(int j=0;j<testGraph.getNumberOfEdges();j++){
        		System.out.print(adj[i][j]+" ");
        	}
        	System.out.println();
        }
        testGraph.visualize();
    }

}
