package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Block {
    
    public Function context;
    public SymbolTable<Variable> scope;
    public List<Statement> statements;

    public Block(Function context, SymbolTable<Variable> parentScope, GrammarParser.BlockContext ctx) {
    	this.context = context;
        scope = new SymbolTable<>(parentScope);
        statements = Statement.recognize(context, scope, ctx);
    }
}
