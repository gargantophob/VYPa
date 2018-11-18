package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class StatementBlock {
    public List<Statement> statements;
    
    public StatementBlock(List<Statement> statements) {
        this.statements = statements;
    }

    public static StatementBlock recognize(GrammarParser.BlockStatementContext ctx) {
        List<Statement> statements = new ArrayList<>();
        ctx.statement().forEach(s -> statements.addAll(Statement.recognize(s)));
        return new StatementBlock(statements);
    }

    @Override
    public String toString() {
        return statements.toString();
    }
}
