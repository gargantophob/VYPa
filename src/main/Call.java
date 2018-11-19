package main;

import java.util.List;
import java.util.ArrayList;

public class Call  {
    
    public Scope scope;

    public boolean isSuper;
    public Path callee;

    public List<Expression> arguments;

    public Function function;
    
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
}