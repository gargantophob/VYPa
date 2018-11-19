package main;

import java.util.List;
import java.util.ArrayList;

public class Statement {
    public static enum Option {
        DECLARATION, ASSIGNMENT, CONDITIONAL, ITERATION, CALL, RETURN;
    }

    public Option option;

    public Variable variable;

    public Path path;
    public Expression expression;
    
    public StatementBlock body;
    public StatementBlock bodyElse;
    
    public Call call;

    public Statement(parsed.Statement parsed, SymbolTable<Variable> scope) {
        switch(parsed.option) {
            case DECLARATION:
                option = Option.DECLARATION;
                variable = new Variable(parsed.variable);
                SymbolTable.classes.assertNonExistence(variable.name);
                SymbolTable.functions.assertNonExistence(variable.name);
                scope.register(variable.name, variable);
                break;
            case ASSIGNMENT:
                option = Option.ASSIGNMENT;
                path = new Path(parsed.path, scope);
                expression = new Expression(parsed.expression, scope);
                break;
            case CONDITIONAL:
                option = Option.CONDITIONAL;
                expression = new Expression(parsed.expression, scope);
                body = new StatementBlock(parsed.body, scope);
                bodyElse = new StatementBlock(parsed.bodyElse, scope);
                break;
            case ITERATION:
                option = Option.ITERATION;
                expression = new Expression(parsed.expression, scope);
                body = new StatementBlock(parsed.body, scope);
                break;
            case CALL:
                option = Option.CALL;
                call = new Call(parsed.call, scope);
                break;
            case RETURN:
                option = Option.RETURN;
                if(parsed.expression != null) {
                    expression = new Expression(parsed.expression, scope);
                }
                break;
        }
    }
}