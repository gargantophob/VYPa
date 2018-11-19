package main;

import java.util.List;
import java.util.ArrayList;

public class Literal {
    public boolean isInteger;
    public int intValue;
    public String stringValue;

    public Literal(boolean isInteger, String text) {
        this.isInteger = isInteger;
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

    public Literal(String value) {
        isInteger = false;
        stringValue = value;
    }
}