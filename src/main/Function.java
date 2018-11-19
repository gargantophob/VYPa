package main;

import java.util.List;
import java.util.ArrayList;

public class Function {
    public parsed.Function prototype;
    
    public Type type;
    public String name;
    public Scope scope;
    public List<Variable> parameters;

    public List<Statement> body;


    public Function(parsed.Function parsed, Scope parentScope) {
        prototype = parsed;
        
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
    }

    public void processBody() {
        body = new ArrayList<>();
        prototype.body.forEach(s -> body.add(new Statement(s, scope)));
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