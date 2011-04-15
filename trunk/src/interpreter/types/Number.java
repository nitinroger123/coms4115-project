package interpreter.types;
import helper.Arithmetic;
/**
 *Class for the type Number 
 * @author nitin
 *
 */
public class Number implements Type{
	private Double value;
	
	public String getValue(){
		return value.toString();
	}
	public Number(double value){
		this.value=value;
	}
	
	public Number(){
		this.value=0.0;
	}
	public double add(Number N1,Number N2){
		return N1.value+N2.value;
	}
	
	public double subtract(Number N1,Number N2){
		return N1.value-N2.value;
	}
	
	public double multiply(Number N1,Number N2){
		return N1.value*N2.value;
	}
	
	public double divide(Number N1,Number N2){
		return N1.value/N2.value;
	}
	
	public double factorial(Number N1){
		return Arithmetic.factorial(N1.value);
	}
	
	public double modulo(Number N1,Number N2){
		return N1.value%N2.value;
	}
	
	public double pow(Number N1,Number N2){
		return Math.pow(N1.value, N2.value);
	}

}
