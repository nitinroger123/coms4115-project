package helper;
/**
 * Class for Arithmetic functions. As and when we define new arithmetic operations
 * we can populate this class.
 * @author nitin
 *
 */
public class Arithmetic {
	
	public static double factorial(double n){
		if(n==0||n==1) return 1;
		return n*(factorial(n-1));
	}
	
	

}
