package helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
 	String fileName;
	public Preprocessor(String filename) throws IOException{
		this.fileName = filename;
		processFunctions(filename);
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
		replaceSpecialCharacters();
	}
	
	/**
	 * Private method to replace special parser characters to the original form
	 * @throws IOException 
	 */
	private void replaceSpecialCharacters() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("temp.gpl"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("toParser.gpl"));
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
			if(s.contains("<=")){
				temp = s.replace(">=", "@");
			}
			if(s.contains(">=")){
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
		Preprocessor p = new Preprocessor(args[0]);
		
	}

}
