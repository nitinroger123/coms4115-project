package interpreter;

import java.util.Arrays;

import interpreter.types.Edge;
import interpreter.types.MyGraph;
import interpreter.types.Node;
import interpreter.types.NumberType;

public class Main {
    public static void main(String args[]){
        mstTest();
        dfsTest();
        bfsTest();
    }

    // Graph from http://en.wikipedia.org/wiki/File:Msp1.jpg
    private static MyGraph getTestGraph() {
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

    public static void mstTest() {
        MyGraph graph = getTestGraph();
        graph.visualize(graph.minimumSpanningTree());
    }
    
    public static void dfsTest() {
        MyGraph graph = getTestGraph();
        Node start = graph.getNodeByInt(0);
        Node end = graph.getNodeByInt(2);
        MyGraph temp = graph.dfs(start, end);
        System.out.println(temp.getNumberOfEdges());
        System.out.println(temp.getNumberOfNodes());
        temp.visualize();
    }
    
    public static void bfsTest() {
        MyGraph testGraph = getTestGraph();
        Node n1 = testGraph.getNodeByInt(0);
        Node n5 = testGraph.getNodeByInt(5);
        testGraph.bfs(n1,n5).visualize();
    }
}

