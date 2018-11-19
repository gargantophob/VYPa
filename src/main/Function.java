package main;

import java.util.List;
import java.util.ArrayList;

public class Function {
    public Type type;
    public String name;
    
    public List<Variable> parameters;
    public List<Statement> body;

    public Scope scope;

    public Function(parsed.Function parsed, Scope parentScope) {
        type = new Type(parsed.type);
        name = parsed.name;
        scope = new Scope(parentScope);
        scope.variableAllow();
        scope.literalAllow();

        parameters = new ArrayList<>();
        parsed.parameters.forEach(p -> {
            Variable v = new Variable(p);
            scope.variableRegister(v);
            parameters.add(v);
        });
        
        body = new ArrayList<>();
        parsed.body.forEach(s -> body.add(new Statement(s, scope)));
    }

    /*public Function(
        Type type, String name, List<Variable> parameters, StatementBlock body
    ) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }*/

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type + " " + name + " (");
        parameters.forEach(p -> sb.append(p + ", "));
        sb.append(") { " + body.toString() + "}");
        return sb.toString();
    }*/
}