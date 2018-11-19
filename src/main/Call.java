package main;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    
    public Scope scope;

    public Function function;
    public Path context;
    public List<Expression> arguments;
    
    public Call(parsed.Call parsed, Scope scope) {
        this.scope = scope;

        if(parsed.isSuper) {
            Recover.notImplemented();
        } else if(parsed.context != null) {
            context = new Path(parsed.context.path, scope);
            Variable callee = context.lastVariable();
            Class contextClass = callee.type.classRef;
            if(contextClass == null) {
                Recover.exit(3, callee.name + " is not an instance variable");
            }
            function = contextClass.scope.functionLookUp(parsed.name);
        } else {
            function = scope.functionLookUp(parsed.name);
        }

        arguments = new ArrayList<>();
        // TODO;
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(callee + "." + name + "(");
        arguments.forEach(arg -> sb.append(arg + ","));
        sb.append(")");
        return sb.toString();
    }*/
}