/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/** Expression. */
public class Expression {
    
    /** Expression types. */
    public static enum Option {
        LITERAL, PATH, NEW, INT2STRING, CAST, CALL,
        NEG, MULI, DIVI, ADDI, ADDS, SUBI,
        LTI, GTI, EQI, NEQI, LTS, GTS, EQS, NEQS,
        AND, OR
    }

    /** Expression type. */
    public Option option;

    /** Immediate operand. */
    public Literal literal;
    /** Path. */
    public Path path;
    /** Object creation. */
    public Class classNew;
    /** Class cast. */
    public Type classCast;
    /** Function call. */
    public Call call;

    /** Operand 1.*/
    public Expression op1;
    /** Operand 2.*/
    public Expression op2;

    /** Inferred type. */
    public Type type;

    /** Create literal expression. */
    public Expression(Literal literal) {
        option = Option.LITERAL;
        this.literal = literal;
        type = literal.type;
    }

    /** Create path expression.*/
    public Expression(Path path) {
        option = Option.PATH;
        this.path = path;
        type = path.type;
    }

    /** Create cast expression. */
    public Expression(Type type, Expression op) {
        op1 = op;

        // can convert int to string or (dynamically) class to class
        if(type == Type.STRING && op.type == Type.INT) {
            option = Option.INT2STRING;
        } else if(type instanceof Class && op.type instanceof Class) {
            option = Option.CAST;
            this.classCast = classCast;
        } else {
            Recover.type("invalid cast");
        }
        this.type = type;
    }

    /** Create object creation. */
    public Expression(Class classNew) {
        option = Option.NEW;
        this.classNew = classNew;
        type = classNew;
    }

    /** Create expression call. */
    public Expression(Call call) {
        option = Option.CALL;
        this.call = call;
        if(call.type == Type.VOID) {
            Recover.type("call returns void and cannot be an expression");
        }
        type = call.type;
    }

    /** Create unary negation. */
    public Expression(Expression op) {
        option = Option.NEG;
        op1 = op;

        if(op.type == Type.STRING) {
            Recover.type("only integer and objects can be negated");
        }
        type = Type.INT;
    }

    /** Create binary operation. */
    public Expression(Option option, Expression op1, Expression op2) {
        this.op1 = op1;
        this.op2 = op2;

        Type t1 = op1.type;
        Type t2 = op2.type;
        
        boolean bothInt = (t1 == Type.INT) && (t2 == Type.INT);
        boolean bothString = (t1 == Type.STRING) && (t2 == Type.STRING);
        boolean bothClass = (t1 instanceof Class) && (t2 instanceof Class);
        type = t1;

        switch(option) {
            case MULI:
            case DIVI:
                if(!bothInt) {
                    Recover.type("only integers can be multiplied/divided");
                }
                break;
            case ADDI:
                if(bothInt) {
                } else if(bothString) {
                    option = Option.ADDS;
                } else {
                    Recover.type("addition type mismatch");
                }
                break;
            case SUBI:
                if(!bothInt) {
                    Recover.type("only integers can be subtracted");
                }
                break;
            case LTI:
                type = Type.INT;
                if(bothInt) {
                } else if(bothString) {
                    option = Option.LTS;
                } else {
                    Recover.type("relation operator type mismatch");
                }
                break;
            case GTI:
                type = Type.INT;
                if(bothInt) {
                } else if(bothString) {
                    option = Option.GTS;
                } else {
                    Recover.type("relation operator type mismatch");
                }
                break;
            case EQI:
                type = Type.INT;
                if(bothString) {
                    option = Option.EQS;
                } else if(t1 != t2 && !bothClass) {
                    Recover.type("relation operator type mismatch");
                }
                break;
            case NEQI:
                type = Type.INT;
                if(bothString) {
                    option = Option.NEQS;
                } else if(t1 != t2 && !bothClass) {
                    Recover.type("relation operator type mismatch");
                }
                break;
            case AND:
            case OR:
                type = Type.INT;
                if(t1 == Type.STRING || t2 != Type.STRING) {
                } else {
                    Recover.type("cannot apply logical operators on string");
                }
            break;
        }

        this.option = option;
    }

    /** Parse expression context. */    
    public static Expression recognize(
        Function context, SymbolTable<Variable> scope, GrammarParser.ExContext ctx
    ) {
        if(ctx.getChildCount() == 3 && ctx.getChild(0).getText().equals("(")) {
            // parentheses - skip
            return recognize(context, scope, ctx.ex().get(0));
        }

        if(ctx.arguments() != null) {
            // call
            return new Expression(Call.recognize(context, scope, ctx));
        }

        if(ctx.value() != null) {
            // direct value
            GrammarParser.ValueContext valueContext = ctx.value();
            if(valueContext.literal() != null) {
                // literal
                return new Expression(
                    Literal.recognize(valueContext.literal())
                );
            } else {
                // path
                return new Expression(
                    Path.recognize(context, scope, valueContext.path())
                );
            }
        }

        if(ctx.type() != null) {
            // cast
            Type type = Type.recognize(ctx.type());
            Expression op = recognize(context, scope, ctx.ex().get(0));
            if(type == op.type) {
                // identity cast
                return op;
            } else {
                return new Expression(type, op);
            }
        }

        if(ctx.name() != null) {
            // new
            return new Expression(Class.table.lookUp(ctx.name().getText()));
        }

        if(ctx.ex().size() == 1) {
            // negation
            return new Expression(recognize(context, scope, ctx.ex().get(0)));
        }

        // binary operation
        Expression op1 = recognize(context, scope, ctx.ex().get(0));
        Expression op2 = recognize(context, scope, ctx.ex().get(1));
        Expression tmp1 = op1;
        Expression tmp2 = op2;
        Option option = null;

        switch(ctx.getChild(1).getText()) {
            case "*": option = Option.MULI; break;
            case "/": option = Option.DIVI; break;
            case "+": option = Option.ADDI; break;
            case "-": option = Option.SUBI; break;
            case "<": option = Option.LTI; break;
            case ">": option = Option.GTI; break;
            case "<=": 
                op1 = new Expression(Option.LTI, tmp1, tmp2);
                op2 = new Expression(Option.EQI, tmp1, tmp2);
                option = Option.OR;
                break;
            case ">=":
                op1 = new Expression(Option.GTI, tmp1, tmp2);
                op2 = new Expression(Option.EQI, tmp1, tmp2);
                option = Option.OR;
                break;
            case "==": option = Option.EQI; break;
            case "!=": option = Option.NEQI; break;
            case "&&": option = Option.AND; break;
            case "||": option = Option.OR; break;
        }
        return new Expression(option, op1, op2);        
    }    
}
