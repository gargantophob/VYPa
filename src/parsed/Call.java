package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    
    public boolean isSuper;
    public Path path;
    public String name;
    public List<Expression> arguments;

    public Call(GrammarParser.CallContext ctx) {
        isSuper = false;
        path = null;
        
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.path() == null) {
                isSuper = true;
            } else {
                path = new Path(ctx.path());
            }
        }
        String name = ctx.name().getText();
        List<Expression> arguments = new ArrayList<>();
        ctx.ex().forEach(ex -> arguments.add(new Expression(ex)));
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