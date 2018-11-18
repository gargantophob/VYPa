package main;

import java.util.List;
import java.util.ArrayList;

public class Path {
    public String root;
    public List<String> path;
    
    public Path(List<String> path) {
        root = path.get(0);
        this.path = path;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(root);
        for(int i = 1; i < path.size(); i++) {
            sb.append('.' + path.get(i));
        }
        return sb.toString();
    }

    /*public void assertSimpleness() {
        if(names.size() > 1) {
            Recover.notImplemented();
        }
    }

    public void assertDefineteness(Scope scope) {
        String name = names.get(0);
        scope.assertExistenceOfVariable(name);
    }*/
}