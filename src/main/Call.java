package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    public boolean isSuper;
    public VariablePath callee;
    public String name;
    public List<Expression> arguments;

    public Call(
        boolean isSuper, VariablePath callee, String name, List<Expression> arguments
    ) {
        this.isSuper = isSuper;
        this.callee = callee;
        this.name = name;
        this.arguments = arguments;    
    }

    public static Call recognize(GrammarParser.CallContext ctx) {
        boolean isSuper = false;
        VariablePath callee = null;
        
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.variable() == null) {
                isSuper = true;
            } else {
                callee = VariablePath.recognize(ctx.variable());
            }
        }
        String name = ctx.name().getText();
        List<Expression> arguments = new ArrayList<>();
        ctx.ex().forEach(ex -> arguments.add(Expression.recognize(ex)));

        return new Call(isSuper, callee, name, arguments);
    }

    public void assertDefineteness(Scope scope) {
        if(isSuper) {
            Recover.notImplemented();
        }
        if(callee != null) {
            Recover.notImplemented();
        }

        if(!scope.isDefinedFunction(name)) {
            Recover.exit(3, "funcion " + name + " was not defined");
        }
        arguments.forEach(arg -> arg.assertDefineteness(scope));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(callee + "." + name + "(");
        arguments.forEach(arg -> sb.append(arg + ","));
        sb.append(")");
        return sb.toString();
    }
}