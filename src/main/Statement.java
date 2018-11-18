package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Statement {
    public static enum StatementType {
        DECLARATION, ASSIGNMENT, CONDITIONAL, ITERATION, CALL, RETURN;
    }

    public StatementType kind;

    public Variable variable;

    public VariablePath path;
    public Expression expression;
    
    public StatementBlock body;
    public StatementBlock bodyElse;
    
    public Call call;

    public Statement(Variable variable) {
        this.kind = StatementType.DECLARATION;
        this.variable = variable;
    }

    public Statement(VariablePath path, Expression expression) {
        this.kind = StatementType.ASSIGNMENT;
        this.path = path;
        this.expression = expression;
    }

    public Statement(Expression expression, StatementBlock body, StatementBlock bodyElse) {
        this.kind = StatementType.CONDITIONAL;
        this.expression = expression;
        this.body = body;
        this.bodyElse = bodyElse;
    }

    public Statement(Expression expression, StatementBlock body) {
        this.kind = StatementType.ITERATION;
        this.expression = expression;
        this.body = body;
    }

    public Statement(Call call) {
        this.kind = StatementType.CALL;
        this.call = call;
    }

    public Statement(Expression expression) {
        this.kind = StatementType.RETURN;
        this.expression = expression;
    }

    @Override
    public String toString() {
        switch(kind) {
            case DECLARATION: return variable.toString();
            case ASSIGNMENT: return path + " = " + expression;
            case CONDITIONAL: return "if(" + expression + ") { " + body + "} else { " + bodyElse + "}";
            case ITERATION: return "while(" + expression + ") { " + body + "}";
            case CALL: return call.toString();
            case RETURN: return "return " + expression + ";";
        }
        return null;
    }

    public static List<Statement> recognize(GrammarParser.VariableDeclarationContext ctx) {
        List<Statement> list = new ArrayList<>();
        Type type = Type.recognize(ctx.type());
        ctx.name().forEach(var -> list.add(new Statement(new Variable(type, var.getText()))));
        return list;
    }

    public static Statement recognize(GrammarParser.AssignmentContext ctx) {
        return new Statement(
            VariablePath.recognize(ctx.variable()), Expression.recognize(ctx.ex())
        );
    }

    public static Statement recognize(GrammarParser.ConditionalContext ctx) {
        List<GrammarParser.BlockStatementContext> blocks = ctx.blockStatement();
        return new Statement(
            Expression.recognize(ctx.ex()),
            StatementBlock.recognize(blocks.get(0)),
            StatementBlock.recognize(blocks.get(1))
        );
    }

    public static Statement recognize(GrammarParser.IterationContext ctx) {
        return new Statement(
            Expression.recognize(ctx.ex()),
            StatementBlock.recognize(ctx.blockStatement())
        );
    }

    public static Statement recognize(GrammarParser.CallContext ctx) {
        return new Statement(Call.recognize(ctx));
    }

    public static Statement recognize(GrammarParser.ReturnStatementContext ctx) {
        Expression expression = null;
        if(ctx.ex() != null) {
            expression = Expression.recognize(ctx.ex());
        }
        return new Statement(expression);
    }

    /****/

    public static List<Statement> recognize(GrammarParser.StatementContext ctx) {
        RuleContext context = (RuleContext) ctx.getChild(0);
        List<Statement> list = new ArrayList<>();
        switch(context.getRuleIndex()) {
            case GrammarParser.RULE_variableDeclaration:
                list.addAll(Statement.recognize(ctx.variableDeclaration()));
                break;
            case GrammarParser.RULE_assignment:
                list.add(Statement.recognize(ctx.assignment()));
                break;
            case GrammarParser.RULE_conditional:
                list.add(Statement.recognize(ctx.conditional()));
                break;
            case GrammarParser.RULE_iteration:
                list.add(Statement.recognize(ctx.iteration()));
                break;
            case GrammarParser.RULE_call:
                list.add(Statement.recognize(ctx.call()));
                break;
            case GrammarParser.RULE_returnStatement:
                list.add(Statement.recognize(ctx.returnStatement()));
                break;
            default: // !
                Recover.warn("!");
                break;
        }
        return list;
    }
}