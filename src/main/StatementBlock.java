package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class StatementBlock {
    
    public Scope scope;
    public List<Statement> statements;

    public StatementBlock(parsed.StatementBlock block, Scope parentScope) {
        scope = new Scope(parentScope);
        scope.variableAllow();
        scope.literalAllow();
        
        this.statements = new ArrayList<>();
        block.statements.forEach(s -> this.statements.add(new Statement(s, scope)));
    }
}
