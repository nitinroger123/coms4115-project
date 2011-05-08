package helper;

import interpreter.types.*;
import interpreter.types.NumberType;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * to use the hashMap just do new MethodHelper().map
 * @author nitin
 *
 */
public class MethodHelper {
	public HashMap<String,HashMap<String,Method>> map = new HashMap<String, HashMap<String,Method>>();
	
	public MethodHelper(){
		init();
	}
	
	public void init(){
		HashMap<String, Method> graphMap = new HashMap<String, Method>();
		HashMap<String, Method> numMap = new HashMap<String, Method>();
		HashMap<String, Method> strMap = new HashMap<String, Method>();
		MyGraph g = new MyGraph();
		NumberType n = new NumberType();
		StringType str = new StringType();
		Method graphMethods [] = g.getClass().getMethods();
		for(int i=0;i<graphMethods.length;i++){
			graphMap.put(graphMethods[i].getName(), graphMethods[i]);
		}
		Method [] numberMethods = str.getClass().getMethods();
		for(int i=0; i< numberMethods.length;i++ ){
			numMap.put(numberMethods[i].getName(), numberMethods[i]);
		}
		Method [] strMethods = n.getClass().getMethods();
		for(int i=0; i< strMethods.length;i++ ){
			strMap.put(strMethods[i].getName(), strMethods[i]);
		}
		map.put(g.getClass().toString(), graphMap);
		map.put(n.getClass().toString(), numMap);
		map.put(str.getClass().toString(),strMap);
	}
	
	
	public static void main(String args []) throws IllegalArgumentException, IllegalAccessException{
		MyGraph g = new MyGraph();
		StringType s = new StringType();
		NumberType n = new NumberType();
		MethodHelper m = new MethodHelper();
		System.out.println(m.map.get(n.getClass().toString()).toString());
	}
}
