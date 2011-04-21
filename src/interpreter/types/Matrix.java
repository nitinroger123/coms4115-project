package interpreter.types;
import java.util.ArrayList;

public class Matrix implements Type{
	private ArrayList<ArrayList<Number>> matVals;
	
	
	/**
	 * Assumes that Graph is an n-length list of nodes with tags that are m-length lists of numbers and constructs a matrix
	 * "" for either chooses the value
	 */
	public Matrix(MyGraph mat, String rowTag, String colTag){
		int n = mat.getNumberOfNodes();
		int m = ((MyGraph)mat.listGet(0, rowTag)).getNumberOfNodes();
		matVals = new ArrayList<ArrayList<Number>>();
		for(int i = 0; i < n; i++){
			matVals.add(new ArrayList<Number>());
			MyGraph row = (MyGraph)mat.listGet(i, rowTag);
			for(int j = 0; j < m; j++){
				matVals.get(i).add((Number)row.listGet(i, colTag));
			}
		}
	}	
	
	public Matrix(ArrayList<ArrayList<Number>> vals){
		matVals = new ArrayList<ArrayList<Number>>();
		for(int i = 0; i < vals.size(); i++){
			matVals.add(new ArrayList<Number>());
			for(int j = 0; j < vals.get(0).size(); j++){
				matVals.get(i).add(vals.get(i).get(j));
			}
		}
	}
	
	public Matrix(MyGraph mat){
		matVals = new ArrayList<ArrayList<Number>>();
		int n = mat.getNumberOfNodes();
		for(int i = 0; i < n; i++){
			ArrayList<Node> adjNodes = mat.getNode(i).getAdjacent();
			int m = adjNodes.size();
			matVals.add(new ArrayList<Number>());
			for(int j = 0; j < m; j++){
				if(adjNodes.contains(mat.getNode(j))){
					matVals.get(i).add(new Number(1));
				}else{
					matVals.get(i).add(new Number(0));
				}
			}
		}
	}
	
	public Matrix(Matrix other){
		matVals = new ArrayList<ArrayList<Number>>();
		int n = other.getHeight();
		int m = other.getWidth();
		for(int i = 0; i < n; i++){
			matVals.add(new ArrayList<Number>());
			for(int j = 0; j < m; j++){
				matVals.get(i).add(other.get(i,j));
			}
		}
	}
	
	public String getValue(){
		return "";
	}
	
	public Number get(int i, int j){
		return matVals.get(i).get(j);
	}
	
	public int getHeight(){
		return matVals.size();
	}
	
	public int getWidth(){
		return matVals.get(0).size();
	}
	
	public Vector vecMult(Matrix mat, Vector vec){
		ArrayList<Number> newVals = new ArrayList<Number>();
		for(int i = 0; i < mat.getHeight(); i++){
			Number temp = new Number(0); // will be ith value of new vec 
			for(int j = 0; j < mat.getWidth(); j++){
				temp = Number.add(temp, Number.multiply(mat.get(i,j), vec.get(j)));
			}
		}
		return new Vector(newVals);
	}
}
