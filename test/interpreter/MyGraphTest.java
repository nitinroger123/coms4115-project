package interpreter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import interpreter.types.Edge;
import interpreter.types.MyGraph;
import interpreter.types.Node;
import interpreter.types.Number;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MyGraphTest {
    MyGraph testGraph;

    // This creates a test graph as shown here http://en.wikipedia.org/wiki/File:Graph.traversal.example.svg
    // TODO Change this graph to be a generic graph which can be a test case for all the graph methods,
    private MyGraph getTestGraph() {
        MyGraph myGraph = new MyGraph();
        Node[] arr = new Node[] {
                new Node(new Number(1)), new Node(new Number(2)), new Node(new Number(3)), new Node(new Number(4)),
                new Node(new Number(5)), new Node(new Number(6)), new Node(new Number(7)) 
        };
        Edge[] arrEdge = new Edge[] {
                new Edge(arr[0], arr[1], null), new Edge(arr[0], arr[2], null), new Edge(arr[0], arr[4], null), 
                new Edge(arr[1], arr[3], null), new Edge(arr[5], arr[1], null), new Edge(arr[2], arr[6], null), 
                new Edge(arr[4], arr[5], null) 
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
        Node start = graph.getNode(0);
        Node end = graph.getNode(3);
        graph.dfs(start, end);
    }

}
