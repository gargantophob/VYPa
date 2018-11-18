package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Type {

    static enum TypeType {
        VOID, INT, STRING, OBJECT;
    }

    public TypeType type;
    public String object;

    public Type(TypeType type, String object) {
        this.type = type;
        this.object = object;
    }

    public static Type recognize(GrammarParser.TypeContext ctx) {
        TypeType type = null;
        String object = null;;

        String txt = ctx.getText();
        if(txt.equals("void")) {
            type = TypeType.VOID;
        } else if(txt.equals("int")) {
            type = TypeType.INT;
        } else if(txt.equals("string")) {
            type = TypeType.STRING;
        } else {
            type = TypeType.OBJECT;
            object = txt;
        }

        return new Type(type, object);
    }
    
    @Override
    public String toString() {
        if(type == TypeType.OBJECT) {
            return object;
        } else {
            return type.toString();
        }
    }
}

