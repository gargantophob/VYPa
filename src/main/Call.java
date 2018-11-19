package main;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    
    public SymbolTable<Variable> scope;

    public Expression context;
    public Function function;
    public List<Expression> arguments;
    
    public Call(parsed.Call parsed, SymbolTable<Variable> scope) {
        
        this.scope = scope;
        context = null;
        String name = parsed.name;
        Variable callee = null;
		Class contextClass = null;

		if(parsed.isSuper) {
            callee = scope.lookUpRecursively("this");
            contextClass = (Class) callee.type;
            function = contextClass.methods.lookUp(name);
        } else if(parsed.context != null) {
        	context = new Expression(parsed.context, scope);
        }

        arguments = new ArrayList<>();
        if(callee != null) {
        	arguments.add(context);
        }
        parsed.arguments.forEach(e -> arguments.add(new Expression(e, scope)));
        
        /*
        } else if(parsed.path != null) {
            path = new Path(parsed.path, scope);
            callee = path.lastVariable();
            if(!(callee.type instanceof Class)) {
                Recover.exit(3, callee.name + " is not an instance variable");
            } else {
	            contextClass = (Class) callee.type;
	            function = contextClass.methods.lookUp(name);
            }
        } else {
            function = SymbolTable.functions.lookUp(parsed.name);
        }
        */
    }
}