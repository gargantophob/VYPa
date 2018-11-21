package main;

import java.util.List;
import java.util.ArrayList;

public class Literal {

    public Type type;
    public String text;
    public int intValue;

    public Literal(parsed.Literal parsed) {
        text = parsed.text;
        
        if(parsed.isInteger) {
            type = Type.INT;
            try {
                intValue = Integer.parseInt(text);
            } catch(NumberFormatException e) {
                Recover.exit(99, "Something went wrong...");
            }
        } else {
            type = Type.STRING;
            text = text.substring(1, text.length()-1);
        }
    }

    public Literal(int intValue) {
        type = Type.INT;
        text = "" + intValue; // FIXME
        this.intValue = intValue;
    }

    public Literal(String text) {
        type = Type.STRING;
        this.text = text;
    }
}