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
    
    public Literal literal;
    public Path path;
    public String className;
    public Type castType;
    public Call call;

    public Expression op1;
    public Expression op2;    
    
    public Expression(GrammarParser.ExContext ctx) {
        int length = ctx.getChildCount();

        option = null;
        literal = null;
        path = null;
        className = null;
        castType = null;
        call = null;
        op1 = null;
        op2 = null;
        
        while(true) {
            if(length == 1) {
                // Direct value or function call
                if(ctx.value() != null) {
                    GrammarParser.ValueContext valueContext = ctx.value();
                    if(valueContext.literal() != null) {
                        option = Option.LITERAL;
                        literal = new Literal(valueContext.literal());
                    } else {
                        option = Option.PATH;
                        path = new Path(valueContext.path());
                    }
                } else {
                    option = Option.CALL;
                    call = new Call(ctx.call());
                }
            } else if(length == 2) {
                // New or negation
                if(ctx.name() != null) {
                    option = Option.NEW;
                    className = ctx.name().getText();
                } else {
                    option = Option.NEG;
                    op1 = new Expression(ctx.ex().get(0));
                }
            } else if(length == 3) {
                // Binary operation or brackets
                
                if(ctx.getChild(0).getText().equals("(")) {
                    ctx = ctx.ex().get(0);
                    continue;
                }

                op1 = new Expression(ctx.ex().get(0));
                op2 = new Expression(ctx.ex().get(1));

                String op = ctx.getChild(1).getText();
                switch(ctx.getChild(1).getText()) {
                    case "*": option = Option.MULT; break;
                    case "/": option = Option.DIV; break;
                    case "+": option = Option.ADD; break;
                    case "-": option = Option.SUB; break;
                    case "<": option = Option.LESS; break;
                    case ">": option = Option.MORE; break;
                    case "<=": option = Option.LEQ; break;
                    case ">=": option = Option.MEQ; break;
                    case "==": option = Option.EQ; break;
                    case "!=": option = Option.NEQ; break;
                    case "&&": option = Option.AND; break;
                    case "||": option = Option.OR; break;
                    default: main.Recover.warn("!"); break;
                }
            } else {
                // Cast
                option = Option.CAST;
                castType = new Type(ctx.type());
                op1 = new Expression(ctx.ex().get(0));
            }
            break;
        }
    }

    /*@Override
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
    }*/
}
