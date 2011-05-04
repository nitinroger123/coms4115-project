package interpreter.types;

import java.util.ArrayList;

public class Vector implements Type{
	private ArrayList<Number> vecVals;
	
	/**
	 * Creates a Vector of numbers contained in the value or tag of a graph-list
	 */
	public Vector(MyGraph vec, String tag){
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
	
	public Vector(Vector other){
		int n = (int)other.getSize().getDouble();
		vecVals = new ArrayList<Number>();
		for(int i = 0; i < n; i++){
			vecVals.add(new Number(other.get(i)));
		}
	}
	
	/**
	 * Creates a vector of uniform values
	 */
	public Vector(int n, Number val){
		vecVals = new ArrayList<Number>();
		for(int i = 0; i < n; i++){
			vecVals.add(new Number(val));
		}
	}
	
	
	public String getValue(){
		return "";
	}
	
	public Number get(int i){
		return vecVals.get(i);
	}
	
	public Vector scalarMult(Number a){
		ArrayList<Number> newVals = new ArrayList<Number>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(Number.multiply(a, vecVals.get(i)));
		}
		return new Vector(newVals);
	}
	
	public Number dot(Vector other){
		Number dotProd = new Number(0);
		for(int i = 0; i < vecVals.size(); i++){
			dotProd = Number.add(dotProd, Number.multiply(vecVals.get(i),other.get(i)));
		}	
		return dotProd;
	}
	
	public Vector add(Vector other){
		ArrayList<Number> newVals = new ArrayList<Number>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(Number.add(vecVals.get(i), other.get(i)));
		}
		return new Vector(newVals);
	}
	
	public Vector normalize(){
		Number norm = this.dot(this);
		ArrayList<Number> newVals = new ArrayList<Number>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(Number.divide(vecVals.get(i), Number.pow(norm, new Number(0.5))));
		}
		return new Vector(newVals);
	}
	
	public Vector elementMult(Vector other){
		ArrayList<Number> newVals = new ArrayList<Number>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(Number.multiply(vecVals.get(i), other.get(i)));
		}
		return new Vector(newVals);
	}
	
	public void print(){
		String str = "";
		for(int i = 0; i < vecVals.size(); i++){
			str += vecVals.get(i).getValue() + " ";
		}
		System.out.println(str);
	}
	
	public Number getSize(){
		return new Number(vecVals.size());
	}
}
