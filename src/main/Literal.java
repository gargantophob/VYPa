package main;

import parser.GrammarParser;
import org.antlr.v4.runtime.Token;

import java.util.List;
import java.util.ArrayList;

public class Literal {

    public Type type;
    public String text;
    public int intValue;

    public Literal(GrammarParser.LiteralContext ctx) {
        Token token = (Token) ctx.getChild(0).getPayload();
        text = token.getText();
        
        if(token.getType() == GrammarParser.IntegerLiteral) {
            type = Type.INT;
            try {
                intValue = Integer.parseInt(text);
            } catch(NumberFormatException e) {
                Recover.internal("string to integer conversion failed");
            }
        } else {
            type = Type.STRING;
            // text = text.substring(1, text.length()-1); // removes quotes
        }
    }

    /*public Literal(int intValue) {
        type = Type.INT;
        text = "" + intValue; // FIXME
        this.intValue = intValue;
    }

    public Literal(String text) {
        type = Type.STRING;
        this.text = text;
    }*/
}