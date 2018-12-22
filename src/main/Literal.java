/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */
package main;

import parser.GrammarParser;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.ArrayList;

/**
 * Integer or string literal.
 */
public class Literal {

    /** Literal type, either INT or STRING. */
    public Type type;
    /** Text representation. */
    public String text;

    /** Construct a literal of default value. */
    public Literal(Type type) {
        this.type = type;
        text = type == Type.STRING ? "\"\"" : "0";
    }

    /** Construct a literal. */
    public Literal(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    /** Parse literal context. */
    public static Literal recognize(GrammarParser.LiteralContext ctx) {
        Token token = (Token) ctx.getChild(0).getPayload();
        String text = token.getText();
        Type type;
        if(token.getType() == GrammarParser.IntegerLiteral) {
            type = Type.INT;
        } else {
            type = Type.STRING;
        }
        return new Literal(type, text);
    }

    /** Push literal value (its text representation) onto stack. */
    public void code() {
        Code.push(text);
    }
}