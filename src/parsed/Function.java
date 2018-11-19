package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Function {
    
    public Type type;
    public String name;
    public List<Variable> parameters;
    public List<Statement> body;

    public Function (GrammarParser.FunctionDefinitionContext ctx) {
        type = new Type(ctx.type());
        name = ctx.name().getText();
        parameters = new ArrayList<>();
        ctx.paramList().formalParameter().forEach(par -> 
            parameters.add(new Variable(new Type(par.type()), par.name().getText()))
        );
        body = Statement.recognize(ctx.block());
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type + " " + name + " (");
        parameters.forEach(p -> sb.append(p + ", "));
        sb.append(") { " + body.toString() + "}");
        return sb.toString();
    }*/
}