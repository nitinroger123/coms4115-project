/*package interpreter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import interpreter.types.*;
import interpreter.types.Number;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.NodesNotConnectedException;

public class GraphTest {
	private Graph graph;
	@Before
	public void setUp() throws Exception {
		graph = new Graph();
	}

	@After
	public void tearDown() throws Exception {
		graph = null;
	}
	
	@Test
	public void testNothing(){
		assertTrue(true);
	}
	
	@Test
	public void testAddNode() throws NodesNotConnectedException{
		Number number = new Number(1.0);
		Number number1 = new Number(2.0);
		graph.addNode(new Node(number,null));
		graph.addNode(new Node(number1,null));
		graph.addEdge(1, 2, new Number(100.0));
		assertEquals(new Number(100.0), graph.getEdge(1, 2));
		assertEquals(2,graph.getNumberOfNodes());
	}

}
*/