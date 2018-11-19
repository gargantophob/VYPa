package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    public boolean isSuper;
    public Path context;
    public String name;
    public List<Expression> arguments;

    public Call(
        boolean isSuper, Path context, String name, List<Expression> arguments
    ) {
        this.isSuper = isSuper;
        this.context = context;
        this.name = name;
        this.arguments = arguments;
    }

    public static Call recognize(GrammarParser.CallContext ctx) {
        boolean isSuper = false;
        Path context = null;
        
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.variable() == null) {
                isSuper = true;
            } else {
                context = Path.recognize(ctx.variable());
            }
        }
        String name = ctx.name().getText();
        List<Expression> arguments = new ArrayList<>();
        ctx.ex().forEach(ex -> arguments.add(Expression.recognize(ex)));

        return new Call(isSuper, context, name, arguments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(context + "." + name + "(");
        arguments.forEach(arg -> sb.append(arg + ","));
        sb.append(")");
        return sb.toString();
    }
}