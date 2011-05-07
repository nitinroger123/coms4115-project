package helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Preprocessor {
	public ArrayList<String> functionDefinitions = new ArrayList<String>();
	public ArrayList<String> code = new ArrayList<String>();
 	
	public void process(String fileName) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String s = "";
		boolean seenFunc = false;
		while((s = reader.readLine()) != null){
			if(seenFunc == true) {
				String out = s+"\n";
				String func = "";
				while(true){
					func = reader.readLine();
					if(func == null || func.equals("end")){
						out = out +func+"\n";
						code.add(out);
						break;
					}
				}
				seenFunc = false;
			}
			if(s.contains("def")){
				seenFunc = true;
				functionDefinitions.add(s);
			}
		}
		
		
	}
	
	public static void main(String args[]) throws IOException{
		new Preprocessor().process("preprocess");
	}

}
