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
}
