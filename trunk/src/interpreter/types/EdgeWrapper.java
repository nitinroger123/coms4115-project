package interpreter.types;

/*
 * Basically a three-tuple with added matching algorithm for searching
 * through edges for both directed and undirected graphs
 * 
 * @author: Yufei Liu
 */
public class EdgeWrapper {
	private int first, second;
	private Type val;
	
	public EdgeWrapper(int first, int second, Type val) {
		this.first = first;
		this.second = second;
		this.val = val;
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}
	
	public void setValue(Type v) {
		val = v;
	}
	
	public Type getValue() {
		return val;
	}
	
	/*
	 * Undirected match
	 */
	public boolean check(int n1, int n2) {
		return this.checkDirected(n1,n2) && this.checkDirected(n2,n1);
	}
	
	/*
	 * Directed match
	 */
	public boolean checkDirected(int n1, int n2) {
		return first==n1 && second==n2;
	}
}
