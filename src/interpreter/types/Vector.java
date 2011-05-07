package interpreter.types;

import java.util.ArrayList;

public class Vector implements Type{
	private ArrayList<NumberType> vecVals;
	
	/**
	 * Creates a Vector of numbers contained in the value or tag of a graph-list
	 */
	public Vector(MyGraph vec, String tag){
		int n = vec.getNumberOfNodes();
		vecVals = new ArrayList<NumberType>();
		for(int i = 0; i < n; i++){
			vec.listGet(i, tag);
		}
	}
	
	public Vector(ArrayList<NumberType> vals){
		vecVals = new ArrayList<NumberType>();
		for(NumberType v: vals){
			vecVals.add(v);
		}
	}
	
	public Vector(Vector other){
		int n = (int)other.getSize().getDouble();
		vecVals = new ArrayList<NumberType>();
		for(int i = 0; i < n; i++){
			vecVals.add(new NumberType(other.get(i)));
		}
	}
	
	/**
	 * Creates a vector of uniform values
	 */
	public Vector(int n, NumberType val){
		vecVals = new ArrayList<NumberType>();
		for(int i = 0; i < n; i++){
			vecVals.add(new NumberType(val));
		}
	}
	
	
	public String getValue(){
		return "";
	}
	
	public NumberType get(int i){
		return vecVals.get(i);
	}
	
	public Vector scalarMult(NumberType a){
		ArrayList<NumberType> newVals = new ArrayList<NumberType>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(NumberType.multiply(a, vecVals.get(i)));
		}
		return new Vector(newVals);
	}
	
	public NumberType dot(Vector other){
		NumberType dotProd = new NumberType(0);
		for(int i = 0; i < vecVals.size(); i++){
			dotProd = NumberType.add(dotProd, NumberType.multiply(vecVals.get(i),other.get(i)));
		}	
		return dotProd;
	}
	
	public Vector add(Vector other){
		ArrayList<NumberType> newVals = new ArrayList<NumberType>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(NumberType.add(vecVals.get(i), other.get(i)));
		}
		return new Vector(newVals);
	}
	
	public Vector normalize(){
		NumberType norm = this.dot(this);
		ArrayList<NumberType> newVals = new ArrayList<NumberType>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(NumberType.divide(vecVals.get(i), NumberType.pow(norm, new NumberType(0.5))));
		}
		return new Vector(newVals);
	}
	
	public Vector elementMult(Vector other){
		ArrayList<NumberType> newVals = new ArrayList<NumberType>();
		for(int i = 0; i < vecVals.size(); i++){
			newVals.add(NumberType.multiply(vecVals.get(i), other.get(i)));
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
	
	public NumberType getSize(){
		return new NumberType(vecVals.size());
	}
}
