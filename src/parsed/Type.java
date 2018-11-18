package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Type {
    public String type;
    
    public Type(String type) {
        this.type = type;
    }

    public static Type recognize(GrammarParser.TypeContext ctx) {
        return new Type(ctx.getText());
    }
    
    @Override
    public String toString() {
        return type;
    }
}

