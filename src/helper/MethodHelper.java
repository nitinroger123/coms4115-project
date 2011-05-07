package helper;

import interpreter.types.*;
import interpreter.types.Number;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * to use the hashMap just do new MethodHelper().map
 * @author nitin
 *
 */
public class MethodHelper {
	public HashMap<Object,HashMap<String,Method>> map = new HashMap<Object, HashMap<String,Method>>();
	
	public MethodHelper(){
		init();
	}
	
	public void init(){
		HashMap<String, Method> graphMap = new HashMap<String, Method>();
		HashMap<String, Method> numMap = new HashMap<String, Method>();
		HashMap<String, Method> strMap = new HashMap<String, Method>();
		MyGraph g = new MyGraph();
		Number n = new Number();
		StringType str = new StringType();
		Method graphMethods [] = g.getClass().getMethods();
		for(int i=0;i<graphMethods.length;i++){
			graphMap.put(graphMethods[i].getName(), graphMethods[i]);
		}
		Method [] numberMethods = n.getClass().getMethods();
		for(int i=0; i< numberMethods.length;i++ ){
			numMap.put(numberMethods[i].getName(), numberMethods[i]);
		}
		Method [] strMethods = n.getClass().getMethods();
		for(int i=0; i< strMethods.length;i++ ){
			strMap.put(strMethods[i].getName(), strMethods[i]);
		}
		map.put(g, graphMap);
		map.put(n, numMap);
		map.put(str,strMap);
	}
	
	/*
	public static void main(String args []) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		MyGraph g = new MyGraph();
		MethodHelper m = new MethodHelper();
		m.init();
	}*/
}
