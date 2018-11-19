package main;

import java.util.List;
import java.util.ArrayList;

public class Path {
    
    public Variable root;
    public List<Variable> path;
    
    public Path(parsed.Path parsed, SymbolTable<Variable> scope) {
        List<String> names = parsed.names;
        root = scope.lookUpRecursively(names.get(0));
        path = new ArrayList<>();

        Variable handle = root;
        for(int i = 1; i < names.size(); i++) {
            if(!(handle.type instanceof Class)) {
                Recover.exit(3, handle.name + " is not an instance variable");
            }
            Class handleClass = (Class) handle.type;
            String name = names.get(i);
            handle = handleClass.lookUpAttribute(name);
            this.path.add(handle);
        }
    }

    public Variable lastVariable() {
        if(path.size() == 0) {
            return root;
        } else {
            return path.get(path.size()-1);
        }
    }
}