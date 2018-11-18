package main;

import java.util.List;
import java.util.ArrayList;

public class Variable {
    public Type type;
    public String name;
    
    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Variable(parsed.Variable ref) {
        type = new Type(ref.type);
        type.assertNonVoid();
        name = ref.name;
    }
    
    /*@Override
    public String toString() {
        return type + " " + name;
    }*/
}
