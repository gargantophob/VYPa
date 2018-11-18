package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Literal {
    public boolean isInteger;
    public String value;

    public Literal(boolean isInteger, String value) {
        this.isInteger = isInteger;
        this.value = value;
    }

    public static Literal recognize(GrammarParser.LiteralContext ctx) {
        Token token = (Token) ctx.getChild(0).getPayload();
        return new Literal(token.getType() == GrammarParser.IntegerLiteral, token.getText());
    }

    @Override
    public String toString() {
        return value;
    }
}