package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Expression {
    
    public static enum Option {
        LITERAL, PATH, NEW, INT2STRING, CAST, CALL,
        NEG, MULI, DIVI, ADDI, ADDS, SUBI,
        LTI, GTI, EQI, NEQI, LTS, GTS, EQS, NEQS,
        AND, OR
    }

    public Function context;
    public SymbolTable<Variable> scope;

    public Option option;

    public Literal literal;
    public Path path;
    public Class classNew;
    public Type typeCast;
    public Call call;

    public Expression op1;
    public Expression op2;

    public Type type;
    
    public static Expression recognize(
        Function context, SymbolTable<Variable> scope, GrammarParser.ExContext ctx
    ) {
        if(ctx.getChildCount() == 3 && ctx.getChild(0).getText().equals("(")) {
            // parentheses - skip
            return recognize(context, scope, ctx.ex().get(0));
        }

        if(ctx.arguments() != null) {
            // call
            return new Expression(
                context, scope, Call.recognize(context, scope, ctx)
            );
        }

        if(ctx.value() != null) {
            // direct value
            GrammarParser.ValueContext valueContext = ctx.value();
            if(valueContext.literal() != null) {
                // literal
                return new Expression(
                    context, scope, new Literal(valueContext.literal())
                );
            } else {
                // path
                return new Expression(
                    context, scope, new Path(context, scope, valueContext.path())
                );
            }
        }

        if(ctx.type() != null) {
            // cast
            Type typeCast = Type.recognize(ctx.type());
            Expression op = recognize(context, scope, ctx.ex().get(0));
            if(typeCast == op.type) {
                // identity cast
                return op;
            } else {
                return new Expression(context, scope, typeCast, op);
            }
        }

        if(ctx.name() != null) {
            // new
            return new Expression(
                context, scope, SymbolTable.classes.lookUp(ctx.name().getText())
            );
        }

        if(ctx.ex().size() == 1) {
            // negation
            return new Expression(
                context, scope, recognize(context, scope, ctx.ex().get(0))
            );
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
                op1 = new Expression(context, scope, Option.LTI, tmp1, tmp2);
                op2 = new Expression(context, scope, Option.EQI, tmp1, tmp2);
                option = Option.OR;
                break;
            case ">=":
                op1 = new Expression(context, scope, Option.GTI, tmp1, tmp2);
                op2 = new Expression(context, scope, Option.EQI, tmp1, tmp2);
                option = Option.OR;
                break;
            case "==": option = Option.EQI; break;
            case "!=": option = Option.NEQI; break;
            case "&&": option = Option.AND; break;
            case "||": option = Option.OR; break;
        }
        return new Expression(context, scope, option, op1, op2);        
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Literal literal
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.LITERAL;
        this.literal = literal;
        type = literal.type;
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Path path
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.PATH;
        this.path = path;
        type = path.type;
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Type typeCast, Expression op
    ) {
        this.context = context;
        this.scope = scope;

        this.typeCast = typeCast;
        this.type = typeCast;
        op1 = op;

        // can convert int to string or (dynamically) class to class
        if(typeCast == Type.STRING && op.type == Type.INT) {
            option = Option.INT2STRING;
        } else if(typeCast instanceof Class && op.type instanceof Class) {
            option = Option.CAST;
        } else {
            Recover.type(context.name + ": invalid cast");
        }
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Class classNew
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.NEW;
        this.classNew = classNew;
        type = classNew;
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Call call
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.CALL;
        this.call = call;
        if(call.type == Type.VOID) {
            Recover.type(
                context.name + ": " + call.function.name +
                " call returns void and cannot be an expression"
            );
        }
        type = call.type;
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Expression op
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.NEG;
        op1 = op;

        if(op.type == Type.STRING) {
            Recover.type(context.name + ": only integer and objects can be negated");
        }
        type = Type.INT;
    }

    public Expression(
        Function context, SymbolTable<Variable> scope,
        Option option, Expression op1, Expression op2
    ) {
        this.context = context;
        this.scope = scope;
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
                    Recover.type(context.name + ": only integers can be multiplied/divided");
                }
                break;
            case ADDI:
                if(bothInt) {
                } else if(bothString) {
                    option = Option.ADDS;
                } else {
                    Recover.type(context.name + ": addition type mismatch");
                }
                break;
            case SUBI:
                if(!bothInt) {
                    Recover.type(context.name + ": only integers can be subtracted");
                }
                break;
            case LTI:
                type = Type.INT;
                if(bothInt) {
                } else if(bothString) {
                    option = Option.LTS;
                } else {
                    Recover.type(context.name + ": relation operator type mismatch");
                }
                break;
            case GTI:
                type = Type.INT;
                if(bothInt) {
                } else if(bothString) {
                    option = Option.GTS;
                } else {
                    Recover.type(context.name + ": relation operator type mismatch");
                }
                break;
            case EQI:
                type = Type.INT;
                if(bothString) {
                    option = Option.EQS;
                } else if(t1 != t2 && !bothClass) {
                    Recover.type(context.name + ": relation operator type mismatch");
                }
                break;
            case NEQI:
                type = Type.INT;
                if(bothString) {
                    option = Option.NEQS;
                } else if(t1 != t2 && !bothClass) {
                    Recover.type(context.name + ": relation operator type mismatch");
                }
                break;
            case AND:
            case OR:
                type = Type.INT;
                if(t1 == Type.STRING || t2 != Type.STRING) {
                } else {
                    Recover.type(context.name + ": cannot apply logical operators on string");
                }
            break;
        }

        this.option = option;
    }

}
