package helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
/**
 * preprocessor for stripping out comments and loading in functions
 * @author nitin
 *
 */
public class Preprocessor {
	public ArrayList<String> functionDefinitions = new ArrayList<String>();
	public ArrayList<String> code = new ArrayList<String>();
 	public ArrayList<FunctionDef> functions = new ArrayList<FunctionDef>();
 	String fileName;
 	String targetFile;
 	
 	
	public Preprocessor(String filename, String target) throws IOException{
		this.fileName = filename;
		targetFile = target;
		//processFunctions(filename);
		removeCommentsAndReplaceCharacters();
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
	
	/**
	 * Remove the comments and write to the file. Also change all the dummy characters to the actual
	 * operator. This is a workaround for jacc
	 * @throws IOException
	 */
	public void removeCommentsAndReplaceCharacters() throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter("temp.gpl"));
		BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
		String s = "";
		while((s = reader.readLine()) != null){
			if(!s.contains("#")){
				writer.write(s+"\n");
			}
			else{
				boolean foundStartQuotes = false;
				boolean foundEndQuotes = false;
				boolean foundComment = true;
				for(int i=0;i<s.length();i++){
					//found a start or end quote.
					if(s.charAt(i) == '"'){
						if(foundStartQuotes){
							foundEndQuotes = true;
						}
						else{
							foundStartQuotes = true;
						}
					}
					//Found # outside a literal and should be treated as a comment
					if(s.charAt(i)=='#'){
						if(foundStartQuotes && foundEndQuotes){
							foundComment = true;
							String temp = s.substring(0, i);
							temp.trim();
							writer.write(temp+"\n");
						}
						if(foundStartQuotes && !foundEndQuotes){
							foundComment = false;
						}
						if(!foundEndQuotes && !foundStartQuotes){
							foundComment = true;
						}
					}
					
				}
				if(!foundComment){
					writer.write(s+"\n");
				}
			}
			
		}
		writer.close();
		processFuncs();
		replaceSpecialCharacters();
	}
	
	/**
	 * pulls out the name of the method
	 * @param line
	 * @return
	 */
	private String getName(String line){
		String name ="";
		for(int i = 0 ; i<line.length(); i++){
			if(line.charAt(i) == '('){
				name = line.substring(3,i);
				name = name.replaceAll("^\\s+", "");
				break;
			}
		}
		return name;
	}
	
	/**
	 * pulls out the type of the params of the function 
	 * @param line
	 * @return
	 */
	private ArrayList<String> getParams(String line){
		ArrayList<String> types = new ArrayList<String>();
		String st = ""; 
		for(int i=0;i<line.length();i++){
			if(line.charAt(i) == '('){
				st=line.substring(i+1, line.length()-1);
				//System.out.println(st);
				break;
			}
		}
		String args [] = st.split(",");
		for(int i=0; i< args.length;i++){
			args[i] = args[i].replaceAll("^\\s+", "");
			String temp []= args[i].split(" ");
			String s = temp[0].replaceAll("^\\s+", "");
			types.add(s);
		}
		return types;
	}
	
	private ArrayList<String> getArgs (String line){
		ArrayList<String> arguments = new ArrayList<String>();
		String st = ""; 
		for(int i=0;i<line.length();i++){
			if(line.charAt(i) == '('){
				st=line.substring(i+1, line.length()-1);
				//System.out.println(st);
				break;
			}
		}
		String args [] = st.split(",");
		for(int i=0; i< args.length;i++){
			args[i] = args[i].replaceAll("^\\s+", "");
			String temp []= args[i].split(" ");
			String s = "";
			if(temp.length > 1){
				s = temp[1].replaceAll("^\\s+", "");
			}
			arguments.add(s);
		}
		return arguments;
	}
	/**
	 * Private method to strip out all functions and store them in the arrayList
	 * @throws IOException 
	 */
	private void processFuncs() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("temp.gpl"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("temp1.gpl"));
		String line ="";
		Stack<String> functionStack = new Stack<String>();
		boolean insideFunction = false;
		String code ="";
		FunctionDef function = null;
		while((line = reader.readLine()) != null){
			if(functionStack.isEmpty()){
				insideFunction = false;
			}	
			
			if(line.contains("def")){
				//create a new function holder to add to functions
				functionStack = new Stack<String>();
				code = "";
				function = new FunctionDef("", "", null);
				String name = getName(line);
				ArrayList<String> params = getParams(line);
				function.name = name;
				function.paramsType = params;
				function.code = code;
				function.args = getArgs(line);
				insideFunction = true;
				functionStack.push(name);
				continue;
			}
			
			if(insideFunction && line.contains("if")){
				functionStack.push("if");
			}
			
			if(insideFunction && line.contains("while")){
				functionStack.push("while");
			}
			
			if(insideFunction && line.contains("end")){
				functionStack.pop();
				if(functionStack.isEmpty()){
					function.code = code;
					this.functions.add(function);
					insideFunction = false;
					continue;
				}
			}
			
			if(!insideFunction){
				writer.write(line+"\n");
			}
			
			if(insideFunction){
				code = code + line +"\n";
			}
			
		}
		writer.close();
		
	}

	/**
	 * Private method to replace special parser characters to the original form
	 * @throws IOException 
	 */
	private void replaceSpecialCharacters() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("temp1.gpl"));
		BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
		String s ="";
		while((s = reader.readLine()) != null){
			String temp = s;
			boolean foundStartQuote = false;
			boolean foundEndQuote = false;
			//Make sure these are not inside a string literal
			for(int i=0;i<s.length();i++){
				if(s.charAt(i)=='"'){
					if(foundStartQuote){
						foundEndQuote = true;
					}
					else{
						foundStartQuote = true;
					}
				}
			}
			//String literal found and hence don't replace
			if(foundEndQuote && foundStartQuote){
				writer.write(s+(char)13+"\n");
				continue;
			}
			//Not inside a string literal
			if(s.contains("==")){
				temp = s.replace("==", "~");
			}
			if(s.contains("!=")){
				temp = s.replace("!=", "`");
			}
			if(s.contains(">=")){
				temp = s.replace(">=", "@");
			}
			if(s.contains("<=")){
				temp = s.replace("<=", "$");
			}
			if(s.contains("&&")){
				temp = s.replace("&&", "&");
			}
			if(s.contains("||")){
				temp = s.replace("||", "|");
			}
			writer.write(temp+(char)13+"\n");
		}
		writer.close();
	}

	/**
	 * Tester code
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException{
		Preprocessor p = new Preprocessor(args[0], args[1]);
		
		/*for(FunctionDef f : p.functions){
			System.out.println("Name "+f.name);
			System.out.println(f.code);
			System.out.println(f.paramsType.toString());
			System.out.println(f.args.toString());
		}*/
		
	}

}
