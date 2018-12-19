/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */
package main;

import parser.GrammarParser;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.ArrayList;

public class Literal {

    /** Literal type, either INT or STRING. */
    public Type type;
    /** Text payload. */
    public String text;
    /** Integer value for integer literals. */
    public int intValue;

    /** Construct a literal. */
    public Literal(Type type, String text, int intValue) {
        this.type = type;
        this.text = text;
        this.intValue = intValue;
    }

    /** Parse a literal context. */
    public static Literal recognize(GrammarParser.LiteralContext ctx) {
        Token token = (Token) ctx.getChild(0).getPayload();
        
        String text = token.getText();
        Type type = token.getType() == GrammarParser.IntegerLiteral ? Type.INT : Type.STRING;
        int intValue = 0;
        
        if(type == Type.INT) {
            try {
                intValue = Integer.parseInt(text);
            } catch(NumberFormatException e) {
                Recover.internal("string to integer conversion failed");
            }
        }

        return new Literal(type, text, intValue);
    }
}