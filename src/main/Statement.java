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

    public Statement(Variable variable) {
        option = Option.DECLARATION;
        this.variable = variable;
    }

    public Statement(Path path, Expression expression) {
        option = Option.ASSIGNMENT;
        this.path = path;
        this.expression = expression;
    }

    public Statement(Expression expression, StatementBlock body, StatementBlock bodyElse) {
        option = Option.CONDITIONAL;
        this.expression = expression;
        this.body = body;
        this.bodyElse = bodyElse;
    }

    public Statement(Expression expression, StatementBlock body) {
        option = Option.ITERATION;
        this.expression = expression;
        this.body = body;
    }

    public Statement(Call call) {
        option = Option.CALL;
        this.call = call;
    }

    public Statement(Expression expression) {
        option = Option.RETURN;
        this.expression = expression;
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