package interpreter.types;

/**
 * 
 * @author mk3245
 *
 */
public class StringType implements Type{

    private String value;
    
    public String getValue(){
    	return value;
    }
    
    public StringType(String value) {
        this.value = new String(value);
    }
    
    public StringType() {
        this.value = "";
    }
    
    public void print() {
        System.out.println(value);
    }
    
    public StringType substring(NumberType beginIndex) {
        return new StringType(value.substring((int)beginIndex.getDouble()));
    }
    
    public StringType substring(NumberType beginIndex, NumberType endIndex) {
        return new StringType(value.substring((int)beginIndex.getDouble(), (int)endIndex.getDouble()));
    }
    
    public StringType concat(StringType str) {
        return new StringType(value + str.getValue());
    }
    
    public NumberType find(StringType str) {
        return new NumberType(value.indexOf(str.getValue()));
    }
    
    public NumberType findLast(StringType str) {
        return new NumberType(value.lastIndexOf(str.getValue(), value.length()));
    }
    
    public StringType toUpper() {
        return new StringType(value.toUpperCase());
    }
    
    public StringType toLower() {
        return new StringType(value.toLowerCase());
    }
    
    public StringType reverse() {
        StringBuilder sb = new StringBuilder(value);
        return new StringType(sb.reverse().toString());
    }
}
