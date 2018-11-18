package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Expression {
    public static enum ExpressionType {
        INTEGER, STRING, PATH, NEW, CAST, CALL,
        NEG, MULT, DIV, ADD, SUB,
        LESS, MORE, LEQ, MEQ, EQ, NEQ, AND, OR
    }

    public ExpressionType type;
    public Expression op1;
    public Expression op2;

    public VariablePath path;
    public Literal literal;

    public String className;
    public Type castType;
    public Call call;

    public Expression(
        ExpressionType type, Expression op1, Expression op2,
        VariablePath path, Literal literal, String className, Type castType, Call call
    ) {
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
        this.path = path;
        this.literal = literal;
        this.className = className;
        this.castType = castType;
        this.call = call;
    }

    public static Expression recognize(GrammarParser.ExContext ctx) {
        int length = ctx.getChildCount();

        ExpressionType type = null;
        Expression op1 = null;
        Expression op2 = null;
        
        VariablePath path = null;
        Literal literal = null;
        String className = null;
        Type castType = null;
        Call call = null;

        if(length == 1) {
            // Direct value or function call
            if(ctx.value() != null) {
                GrammarParser.ValueContext valueContext = ctx.value();
                if(valueContext.variable() != null) {
                    type = ExpressionType.PATH;
                    path = VariablePath.recognize(valueContext.variable());
                } else {
                    literal = Literal.recognize(valueContext.literal());
                    if(literal.isInteger) {
                        type = ExpressionType.INTEGER;
                    } else {
                        type = ExpressionType.STRING;
                    }
                }
            } else {
                type = ExpressionType.CALL;
                call = Call.recognize(ctx.call());
            }
        } else if(length == 2) {
            // New or negation
            RuleContext context = (RuleContext) ctx.getChild(1);
            if(context.getRuleIndex() == GrammarParser.RULE_name) {
                type = ExpressionType.NEW;
                className = ctx.name().getText();
                Recover.warn("new");
            } else {
                type = ExpressionType.NEG;
                op1 = recognize((GrammarParser.ExContext) ctx.getChild(1));
            }
        } else if(length == 3) {
            // Binary operation or brackets
            
            if(ctx.getChild(0).getText().equals("(")) {
                return recognize((GrammarParser.ExContext) ctx.getChild(1));
            }

            op1 = recognize((GrammarParser.ExContext) ctx.getChild(0));
            op2 = recognize((GrammarParser.ExContext) ctx.getChild(2));

            String op = ctx.getChild(1).getText();
            if(op.equals("*")) {
                type = ExpressionType.MULT;
            } else if(op.equals("/")) {
                type = ExpressionType.DIV;
            } else if(op.equals("+")) {
                type = ExpressionType.ADD;
            } else if(op.equals("-")) {
                type = ExpressionType.SUB;
            } else if(op.equals("<")) {
                type = ExpressionType.LESS;
            } else if(op.equals(">")) {
                type = ExpressionType.MORE;
            } else if(op.equals("<=")) {
                type = ExpressionType.LEQ;
            } else if(op.equals(">=")) {
                type = ExpressionType.MEQ;
            } else if(op.equals("==")) {
                type = ExpressionType.EQ;
            } else if(op.equals("!=")) {
                type = ExpressionType.NEQ;
            } else if(op.equals("&&")) {
                type = ExpressionType.AND;
            } else {
                type = ExpressionType.OR;
            }
        } else {
            // Cast
            type = ExpressionType.CAST;
            castType = Type.recognize(ctx.type());
            op1 = recognize((GrammarParser.ExContext) ctx.ex());
        }

        return new Expression(type, op1, op2, path, literal, className, castType, call);
    }

    public void assertDefineteness(Scope scope) {
        switch(type) {
            case INTEGER: case STRING: break;
            case PATH: path.assertDefineteness(scope); break;
            case NEW: Recover.notImplemented(); break;
            case CAST: Recover.notImplemented(); break;
            case CALL: Recover.notImplemented(); break;
            case NEG: op1.assertDefineteness(scope); break;
            default: op1.assertDefineteness(scope); op2.assertDefineteness(scope); break;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        switch(type) {
            case INTEGER: case STRING: sb.append(literal); break;
            case PATH: sb.append(path); break;
            case NEW: sb.append("new " + className); break;
            case CAST: sb.append("(" + className + ") " + op1); break;
            case CALL: sb.append(call); break;
            case NEG: sb.append(type + " " + op1); break;
            default: sb.append(op1 + " " + type + " " + op2); break;
        }
        sb.append(")");
        return sb.toString();
    }
}
