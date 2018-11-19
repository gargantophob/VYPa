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
        return new StatementBlock(Statement.recognize(ctx));
    }

    @Override
    public String toString() {
        return statements.toString();
    }
}
