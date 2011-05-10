package interpreter.types;


import java.util.ArrayList;
/**
 * Helper class. Used for temp storage.
 * @author nitin
 *
 */
public class Function {
	public String name;
	public String code;
	public ArrayList<String> paramsType;
	public ArrayList<String> args;
	public Function(String name , String code , ArrayList<String> params){
		this.name = name;
		this.code = code;
		this.paramsType = params;
	}

}
