package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class StatementBlock {
    
    public SymbolTable<Variable> scope;
    public List<Statement> statements;

    public StatementBlock(List<parsed.Statement> parsed, SymbolTable<Variable> parentScope) {
        scope = new SymbolTable<>(parentScope);
        statements = new ArrayList<>();
        parsed.forEach(s -> statements.add(new Statement(s, scope)));
    }
}
