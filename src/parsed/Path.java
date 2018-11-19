package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Path {
    
    public List<String> names;
    
    public Path(GrammarParser.PathContext ctx) {
        names = new ArrayList<>();
        ctx.atomicPath().forEach(ap -> names.add(ap.getText()));
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(path.get(0));
        for(int i = 1; i < path.size(); i++) {
            sb.append('.' + path.get(i));
        }
        return sb.toString();
    }*/
}