package main;

public class Type {

    public static Type VOID = new Type();
    public static Type INT = new Type();
    public static Type STRING = new Type();
    
    protected Type() {}

    public static Type recognize(parsed.Type parsed) {
        String text = parsed.text;
    	if(text.equals("void")) {
            return VOID;
        } else if(text.equals("int")) {
            return INT;
        } else if(text.equals("string")) {
            return STRING;
        } else {
            return SymbolTable.classes.lookUp(text);
        }
    }
}