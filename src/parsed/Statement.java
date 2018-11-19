package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

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
    
    public List<Statement> body;
    public List<Statement> bodyElse;
    
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

    public Statement(Expression expression, List<Statement> body, List<Statement> bodyElse) {
        option = Option.CONDITIONAL;
        this.expression = expression;
        this.body = body;
        this.bodyElse = bodyElse;
    }

    public Statement(Expression expression, List<Statement> body) {
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

    public static List<Statement> recognize(GrammarParser.BlockContext ctx) {
        List<Statement> list = new ArrayList<>();
        ctx.statement().forEach(s -> list.addAll(Statement.recognize(s)));
        return list;
    }

    public static List<Statement> recognize(GrammarParser.StatementContext ctx) {
        List<Statement> list = new ArrayList<>();
        if(ctx.variableDeclaration() != null) {
            Variable.recognize(ctx.variableDeclaration()).forEach(v -> list.add(new Statement(v)));
            return list;
        }

        Statement s;
        if(ctx.assignment() != null) {
            s = new Statement(
                new Path(ctx.assignment().path()), new Expression(ctx.assignment().ex())
            );
        } else if(ctx.conditional() != null) {
            s = new Statement(
                new Expression(ctx.conditional().ex()),
                Statement.recognize(ctx.conditional().block().get(0)),
                Statement.recognize(ctx.conditional().block().get(1))
            );
        } else if(ctx.iteration() != null) {
            s = new Statement(
                new Expression(ctx.iteration().ex()),
                Statement.recognize(ctx.iteration().block())
            );
        } else if(ctx.call() != null) {
            s = new Statement(new Call(ctx.call()));
        } else if(ctx.returnStatement() != null) {
            Expression expression = null;
            if(ctx.returnStatement().ex() != null) {
                expression = new Expression(ctx.returnStatement().ex());
            }
            s = new Statement(expression);
        } else {
            main.Recover.warn("!");
            return null;
        }

        list.add(s);
        return list;
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