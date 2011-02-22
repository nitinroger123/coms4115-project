package helper;

import java.util.HashMap;

public class IDGenerator {
	HashMap<Double,Double> nodeIDHash = new HashMap<Double, Double>();
	
	public static Double generateNodeId(){
		Double id = Math.random();
		while(nodeIDHash.get(id) != null ){
			id= Math.random();
		}
		return id;
	}

}
