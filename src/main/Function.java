package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Function {

    public Class context;
    public Type type;
    public String name;
    
    public Variable self; // aka "this"
    public List<Variable> parameters; // in the declaration order, excluds self-parameter
    public List<Type> signature; // for fast reference
    public SymbolTable<Variable> scope;
    
    public GrammarParser.BlockContext bodyContext;
    public List<Statement> body;

    public int order;

    public Function(Type type, String name, List<Variable> parameters, Class context) {
        this.context = context;
        this.type = type;
        this.name = name;
        
        if(parameters == null) {
            parameters = new ArrayList<>();
        }
        this.parameters = parameters;
        
        initialize();
    }

    public Function (GrammarParser.FunctionDefinitionContext ctx, Class context) {
        this.context = context;
        type = Type.recognize(ctx.type());
        name = ctx.name().getText();
        
        parameters = new ArrayList<>();
        ctx.paramList().formalParameter().forEach(par -> parameters.add(new Variable(par)));
        
        bodyContext = ctx.block();

        initialize();
    }

    public void initialize() {
        scope = new SymbolTable<>();
        
        if(context != null) {
            self = new Variable(context, "this");
            scope.register(self.name, self);
        }

        signature = new ArrayList<>();
        order = -1;
        for(Variable v: parameters) {
            SymbolTable.classes.assertNonExistence(v.name);
            SymbolTable.functions.assertNonExistence(v.name);
            signature.add(v.type);
            scope.register(v.name, v);
            v.order = order;
            order--;
        }
        order = 0;
    }

    public int order() {
        order++;
        return order;
    }

    public boolean signatureMatch(Type type, List<Type> signature) {
        if(context == null && name.equals("print")) {
            // can print any primitive type
            for(Type arg: signature) {
                if(arg instanceof Class) {
                    return false;
                }
            }
            return true;
        }
        return this.type == type && this.signature.equals(signature);
    }

    public void collectBody() {
        if(bodyContext == null) {
            // System.err.println("Skipping body of " + name);
            return;
        }
        body = Statement.recognize(this, scope, bodyContext);
    }
}