package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class VariablePath {
    public List<String> names;
    
    public VariablePath(List<String> names) {
        this.names = names;
    }

    public static VariablePath recognize(GrammarParser.VariableContext ctx) {
        List<String> names = new ArrayList<>();
        ctx.atomicVariable().forEach(av -> names.add(av.getText()));
        return new VariablePath(names);
    }

    public void assertSimpleness() {
        if(names.size() > 1) {
            Recover.notImplemented();
        }
    }

    public void assertDefineteness(Scope scope) {
        String name = names.get(0);
        if(!scope.isDefinedVariable(name)) {
            Recover.exit(3, "variable " + name + " was not declared");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(names.get(0));
        for(int i = 1; i < names.size(); i++) {
            sb.append('.' + names.get(i));
        }
        return sb.toString();
    }
}