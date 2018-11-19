package main;

public class Type {
    public static enum Option {
        VOID, INT, STRING, OBJECT;
    }

    public Option option;
    public Class classRef;

    /*public Type(Option option, Class classRef) {
        this.option = option;
        this.classRef = classRef;
    }*/

    public Type(parsed.Type parsed) {
        String text = parsed.text;
    	if(text.equals("void")) {
            option = Option.VOID;
        } else if(text.equals("int")) {
            option = Option.INT;
        } else if(text.equals("string")) {
            option = Option.STRING;
        } else {
            option = Option.OBJECT;
            classRef = SymbolTable.classes.lookUp(text);
        }
    }

    public void assertNonVoid() {
    	if(option == Option.VOID) {
    		Recover.exit(3, "unexpected void type");
    	}
    }
}