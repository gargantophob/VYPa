/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Path {
    /** Context function. */   
    public Function contextFunction;
    /** Current scope. */
    public SymbolTable<Variable> scope;
    /** Path. */
    public List<Variable> path;
    /** Inferred type. */
    public Type type;

    public Path (
        Function contextFunction, SymbolTable<Variable> scope,
        List<Variable> path
    ) {
        this.contextFunction = contextFunction;
        this.scope = scope;
        this.path = path;
        type = path.get(path.size()-1).type;
    }

    public static Path recognize(
        Function contextFunction, SymbolTable<Variable> scope,
        GrammarParser.PathContext ctx
    ) {
        // Collect names from the syntax tree
        List<String> names = new ArrayList<>();
        ctx.atomicPath().forEach(ap -> names.add(ap.getText()));

        List<Variable> path = new ArrayList<>();
        Variable current = scope.lookUpRecursively(names.get(0));
        path.add(current);
        for(int i = 1; i < names.size(); i++) {
            if(!(current.type instanceof Class)) {
                Recover.type(contextFunction.name + ": " + current.name + " is not an instance variable");
            }
            Class currentClass = (Class) current.type;
            String name = names.get(i);
            current = currentClass.lookUpAttribute(name);
            path.add(current);
        }

        // Type is the type of the last atomic path
        return new Path(contextFunction, scope, path);
    }
}