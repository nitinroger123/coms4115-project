package helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * preprocessor for stripping out comments and loading in functions
 * @author nitin
 *
 */
public class Preprocessor {
	public ArrayList<String> functionDefinitions = new ArrayList<String>();
	public ArrayList<String> code = new ArrayList<String>();
 	public ArrayList<FunctionDef> functions = new ArrayList<FunctionDef>();
	public Preprocessor(String filename) throws IOException{
		processFunctions(filename);
	}
	public void processFunctions(String fileName) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String s = "";
		boolean seenFunc = false;
		FunctionDef function = null;
		while((s = reader.readLine()) != null){
			if(seenFunc == true) {
				String out = s+"\n";
				String func = "";
				while(true){
					func = reader.readLine();
					if(func == null || func.equals("end")){
						function.code = out;
						code.add(out);
						if(function != null ){
							functions.add(function);
						}
						break;
					}
				}
				seenFunc = false;
			}
			if(s.contains("def")){
				seenFunc = true;
				String def =s;
				String params[] = new String [100];
				String name="";
				ArrayList<String> types = new ArrayList<String>();
				for(int i=0;i<def.length();i++){
					if(def.charAt(i)=='('){
						params = def.substring(i+1).split(",");
						name = def.substring(3,i);
						name = name.replaceAll("^\\s+", "");
						break;
					}
				}
				for(int i=0;i<params.length;i++){
					params[i] = params[i].replaceAll("^\\s+", "");
					types.add(params[i].split(" ")[0].trim());
				}
				function = new FunctionDef(name, "", types);
				functionDefinitions.add(s);
			}
		}
		
		
	}
	
	public static void main(String args[]) throws IOException{
		Preprocessor p = new Preprocessor("preprocess");
		System.out.println(p.functions.get(0).name);
		System.out.println(p.functions.get(0).paramsType.get(0));
		System.out.println(p.functions.get(0).paramsType.get(1));
		System.out.println(p.functions.get(0).paramsType.get(2));
		System.out.println(p.functions.get(0).code);
		System.out.println("*****************");
		System.out.println(p.functions.get(1).name);
		System.out.println(p.functions.get(1).paramsType.get(0));
		System.out.println(p.functions.get(0).code);
	}

}
