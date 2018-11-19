package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Literal {
    
    public boolean isInteger;
    public String text;

    public Literal(GrammarParser.LiteralContext ctx) {
        Token token = (Token) ctx.getChild(0).getPayload();
        isInteger = token.getType() == GrammarParser.IntegerLiteral;
        text = token.getText();
    }

    /*@Override
    public String toString() {
        return value;
    }*/
}