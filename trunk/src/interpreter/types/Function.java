package interpreter.types;

import java.util.ArrayList;

/**
 * A class for a function. Functions will take the parameters
 * and returns the answer to the variable that equates to the call
 * @author nitin
 *
 */
public class Function implements Type {
	String name;
	Type returnValue;
	ArrayList<Type> params;
	String returnAddress;
	
	/**
	 * Constructor that creates a function call
	 * @param name
	 * @param returnType
	 * @param params
	 * @param returnAddress
	 */
	public Function(String name , Type returnType , ArrayList<Type> params, String returnAddress){
		this.name = name;
		this.returnValue = returnType;
		this.params = params;
		this.returnAddress = returnAddress;
	}
	
	@Override
	public String getValue() {
		return name;
	}
	
	
	
	
	

}
