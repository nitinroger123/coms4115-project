package interpreter.types;

import helper.Arithmetic;

/**
 *Class for the type Number 
 * @author nitin
 *
 */
public class Number implements Type{
	private double value;
	
	public Number(double value){
		this.value=value;
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
