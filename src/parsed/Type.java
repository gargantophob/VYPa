package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Type {
    
    public String text;
    
    public Type(GrammarParser.TypeContext ctx) {
        text = ctx.getText();
    }

    /*@Override
    public String toString() {
        return type;
    }*/
}

