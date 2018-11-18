package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Expression {
    public static enum Option {
        LITERAL, PATH, NEW, CAST, CALL,
        NEG, MULT, DIV, ADD, SUB,
        LESS, MORE, LEQ, MEQ, EQ, NEQ, AND, OR
    }

    public Option option;
    public Expression op1;
    public Expression op2;

    public Path path;
    public Literal literal;

    public String className;
    public Type castType;
    public Call call;

    public Expression(
        Option option, Expression op1, Expression op2,
        Path path, Literal literal, String className, Type castType, Call call
    ) {
        this.option = option;
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

        Option option = null;
        Expression op1 = null;
        Expression op2 = null;
        
        Path path = null;
        Literal literal = null;
        String className = null;
        Type castType = null;
        Call call = null;

        if(length == 1) {
            // Direct value or function call
            if(ctx.value() != null) {
                GrammarParser.ValueContext valueContext = ctx.value();
                if(valueContext.variable() != null) {
                    option = Option.PATH;
                    path = Path.recognize(valueContext.variable());
                } else {
                    option = Option.LITERAL;
                    literal = Literal.recognize(valueContext.literal());
                }
            } else {
                option = Option.CALL;
                call = Call.recognize(ctx.call());
            }
        } else if(length == 2) {
            // New or negation
            RuleContext context = (RuleContext) ctx.getChild(1);
            if(context.getRuleIndex() == GrammarParser.RULE_name) {
                option = Option.NEW;
                className = ctx.name().getText();
                main.Recover.warn("new");
            } else {
                option = Option.NEG;
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
                option = Option.MULT;
            } else if(op.equals("/")) {
                option = Option.DIV;
            } else if(op.equals("+")) {
                option = Option.ADD;
            } else if(op.equals("-")) {
                option = Option.SUB;
            } else if(op.equals("<")) {
                option = Option.LESS;
            } else if(op.equals(">")) {
                option = Option.MORE;
            } else if(op.equals("<=")) {
                option = Option.LEQ;
            } else if(op.equals(">=")) {
                option = Option.MEQ;
            } else if(op.equals("==")) {
                option = Option.EQ;
            } else if(op.equals("!=")) {
                option = Option.NEQ;
            } else if(op.equals("&&")) {
                option = Option.AND;
            } else {
                option = Option.OR;
            }
        } else {
            // Cast
            option = Option.CAST;
            castType = Type.recognize(ctx.type());
            op1 = recognize((GrammarParser.ExContext) ctx.getChild(3));
        }

        return new Expression(option, op1, op2, path, literal, className, castType, call);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        switch(option) {
            case LITERAL: sb.append(literal); break;
            case PATH: sb.append(path); break;
            case NEW: sb.append("new " + className); break;
            case CAST: sb.append("(" + className + ") " + op1); break;
            case CALL: sb.append(call); break;
            case NEG: sb.append(option + " " + op1); break;
            default: sb.append(op1 + " " + option + " " + op2); break;
        }
        sb.append(")");
        return sb.toString();
    }
}
