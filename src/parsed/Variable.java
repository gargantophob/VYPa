package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Variable {
    
    public Type type;
    public String name;
    
    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public static List<Variable> recognize(GrammarParser.VariableDeclarationContext ctx) {
        List<Variable> list = new ArrayList<>();
        Type type = new Type(ctx.type());
        ctx.name().forEach(name -> list.add(new Variable(type, name.getText())));
        return list;
    }
    
    /*@Override
    public String toString() {
        return type + " " + name;
    }*/
}
