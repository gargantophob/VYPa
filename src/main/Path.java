package main;

import java.util.List;
import java.util.ArrayList;

public class Path {
    public Variable root;
    public List<Variable> path;
    
    public Path(parsed.Path path, SymbolTable<Variable> scope) {
        /*List<String> names = path.names;
        root = scope.lookUp(names.get(0));
        this.path = new ArrayList<>();

        Variable handle = root;
        for(int i = 1; i < names.size(); i++) {
            Class handleClass = handle.type.classRef;
            if(handleClass == null) {
                Recover.exit(3, handle.name + " is not an instance variable");
            }
            String name = names.get(i);
            if(name.equals("this")) {
                Recover.exit(3, "cannot cascade this");
            }
            handle = handleClass.scope.lookUp(name);
            this.path.add(handle);
        }*/
    }

    /*public Variable lastVariable() {
        return path.size() == 0 ? root : path.get(path.size()-1);
    }*/
}