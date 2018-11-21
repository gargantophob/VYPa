package main;

import java.util.List;
import java.util.ArrayList;

public class Call  {

    
    public SymbolTable<Variable> scope;
    public String name;
    public Expression context;
    
    public Function function;
    public List<Expression> arguments;
    public Type type;
    
    public Call(parsed.Call parsed, SymbolTable<Variable> scope) {
        
        this.scope = scope;
        context = null;
        name = parsed.name;
        Variable callee = null;
		Class contextClass = null;

		if(parsed.isSuper) {
            callee = scope.lookUpRecursively("this");
            contextClass = (Class) callee.type;
            function = contextClass.methods.lookUp(name);
        } else if(parsed.context != null) {
        	context = new Expression(parsed.context, scope);
        	function = null;
        } else {
        	function = SymbolTable.functions.lookUp(name);
        }

        arguments = new ArrayList<>();
        parsed.arguments.forEach(e -> arguments.add(new Expression(e, scope)));
    }

    public void inferType() {
    	if(context != null) {
            context.inferType();
            if(!(context.type instanceof Class)) {
                Recover.exit(4, "type mismatch");
            }
            Class contextClass = (Class) context.type;
            function = contextClass.methods.lookUp(name);
        }

        type = function.type;
        arguments.forEach(e -> e.inferType());
        List<Type> signature = new ArrayList<>();
        arguments.forEach(e -> signature.add(e.type));
        if(!function.signatureMatch(type, signature)) {
            Recover.exit(4, "type mismatch");
        }
    }
}