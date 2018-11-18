package main;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    public Function caller;
    public Function callee;

    public List<Expression> arguments;
    
    public boolean isSuper;
    
    // public Path callee;
    // public String name;

    /*public Call(
        boolean isSuper, Path callee, String name, List<Expression> arguments
    ) {
        this.isSuper = isSuper;
        this.callee = callee;
        this.name = name;
        this.arguments = arguments;    
    }*/

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(callee + "." + name + "(");
        arguments.forEach(arg -> sb.append(arg + ","));
        sb.append(")");
        return sb.toString();
    }*/

    /*public void assertDefineteness(Scope scope) {
        if(isSuper) {
            Recover.notImplemented();
        }
        if(callee != null) {
            Recover.notImplemented();
        }

        scope.assertExistenceOfFunction(name);
        arguments.forEach(arg -> arg.assertDefineteness(scope));
    }*/
}