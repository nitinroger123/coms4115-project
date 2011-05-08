package helper;

import java.util.ArrayList;

public class FunctionDef {
	public String name;
	public String code;
	public ArrayList<String> paramsType;
	public FunctionDef(String name , String code , ArrayList<String> params){
		this.name = name;
		this.code = code;
		this.paramsType = params;
	}

}
