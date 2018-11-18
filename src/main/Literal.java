package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Literal {
    public boolean isInteger;
    public int intValue;
    public String stringValue;

    public Literal(int value) {
        isInteger = true;
        intValue = value;
    }

    public Literal(String value) {
        isInteger = false;
        stringValue = value;
    }

    public static Literal recognize(GrammarParser.LiteralContext ctx) {
        Token token = (Token) ctx.getChild(0).getPayload();
        String text = token.getText();
        if(token.getType() == GrammarParser.IntegerLiteral) {
            try {
                return new Literal(Integer.parseInt(text));
            } catch(NumberFormatException e) {
                System.err.println("Something went wrong...");
                return null;
            }
        } else {
            return new Literal(text.substring(1, text.length()-1));
        }
    }

    @Override
    public String toString() {
        if(isInteger) {
            return "" + intValue;
        } else {
            return stringValue;
        }
    }
}