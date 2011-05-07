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
	String code;
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
		this.code ="";
	}
	
	/**
	 * Constructor to create a new function of a given name with the supplied code
	 * @param code
	 */
	public Function(String name,String code){
		this.name = name;
		this.code = code;
	}
	
	/**
	 * Set the code with respect to the given function
	 * @param code
	 */
	public void setCode(String code)
	{
		this.code = code;
	}
	
	@Override
	public String getValue() {
		return name;
	}
	
	public String getCode(){
		return code;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Type returnValue) {
		this.returnValue = returnValue;
	}

	public ArrayList<Type> getParams() {
		return params;
	}

	public void setParams(ArrayList<Type> params) {
		this.params = params;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}
	@Override
	public String toString() {
		return "Function (name=" + name + ", returnValue=" + returnValue
				+ ", params=" + params + ", returnAddress=" + returnAddress
				+ ")";
	}

}
