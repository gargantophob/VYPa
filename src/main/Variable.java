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
    
    @Override
    public String toString() {
        return type + " " + name;
    }

    public void assertVoid() {
        if(type.type == Type.TypeType.VOID) {
            Recover.exit(3, "variable of type void");
        }
    }

    public void assertObject() {
        if(type.type == Type.TypeType.OBJECT) {
            Recover.notImplemented();
        }
    }

    public void assertDefineteness(Scope scope) {
        if(scope.isDefinedSymbol(name) || scope.isDefinedVariableHere(name)) {
            Recover.exit(3, "redefinition of symbol " + name);
        }
    }
}
