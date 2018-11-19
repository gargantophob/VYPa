package main;

import java.util.List;
import java.util.ArrayList;

public class Path {
    public Variable root;
    public List<Variable> path;
    
    public Path(List<String> path, Scope scope) {
        root = scope.variableLookUp(path.get(0));
        this.path = new ArrayList<>();

        Variable handle = root;
        for(int i = 1; i < path.size(); i++) {
            Class handleClass = handle.type.classRef;
            if(handleClass == null) {
                Recover.exit(3, handle.name + " is not an instance variable");
            }
            String name = path.get(i);
            if(name.equals("this")) {
                Recover.exit(3, "cannot cascade this");
            }
            handle = handleClass.scope.variableLookUp(name);
            this.path.add(handle);
        }
    }

    public Variable lastVariable() {
        return path.size() == 0 ? root : path.get(path.size()-1);
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(root);
        for(int i = 1; i < path.size(); i++) {
            sb.append('.' + path.get(i));
        }
        return sb.toString();
    }*/
}