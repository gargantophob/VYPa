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
    public StatementBlock body;

    public Function(
        Type type, String name, List<Variable> parameters, StatementBlock body
    ) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public static Function recognize(GrammarParser.FunctionDefinitionContext ctx) {
        Type type = Type.recognize(ctx.type());
        String name = ctx.name().getText();
        List parameters = new ArrayList<>();
        ctx.paramList().formalParameter().forEach(par -> 
            parameters.add(new Variable(Type.recognize(par.type()), par.name().getText()))
        );
        StatementBlock body = StatementBlock.recognize(ctx.blockStatement());
        return new Function(type, name, parameters, body);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type + " " + name + " (");
        parameters.forEach(p -> sb.append(p + ", "));
        sb.append(") { " + body.toString() + "}");
        return sb.toString();
    }

}