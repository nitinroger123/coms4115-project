package helper;

import java.util.ArrayList;
/**
 * Helper class. Used for temp storage.
 * @author nitin
 *
 */
public class FunctionDef {
	public String name;
	public String code;
	public ArrayList<String> paramsType;
	public ArrayList<String> args;
	public FunctionDef(String name , String code , ArrayList<String> params){
		this.name = name;
		this.code = code;
		this.paramsType = params;
	}

}
