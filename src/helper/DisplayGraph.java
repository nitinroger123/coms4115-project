package helper;
import interpreter.types.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 
 * @author nitin
 * Graph Visualizer
 */

@SuppressWarnings("serial")
public class DisplayGraph extends JFrame {
    JScrollPane scrollpane;
    DisplayPanel panel;

    MyGraph g;

    public DisplayGraph(MyGraph aGraph) {
        g = aGraph;
        panel = new DisplayPanel(g.getEdgeList()); // initialize a new display panel (as defined in the internal class below)
        panel.setPreferredSize(new Dimension(300, 300));
        scrollpane = new JScrollPane(panel);
        getContentPane().add(scrollpane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack(); // cleans up the window panel
    }

    public void KamKawLayout(){
        int n = g.getNumberOfNodes();
        double x[] = new double[n];
        double y[] = new double[n];
        for(int i = 0; i < n; i++){
            x[i] = g.getNodeByInt(i).x;
            y[i] = g.getNodeByInt(i).y;
        }
        //		double[][] dist = g.allPairsShortestPath();
        //		int max = 0;
        //		for(int i = 0; i < n; i++){
        //			for(int j = 0; j < n; j++){
        //				if(dist[i][j] > max){max = dist[i][j];}
        //			}
        //		}
        //		double lScale = 300/(double)max;
        //		double[][] l = new double[n][n];
        //		for(int i = 0; i < n; i++){
        //			for(int j = 0; j < n; j++){
        //				l[i][j] = lScale*dist[i][j];
        //			}
        //		}
        //		double kScale = 1;
        //		double[][] k = new double[n][n];
        //		for(int i = 0; i < n; i++){
        //			for(int j = 0; j < n; j++){
        //				k[i][j] = K/(dist[i][j]*dist[i][j]);
        //			}
        //		}
        //		double[] delEx = new double[n];
        //		double[] delEy = new double[n];
        //		double[] delEall = new double[n];
        //		for(int m = 0; m < n; m++){
        //			double sumX = 0;
        //			double sumY = 0;
        //			for(int i = 0; i < n; i++){
        //				if(m != i){
        //					sumX += k[m][i]*((x[m] - x[i])- l[m][i]*(x[m]-x[i])/Math.sqrt((x[m]-x[i])*(x[m]-x[i]) + (y[m]-y[i])*(y[m]-y[i])));
        //					sumY += k[m][i]*((y[m] - y[i])- l[m][i]*(y[m]-y[i])/Math.sqrt((x[m]-x[i])*(x[m]-x[i]) + (y[m]-y[i])*(y[m]-y[i])));
        //				}
        //			}
        //			delEx[m] = sumX;
        //			delEy[m] = sumY;
        //			delEall[m] = Math.sqrt(delEx[m]*delEx[m] +delEy[m]*delEy[m]);
        //		}
        //		double[] velX = double[n];
        //		double[] velY = double[n];
        //		double forceConst = 
        //		for(int i = 0; i < n; i++){
        //			velX[i] = velX[i] + delEx[i]
        //		}
    }
}


@SuppressWarnings("serial")
class DisplayPanel extends JPanel {
    ArrayList<Edge> edge;
    int xs; // a variable for the x coordinates of each vertex
    int ys; // a variable for the y coordinates of each vertex
    public static final int DIMENSION = 30;


    public DisplayPanel(ArrayList<Edge> edge) {
        this.edge = edge; // allows display routines to access the tree
        setBackground(Color.white);
        setForeground(Color.black);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground()); // colors the window
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground()); // set color and fonts
        Font MyFont = new Font("SansSerif", Font.PLAIN, 10);
        g.setFont(MyFont);
        xs = 10; // where to start printing on the panel
        ys = 20;
        g.drawString("The Graph!:\n", xs, ys);
        ys = ys + 10;
        this.drawGraph(g, edge); 
        revalidate(); 
    }

    public void drawGraph(Graphics g, ArrayList<Edge> edge) {
        for(Edge e: edge) {
            int dy1 = (e.node1.y);
            int dy2 = (e.node2.y);
            int dx1 = (e.node1.x);
            int dx2 = (e.node2.x);
            g.drawLine(dx1, dy1, dx2, dy2);
            g.drawString(e.node1.getValue(), dx1, dy1);
            g.drawString(e.node2.getValue(), dx2, dy2);
            g.drawRoundRect(dx1, dy1, DIMENSION,DIMENSION , DIMENSION, DIMENSION);
            g.drawRoundRect(dx2, dy2, DIMENSION,DIMENSION , DIMENSION, DIMENSION);
        }
    }
} 
