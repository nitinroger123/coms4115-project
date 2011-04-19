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
	public static Number add(Number N1,Number N2){
		return new Number(N1.value+N2.value);
	}
	
	public static Number subtract(Number N1,Number N2){
		return new Number (N1.value-N2.value);
	}
	
	public static Number multiply(Number N1,Number N2){
		return new Number (N1.value*N2.value);
	}
	
	public static Number divide(Number N1,Number N2){
		return new Number(N1.value/N2.value);
	}
	
	public static Number factorial(Number N1){
		return new Number(Arithmetic.factorial(N1.value));
	}
	
	public static Number modulo(Number N1,Number N2){
		return new Number(N1.value%N2.value);
	}
	
	public static Number pow(Number N1,Number N2){
		return new Number(Math.pow(N1.value, N2.value));
	}

}
