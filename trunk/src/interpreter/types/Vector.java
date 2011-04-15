package interpreter.types;

import java.util.ArrayList;

public class Vector {
	private ArrayList<Number> vecVals;
	
	/**
	 * Creates a Vector of numbers contained in the value or tag of a graph-list
	 */
	public Vector(Graph vec, String tag){
		int n = vec.getNumberOfNodes();
		vecVals = new ArrayList<Number>();
		for(int i = 0; i < n; i++){
			vec.listGet(i, tag);
		}
	}
	
	public Vector(ArrayList<Number> vals){
		vecVals = new ArrayList<Number>();
		for(Number v: vals){
			vecVals.add(v);
		}
	}
	
	public Number get(int i){
		return vecVals.get(i);
	}
}
