package main;

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

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        switch(option) {
            case INTEGER: case STRING: sb.append(literal); break;
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
