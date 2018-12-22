/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/**
 * Path evaluation.
 */
public class Path {
    /** Path. */
    public List<Variable> path;
    /** Inferred type (type of the last object). */
    public Type type;

    /** Construct new path. */
    public Path (List<Variable> path) {
        this.path = path;
        type = path.get(path.size()-1).type;
    }

    /** Recognize path context. */
    public static Path recognize(
        Function contextFunction, SymbolTable<Variable> scope,
        GrammarParser.PathContext ctx
    ) {
        // Collect names from the syntax tree
        List<String> names = new ArrayList<>();
        ctx.atomicPath().forEach(ap -> names.add(ap.getText()));

        // Recognize names and construct a path
        List<Variable> path = new ArrayList<>();
        Variable current = scope.lookUpRecursively(names.get(0));
        path.add(current);
        for(int i = 1; i < names.size(); i++) {
            if(!(current.type instanceof Class)) {
                Recover.type(
                    contextFunction.name + ": " + current.name
                    + " is not an instance variable"
                );
            }
            Class currentClass = (Class) current.type;

            // Get next variable from the path
            String name = names.get(i);
            current = currentClass.lookUpAttribute(name);
            path.add(current);
        }

        // Construct
        return new Path(path);
    }

    /* ************************************************************************/

    /** Evaluate the path and push resulting value onto stack. */
    public void code() {
        Variable handle = path.get(0);
        
        if(path.size() == 1) {
            // atomic path
            if(type == Type.STRING) {
                // String copy
                Code.println("COPY $R " + handle.code());
                Code.push("$R");
            } else {
                Code.push(handle);
            }
            return;
        } 
        
        // Composite path
        Code.println("SET $R " + handle.code());
        for(int i = 1; i < path.size(); i++) {
            Code.println("GETWORD $R $R " + (path.get(i).index+1));
        }
        if(type == Type.STRING) {
            Code.println("COPY $R $R");
        }
        Code.push("$R"); 
    }
}