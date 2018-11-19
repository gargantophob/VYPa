package main;

import java.util.List;
import java.util.ArrayList;

public class Type {
	public static enum Option {
        VOID, INT, STRING, OBJECT;
    }

    public Option option;
    public Class classRef;

    public Type(Option option, Class classRef) {
        this.option = option;
        this.classRef = classRef;
    }

    public Type(parsed.Type ref) {
        String str = ref.type;
    	if(str.equals("void")) {
            option = Option.VOID;
        } else if(str.equals("int")) {
            option = Option.INT;
        } else if(str.equals("string")) {
            option = Option.STRING;
        } else {
            option = Option.OBJECT;
            classRef = ClassTable.lookUp(str);
        }
    }

    /*@Override
    public String toString() {
    	if(option == Option.OBJECT) {
            return className;
        } else {
            return option.toString();
        }
    }*/

    public void assertNonVoid() {
    	if(option == Option.VOID) {
    		Recover.exit(3, "unexpected void type");
    	}
    }
}