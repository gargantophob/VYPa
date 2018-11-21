package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Variable {
    
    public Type type;
    public String name;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Variable(GrammarParser.FormalParameterContext ctx) {
        type = Type.recognize(ctx.type());
        name = ctx.name().getText();
    }

    public Variable(GrammarParser.VariableDeclarationContext ctx) {
        Variable prototype = recognize(ctx).get(0);
        type = prototype.type;
        name = prototype.name;
    }
    
    public static List<Variable> recognize(GrammarParser.VariableDeclarationContext ctx) {
        List<Variable> list = new ArrayList<>();
        Type type = Type.recognize(ctx.type());
        if(type == Type.VOID) {
            Recover.type("cannot declare void variable");
        }
        ctx.name().forEach(name -> list.add(new Variable(type, name.getText())));
        return list;
    }
}
