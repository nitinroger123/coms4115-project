package helper;

import interpreter.types.MyGraph;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * to use the hashMap just do new MethodHelper().map
 * @author nitin
 *
 */
public class MethodHelper {
	public HashMap<String, Method> map = new HashMap<String, Method>();
	
	public MethodHelper(){
		init();
	}
	
	public void init(){
		MyGraph g = new MyGraph();
		Method methods [] = g.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			map.put(methods[i].getName(), methods[i]);
		}
	}
	
	/*
	public static void main(String args []) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		MyGraph g = new MyGraph();
		MethodHelper m = new MethodHelper();
		m.init();
	}*/
}
