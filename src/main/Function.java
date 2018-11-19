package main;

import java.util.List;
import java.util.ArrayList;

public class Function {
    public parsed.Function prototype;
    
    public Type type;
    public String name;
    
    public SymbolTable<Variable> scope;
    public List<Variable> parameters;
    public List<Statement> body;

    public Function(parsed.Function prototype) {
        this.prototype = prototype;
        
        type = new Type(prototype.type);
        name = prototype.name;
        scope = new SymbolTable<>();
        
        parameters = new ArrayList<>();
        prototype.parameters.forEach(parsed -> {
            Variable v = new Variable(parsed);
            scope.register(v.name, v);
            parameters.add(v);
        });
    }

    public void collectBody() {
        /*body = new ArrayList<>();
        prototype.body.forEach(s -> body.add(new Statement(s, scope)));*/
    }
}