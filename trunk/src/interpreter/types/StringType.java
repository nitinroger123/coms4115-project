package interpreter.types;

/**
 * 
 * @author mk3245
 *
 */
public class StringType implements Type{

    private String value;
    
    public StringType(String value) {
        this.value = new String(value);
    }
    
    public StringType() {
        this.value = "";
    }
    
    public void print() {
        System.out.println(value);
    }
    
    public String substring(int beginIndex) {
        return value.substring(beginIndex);
    }
    
    public String substring(int beginIndex, int endIndex) {
        return value.substring(beginIndex, endIndex);
    }
    
    public String concat(String str) {
        return value + str;
    }
    
    public int find(String str) {
        return value.indexOf(str);
    }
    
    public int findLast(String str) {
        return value.lastIndexOf(str, value.length());
    }
    
    public String toUpper() {
        return value.toUpperCase();
    }
    
    public String toLower() {
        return value.toLowerCase();
    }
    
    public String reverse() {
        StringBuilder sb = new StringBuilder(value);
        return sb.reverse().toString();
    }
}
