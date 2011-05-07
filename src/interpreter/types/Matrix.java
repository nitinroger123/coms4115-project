package interpreter.types;

import java.util.ArrayList;
import java.util.List;

public class Matrix implements Type{
	private ArrayList<ArrayList<NumberType>> matVals;
	
	public Matrix(Vector vec){
		int n = (int)Math.floor(vec.getSize().getDouble());
		matVals = new ArrayList<ArrayList<NumberType>>();
		for(int i = 0; i < n; i++){
			matVals.add(new ArrayList<NumberType>());
			for(int j = 0; j < n; j++){
				if(i == j){
					matVals.get(i).add(new NumberType(vec.get(i)));
				}
				else{matVals.get(i).add(new NumberType(0));}
			}
		}
	}
	
	/**
	 * Assumes that Graph is an n-length list of nodes with tags that are m-length lists of numbers and constructs a matrix
	 * "" for either chooses the value
	 */
	public Matrix(MyGraph mat, String rowTag, String colTag){
		int n = mat.getNumberOfNodes();
		int m = ((MyGraph)mat.listGet(0, rowTag)).getNumberOfNodes();
		matVals = new ArrayList<ArrayList<NumberType>>();
		for(int i = 0; i < n; i++){
			matVals.add(new ArrayList<NumberType>());
			MyGraph row = (MyGraph)mat.listGet(i, rowTag);
			for(int j = 0; j < m; j++){
				matVals.get(i).add((NumberType)row.listGet(i, colTag));
			}
		}
	}	
	
	public Matrix(ArrayList<ArrayList<NumberType>> vals){
		matVals = new ArrayList<ArrayList<NumberType>>();
		for(int i = 0; i < vals.size(); i++){
			matVals.add(new ArrayList<NumberType>());
			for(int j = 0; j < vals.get(0).size(); j++){
				matVals.get(i).add(vals.get(i).get(j));
			}
		}
	}
	
	public Matrix(MyGraph mat){
		matVals = new ArrayList<ArrayList<NumberType>>();
		int n = mat.getNumberOfNodes();
		for(int i = 0; i < n; i++){
			List<Node> adjNodes = mat.getNode(i).getAdjacent();
			matVals.add(new ArrayList<NumberType>());
			for(int j = 0; j < n; j++){
				if(adjNodes.contains(mat.getNode(j))){
					matVals.get(i).add(new NumberType(1));
				}else{
					matVals.get(i).add(new NumberType(0));
				}
			}
		}
	}
	
	public Matrix(Matrix other){
		matVals = new ArrayList<ArrayList<NumberType>>();
		int n = other.getHeight();
		int m = other.getWidth();
		for(int i = 0; i < n; i++){
			matVals.add(new ArrayList<NumberType>());
			for(int j = 0; j < m; j++){
				matVals.get(i).add(other.get(i,j));
			}
		}
	}
	
	public String getValue(){
		return "";
	}
	
	public NumberType get(int i, int j){
		return matVals.get(i).get(j);
	}
	
	public int getHeight(){
		return matVals.size();
	}
	
	public int getWidth(){
		return matVals.get(0).size();
	}
	
	public Vector vecMult(Vector vec){
		ArrayList<NumberType> newVals = new ArrayList<NumberType>();
		for(int i = 0; i < getHeight(); i++){
			NumberType temp = new NumberType(0); // will be ith value of new vec 
			for(int j = 0; j < getWidth(); j++){
				temp = NumberType.add(temp, NumberType.multiply(get(i,j), vec.get(j)));
			}
			newVals.add(temp);
		}
		return new Vector(newVals);
	}
	
//	public Matrix matMult(Matrix other){
//		ArrayList<Number> newVals = new ArrayList<Number>();
//		for(int i = 0; i < getHeight(); i++){
//			Number temp = new Number(0); // will be ith value of new vec 
//			for(int j = 0; j < getWidth(); j++){
//				temp = Number.add(temp, Number.multiply(get(i,j), vec.get(j)));
//			}
//			newVals.add(temp);
//		}
//		return new Vector(newVals);
//	}
}
