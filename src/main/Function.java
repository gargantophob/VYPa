package main;

import java.util.List;
import java.util.ArrayList;

public class Function {
    public parsed.Function prototype;
    
    public Type type;
    public String name;
    public List<Variable> parameters;
    
    public List<Type> signature;
    public SymbolTable<Variable> scope;
    
    public List<Statement> body;
    
    public Class context;

    public Function(
        Type type, String name, List<Variable> parameters,
        List<Statement> body, Class context
    ) {
        prototype = null;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.context = context;
        
        initialize();
    }

    public Function(parsed.Function prototype, Class context) {
        this.prototype = prototype;
        type = Type.recognize(prototype.type);
        name = prototype.name;
        parameters = new ArrayList<>();
        this.context = context;
        prototype.parameters.forEach(parsed -> parameters.add(new Variable(parsed)));

        initialize();
    }

    public void initialize() {
        scope = new SymbolTable<>();
        signature = new ArrayList<>();
        parameters.forEach(v -> {
            SymbolTable.classes.assertNonExistence(v.name);
            SymbolTable.functions.assertNonExistence(v.name);
            scope.register(v.name, v);
            signature.add(v.type);
        });

        if(context != null) {
            Variable selfParameter = new Variable(context, "this");
            scope.register(selfParameter.name, selfParameter);
        }
    }

    public void collectBody() {
        if(prototype == null) {
            System.out.println("Skipping " + name);
            return;
        }
        body = new ArrayList<>();
        prototype.body.forEach(s -> body.add(new Statement(s, scope)));
    }

    public boolean signatureMatch(Type type, List<Type> signature) {
        return this.type == type && this.signature.equals(signature);
    }

    public boolean signatureMatch(Function f) {
        return signatureMatch(f.type, f.signature);
    }

}