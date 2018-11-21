package main;

import parser.GrammarParser;

public class Type {

    protected Type() {}
    
    public static Type VOID = new Type();
    public static Type INT = new Type();
    public static Type STRING = new Type();
    
    public static Type recognize(GrammarParser.TypeContext ctx) {
        String text = ctx.getText();
        switch(text) {
            case "void": return VOID;
            case "int": return INT;
            case "string": return STRING;
            default: return SymbolTable.classes.lookUp(text);
        }
    }
}