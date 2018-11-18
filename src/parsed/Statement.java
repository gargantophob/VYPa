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

    public static List<Statement> recognize(GrammarParser.StatementContext ctx) {
        List<Statement> list = new ArrayList<>();
        switch(((RuleContext) ctx.getChild(0)).getRuleIndex()) {
            case GrammarParser.RULE_variableDeclaration:
                Variable.recognize(ctx.variableDeclaration()).forEach(var -> list.add(new Statement(var)));
                break;
            case GrammarParser.RULE_assignment:
                list.add(new Statement(
                    Path.recognize(
                        ctx.assignment().variable()), Expression.recognize(ctx.assignment().ex()
                    )
                ));
                break;
            case GrammarParser.RULE_conditional:
                List<GrammarParser.BlockStatementContext> blocks = ctx.conditional().blockStatement();
                list.add(new Statement(
                    Expression.recognize(ctx.conditional().ex()),
                    StatementBlock.recognize(blocks.get(0)),
                    StatementBlock.recognize(blocks.get(1))
                ));
                break;
            case GrammarParser.RULE_iteration:
                list.add(new Statement(
                    Expression.recognize(ctx.iteration().ex()),
                    StatementBlock.recognize(ctx.iteration().blockStatement())
                ));
                break;
            case GrammarParser.RULE_call:
                list.add(new Statement(Call.recognize(ctx.call())));
                break;
            case GrammarParser.RULE_returnStatement:
                Expression expression = null;
                GrammarParser.ExContext exContext = ctx.returnStatement().ex();
                if(exContext != null) {
                    expression = Expression.recognize(exContext);
                }
                list.add(new Statement(expression));
                break;
            default: // !
                main.Recover.warn("!");
                break;
        }
        return list;
    }

    @Override
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
    }

}