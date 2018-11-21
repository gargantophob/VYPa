package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Path {
    
    public Function context;
    public SymbolTable<Variable> scope;

    public Variable handle;
    public List<Variable> path;
    public Type type;

    public Path(
        Function context, SymbolTable<Variable> scope, GrammarParser.PathContext ctx
    ) {
        this.context = context;
        this.scope = scope;

        // Collect names from the syntax tree
        List<String> names = new ArrayList<>();
        ctx.atomicPath().forEach(ap -> names.add(ap.getText()));

        // Get handle
        handle = scope.lookUpRecursively(names.get(0));
        if(names.size() == 1) {
            type = handle.type;
            return;
        }

        // Process appendix
        path = new ArrayList<>();
        Variable current = handle;
        for(int i = 1; i < names.size(); i++) {
            if(!(current.type instanceof Class)) {
                Recover.type(context.name + ": " + current.name + " is not an instance variable");
            }
            Class currentClass = (Class) current.type;
            String name = names.get(i);
            current = currentClass.lookUpAttribute(name);
            this.path.add(current);
        }

        // Type is the type of the last address
        type = path.get(path.size()-1).type;
    }
}