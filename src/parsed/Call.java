package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    
    public boolean isSuper;
    public Expression context;

    public String name;
    
    public List<Expression> arguments;

    public Call(String name, GrammarParser.ArgumentsContext ctx) {
        this.name = name;
        arguments = new ArrayList<>();
        ctx.ex().forEach(e -> arguments.add(new Expression(e)));
    }

    public Call(GrammarParser.CallContext ctx) {
        this(ctx.name().getText(), ctx.arguments());
        
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.ex() != null) {
                isSuper = false;
                context = new Expression(ctx.ex());
            } else {
                isSuper = true;
                context = null;
            }
        }
    }

    public Call(GrammarParser.ExContext ctx) {
        this(ctx.name().getText(), ctx.arguments());

        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.ex().size() > 0) {
                isSuper = false;
                context = new Expression(ctx.ex().get(0));
            } else {
                isSuper = true;
                context = null;
            }
        }
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(context + "." + name + "(");
        arguments.forEach(arg -> sb.append(arg + ","));
        sb.append(")");
        return sb.toString();
    }*/
}