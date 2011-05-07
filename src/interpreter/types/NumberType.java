package interpreter.types;
import helper.Arithmetic;
/**
 *Class for the type Number 
 * @author nitin
 *
 */
public class NumberType implements Type{
	private double value;
	
	public String getValue(){
		return String.valueOf(value);
	}
	
	
	public NumberType(double value){
		this.value=value;
	}
	
	public NumberType(){
		this.value=0;
	}
	
	public NumberType(NumberType n){
		this.value = n.value;
	}
	
	public double getDouble(){
		return value;
	}
	
	public static NumberType add(NumberType N1,NumberType N2){
		return new NumberType(N1.value+N2.value);
	}
	
	public static NumberType subtract(NumberType N1,NumberType N2){
		return new NumberType (N1.value-N2.value);
	}
	
	public static NumberType multiply(NumberType N1,NumberType N2){
		return new NumberType (N1.value*N2.value);
	}
	
	public static NumberType divide(NumberType N1,NumberType N2){
		return new NumberType(N1.value/N2.value);
	}
	
	public static NumberType factorial(NumberType N1){
		return new NumberType(Arithmetic.factorial(N1.value));
	}
	
	public static NumberType modulo(NumberType N1,NumberType N2){
		return new NumberType(N1.value%N2.value);
	}
	
	public static NumberType pow(NumberType N1,NumberType N2){
		return new NumberType(Math.pow(N1.value, N2.value));
	}

}
