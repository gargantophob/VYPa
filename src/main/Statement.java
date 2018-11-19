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

    public Statement(parsed.Statement parsed, Scope scope) {
        switch(parsed.option) {
            case DECLARATION:
                option = Option.DECLARATION;
                variable = new Variable(parsed.variable);
                scope.variableRegister(variable);
                break;
            case ASSIGNMENT:
                option = Option.ASSIGNMENT;
                path = new Path(parsed.path.path, scope);
                expression = null; // TODO;
                break;
            case CONDITIONAL:
                option = Option.CONDITIONAL;
                expression = null; // TODO
                body = new StatementBlock(parsed.body, scope);
                bodyElse = new StatementBlock(parsed.bodyElse, scope);
                break;
            case ITERATION:
                option = Option.ITERATION;
                expression = null; // TODO
                body = new StatementBlock(parsed.body, scope);
                break;
            case CALL:
                option = Option.CALL;
                call = new Call(parsed.call, scope);
                break;
            case RETURN:
                option = Option.RETURN;
                expression = null; // TODO
                break;
        }

    }

    /*@Override
    public String toString() {
        switch(option) {
            case DECLARATION: return variable.toString();
            case ASSIGNMENT: return path + " = " + expression;
            case CONDITIONAL: return "if(" + expression + ") { " + body + "} else { " + bodyElse + "}";
            case ITERATION: return "while(" + expression + ") { " + body + "}";
            case CALL: return call.toString();
            case RETURN: return "return " + expression + ";";
        }
        return null;
    }*/
}