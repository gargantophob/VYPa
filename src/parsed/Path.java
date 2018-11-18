package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Path {
    public List<String> path;
    
    public Path(List<String> path) {
        this.path = path;
    }

    public static Path recognize(GrammarParser.VariableContext ctx) {
        List<String> path = new ArrayList<>();
        ctx.atomicVariable().forEach(av -> path.add(av.getText()));
        return new Path(path);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(path.get(0));
        for(int i = 1; i < path.size(); i++) {
            sb.append('.' + path.get(i));
        }
        return sb.toString();
    }
}