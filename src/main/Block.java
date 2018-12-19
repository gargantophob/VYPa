/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */
package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Block {
    /** Context function. */    
    public Function contextFunction;
    /** Current scope. */
    public SymbolTable<Variable> scope;
    /** Statements inside the block. */
    public List<Statement> statements;

    /** Parse a block. */
    public static Block recognize(
        Function contextFunction, SymbolTable<Variable> parentScope,
    	GrammarParser.BlockContext ctx
    ) {
        Block b = new Block();
    	b.contextFunction = contextFunction;
        b.scope = new SymbolTable<>(parentScope);
        b.statements = Statement.recognize(contextFunction, b.scope, ctx);
        return b;
    }

    /** Indexate the block. */
    public void indexate() {
    	statements.forEach(s -> s.indexate());
    }
}
