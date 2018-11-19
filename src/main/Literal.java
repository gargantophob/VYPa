package main;

import java.util.List;
import java.util.ArrayList;

public class Literal {
    
    public boolean isInteger;
    public int intValue;
    public String stringValue;

    public Literal(parsed.Literal parsed) {
        isInteger = parsed.isInteger;
        String text = parsed.text;
        if(isInteger) {
            try {
                intValue = Integer.parseInt(text);
            } catch(NumberFormatException e) {
                Recover.exit(99, "Something went wrong...");
            }
        } else {
            stringValue = text.substring(1, text.length()-1);
        }
    }

    public Literal(int intValue) {
        isInteger = true;
        this.intValue = intValue;
    }

    public Literal(String stringValue) {
        isInteger = false;
        this.stringValue = stringValue;
    }
}