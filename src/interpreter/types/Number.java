package interpreter.types;
import helper.Arithmetic;
/**
 *Class for the type Number 
 * @author nitin
 *
 */
public class Number implements Type{
	private double value;
	
	public String getValue(){
		return String.valueOf(value);
	}
	
	
	public Number(double value){
		this.value=value;
	}
	
	public Number(){
		this.value=0;
	}
	public static double add(Number N1,Number N2){
		return N1.value+N2.value;
	}
	
	public static double subtract(Number N1,Number N2){
		return N1.value-N2.value;
	}
	
	public static double multiply(Number N1,Number N2){
		return N1.value*N2.value;
	}
	
	public static double divide(Number N1,Number N2){
		return N1.value/N2.value;
	}
	
	public static double factorial(Number N1){
		return Arithmetic.factorial(N1.value);
	}
	
	public static double modulo(Number N1,Number N2){
		return N1.value%N2.value;
	}
	
	public static double pow(Number N1,Number N2){
		return Math.pow(N1.value, N2.value);
	}

}
