/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

/**
 * Three primitive types + type recognizer.
 * @note Primitive types could have been implemented using enum, but it is
 * easier to create three empty instances to ensure compatibility and easier
 * usage with respect to the Class subclass.
 * @link Class
 */
public class Type {

    /** void */
    public static Type VOID = new Type();
    /** int */
    public static Type INT = new Type();
    /** string */
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