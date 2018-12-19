/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

/**
 * Three primitive types + type recognizer.
 */
public class Type {

    public static Type VOID = new Type();
    public static Type INT = new Type();
    public static Type STRING = new Type();
    
    /** Parse type context. */
    public static Type recognize(GrammarParser.TypeContext ctx) {
        String text = ctx.getText();
        switch(text) {
            case "void": return VOID;
            case "int": return INT;
            case "string": return STRING;
            default: return Class.table.lookUp(text);
        }
    }
}