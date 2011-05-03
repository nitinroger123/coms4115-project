package helper;
import interpreter.types.Edge;

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

	
	public DisplayGraph(ArrayList<Edge> edge) {
		panel = new DisplayPanel(edge); // initialize a new display panel (as defined in the internal class below)
		panel.setPreferredSize(new Dimension(300, 300));
		scrollpane = new JScrollPane(panel);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack(); // cleans up the window panel
	}

}


@SuppressWarnings("serial")
class DisplayPanel extends JPanel {
	ArrayList<Edge> edge;
	int xs; // a variable for the x coordinates of each vertex
	int ys; // a variable for the y coordinates of each vertex

	
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
			g.drawRoundRect(dx1, dy1, 20, 20, 20, 20);
			g.drawRoundRect(dx2, dy2, 20, 20, 20, 20);
		}
	}
} 
