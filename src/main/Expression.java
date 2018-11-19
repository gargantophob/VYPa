package main;

import java.util.List;
import java.util.ArrayList;

public class Expression {
    public static enum Option {
        LITERAL, PATH, NEW, CAST, CALL,
        NEG, MUL, DIV, ADD, SUB,
        LESS, MORE, LEQ, MEQ, EQ, NEQ, AND, OR
    }

    public Option option;

    public Literal literal;
    public Path path;
    public Class classNew;
    public Type typeCast;
    public Call call;

    public Expression op1;
    public Expression op2;

    public Expression(parsed.Expression parsed, SymbolTable<Variable> scope) {

    	if(parsed.literal != null) {
			literal = new Literal(parsed.literal);
    	}
    	if(parsed.path != null) {
	    	path = new Path(parsed.path, scope);
    	}
    	if(parsed.classNew != null) {
	    	classNew = SymbolTable.classes.lookUp(parsed.classNew);
    	}
    	if(parsed.typeCast != null) {
			typeCast = Type.recognize(parsed.typeCast);
    	}
    	if(parsed.call != null) {
			call = new Call(parsed.call, scope);
    	}
    	if(parsed.op1 != null) {
	    	op1 = new Expression(parsed.op1, scope);
    	}
    	if(parsed.op2 != null) {
			op2 = new Expression(parsed.op2, scope);
    	}

    	switch(parsed.option) {
    		case LITERAL: option = Option.LITERAL; break;
    		case PATH: option = Option.PATH; break;
    		case NEW: option = Option.NEW; break;
    		case CAST: option = Option.CAST; break;
    		case CALL: option = Option.CALL; break;
    		case NEG: option = Option.NEG; break;
    		case MUL: option = Option.MUL; break;
    		case DIV: option = Option.DIV; break;
    		case ADD: option = Option.ADD; break;
    		case SUB: option = Option.SUB; break;
    		case LESS: option = Option.LESS; break;
    		case MORE: option = Option.MORE; break;
    		case LEQ: option = Option.LEQ; break;
    		case MEQ: option = Option.MEQ; break;
    		case EQ: option = Option.EQ; break;
    		case NEQ: option = Option.NEQ; break;
    		case AND: option = Option.AND; break;
    		case OR: option = Option.OR; break;
    	}
    }

    /*public Expression(Literal literal) {
    	option = Option.LITERAL;
    	this.literal = literal;
    }*/

    public Expression(Path path) {
    	option = Option.PATH;
    	this.path = path;
    }
}
