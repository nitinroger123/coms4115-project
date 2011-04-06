package helper;

import java.util.HashSet;
import java.util.Set;

public class IDGenerator {
    private static Set<Double> nodeIDSet = new HashSet<Double>();

    public static Double generateNodeId(){
        Double id = Math.random();
        while(nodeIDSet.contains(id)){
            id= Math.random();
        }
        nodeIDSet.add(id);
        return id;
    }

}
