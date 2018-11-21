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

    public Expression(Path path) {
    	option = Option.PATH;
    	this.path = path;
    }

    public Type type;

    public void inferType() {

        Type t1 = null;
        Type t2 = null;
        boolean errorFlag = false;

        if(literal != null) {

        }
        if(path != null) {
            path.inferType();
        }
        if(call != null) {
            call.inferType();
        }
        if(op1 != null) {
            op1.inferType();
            t1 = op1.type;
        }
        if(op2 != null) {
            op2.inferType();
            t2 = op1.type;
        }

        switch(option) {
            case LITERAL: type = literal.type; break;
            case PATH: type = path.type; break;
            case NEW: type = classNew; break;
            case CAST:
                type = typeCast;
                // TODO check
                break;
            case CALL:
                if(call.type != Type.VOID) {
                    type = call.type;
                } else {
                    errorFlag = true;
                }
                break;
            case NEG:
                if(t1 == Type.INT) {
                    type = Type.INT;
                } else {
                    errorFlag = true;
                }
                break;
            case MUL:
            case DIV:
                if(t1 == Type.INT && t2 == Type.INT) {
                    type = Type.INT;
                } else {
                    errorFlag = true;
                }
                break;
            case ADD:
                if(t1 == Type.INT && t2 == Type.INT) {
                    type = Type.INT;
                } else if(t1 == Type.STRING && t2 == Type.STRING) {
                    type = Type.STRING;
                } else {
                    errorFlag = true;
                }
                break;
            case SUB:
                if(t1 == Type.INT && t2 == Type.INT) {
                    type = Type.INT;
                } else {
                    errorFlag = true;
                }
                break;
            case LESS:
            case MORE:
            case LEQ:
            case MEQ:
            case EQ:
            case NEQ:
                if(t1 == Type.INT && t2 == Type.INT) {
                    type = Type.INT;
                } else if(t1 == Type.STRING && t2 == Type.STRING) {
                    type = Type.INT;
                } else if(t1 instanceof Class && t1 instanceof Class) {
                    type = Type.INT;
                } else {
                    errorFlag = true;
                }
                break;
            case AND:
            case OR:
                if(t1 == Type.STRING || t2 == Type.STRING) {
                    errorFlag = true;
                } else {
                    type = Type.INT;
                }
            break;
        }

        if(errorFlag) {
            Recover.exit(4, "type mismatch");
        }
    }
}
