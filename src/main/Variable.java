/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */
package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/**
 * Variable declaration.
 */
public class Variable implements Named {

    /** Variable type. */    
    public Type type;
    /** Variable name. */
    public String name;
    
    /** Construct a variable. */
    public Variable(Type type, String name) {
        if(type == Type.VOID) {
            Recover.type("cannot declare variable of void type");
        }
        this.type = type;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    /** Recognize multiple variable declarations. */
    public static List<Variable> recognize(
        GrammarParser.VariableDeclarationContext ctx
    ) {
        List<Variable> list = new ArrayList<>();
        Type type = Type.recognize(ctx.type());
        ctx.name().forEach(name -> list.add(new Variable(type, name.getText())));
        return list;
    }

    /** Recognize a formal parameter context. */
    public static Variable recognize(GrammarParser.FormalParameterContext ctx) {
        return new Variable(Type.recognize(ctx.type()), ctx.name().getText());
    }

    
    /* ************************************************************************/
    
    /** Variable index. */
    public int index;

    /** Indexate the variable. */
    public void indexate(int index) {
        this.index = index;
    }

    /**
     * Treat the variable as a local one and generate its access via
     * the framepointer.
     */
    public String code() {
        return String.format("[$FP%+d]", index);
    }
}
