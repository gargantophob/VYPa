package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Statement {
    public static enum Option {
        DECLARATION, ASSIGNMENT, CONDITIONAL, ITERATION, CALL, RETURN;
    }

    public Function context;
    public SymbolTable<Variable> scope;

    public Option option;

    public Variable declaration;
    public Path path;
    public Expression ex;
    
    public Block blockTrue;
    public Block blockFalse;
    
    public Call call;

    public Statement(
        Function context, SymbolTable<Variable> scope,
        Variable declaration
    ) {
        this.context = context;
        this.scope = scope;
        
        option = Option.DECLARATION;
        this.declaration = declaration;

        SymbolTable.classes.assertNonExistence(declaration.name);
        SymbolTable.functions.assertNonExistence(declaration.name);
        scope.register(declaration.name, declaration);
    }

    public Statement(
        Function context, SymbolTable<Variable> scope,
        Path path, Expression ex
    ) {
        this.context = context;
        this.scope = scope;
        
        option = Option.ASSIGNMENT;
        this.path = path;
        this.ex = ex;

        if(context != null && path.handle == context.self && path.path == null) {
            Recover.semantic("cannot reassign 'this' variable");
        }

        Type lType = path.type;
        Type rType = ex.type;
        if(lType != rType) {
            if(!(lType instanceof Class) || !(rType instanceof Class)) {
                Recover.type(context.name + ": assignment primitive type mismatch");
            } else {
                Class lClass = (Class) lType;
                Class rClass = (Class) rType;
                if(!(rClass.isSubclassOf(lClass))) {
                    Recover.type(
                        context.name + ": class " + rClass.name + 
                        " cannot be assigned as " + lClass.name
                    );
                } else {
                    // Explicit cast
                    this.ex = new Expression(context, scope, lClass, ex);
                }
            }
        }
    }

    public Statement(
        Function context, SymbolTable<Variable> scope,
        Expression ex, Block blockTrue, Block blockFalse
    ) {
        this.context = context;
        this.scope = scope;
        
        option = Option.CONDITIONAL;
        this.ex = ex;
        this.blockTrue = blockTrue;
        this.blockFalse = blockFalse;
        
        if(ex.type == Type.STRING) {
            Recover.type(context.name + ": if() condition cannot be string");
        }
    }

    public Statement(
        Function context, SymbolTable<Variable> scope,
        Expression ex, Block blockTrue
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.ITERATION;
        this.ex = ex;
        this.blockTrue = blockTrue;

        if(ex.type == Type.STRING) {
            Recover.type(context.name + ": while() condition cannot be string");
        }
    }

    public Statement(
        Function context, SymbolTable<Variable> scope,
        Call call
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.CALL;
        this.call = call;
    }

    public Statement(
        Function context, SymbolTable<Variable> scope,
        Expression ex
    ) {
        this.context = context;
        this.scope = scope;

        option = Option.RETURN;
        this.ex = ex;

        if(ex == null) {
            if(context.type != Type.VOID) {
                Recover.type(context.name + ": return statement cannot be empty here");
            }
        } else {
            Type f = context.type;
            Type e = ex.type;
            if(f != e) {
                if(!(f instanceof Class) || !(e instanceof Class)) {
                    Recover.type(context.name + ": return type mismatch");
                } else if(!(((Class) e).isSubclassOf((Class) f))) {
                    Recover.type(context.name + ": return type mismatch");
                }
            }
        }
    }

    public static List<Statement> recognize(
        Function context, SymbolTable<Variable> scope, GrammarParser.StatementContext ctx
    ) {
        List<Statement> list = new ArrayList<>();
        if(ctx.variableDeclaration() != null) {
            Variable.recognize(ctx.variableDeclaration()).forEach(v ->
                list.add(new Statement(context, scope, v))
            );
            return list;
        }
        
        Statement s;
        if(ctx.assignment() != null) {
            s = new Statement(
                context, scope,
                new Path(context, scope, ctx.assignment().path()),
                Expression.recognize(context, scope, ctx.assignment().ex())
            );
        } else if(ctx.conditional() != null) {
            s = new Statement(
                context, scope,
                Expression.recognize(context, scope, ctx.conditional().ex()),
                new Block(context, scope, ctx.conditional().block().get(0)),
                new Block(context, scope, ctx.conditional().block().get(1))
            );
        } else if(ctx.iteration() != null) {
            s = new Statement(
                context, scope,
                Expression.recognize(context, scope, ctx.iteration().ex()),
                new Block(context, scope, ctx.iteration().block())
            );
        } else if(ctx.call() != null) {
            s = new Statement(context, scope, Call.recognize(context, scope, ctx.call()));
        // } else if(ctx.returnStatement() != null) {
        } else {
            Expression ex = null;
            if(ctx.returnStatement().ex() != null) {
                ex = Expression.recognize(context, scope, ctx.returnStatement().ex());
            }
            s = new Statement(context, scope, ex);
        }
        list.add(s);
        return list;
    }

    public static List<Statement> recognize(
        Function context, SymbolTable<Variable> scope, GrammarParser.BlockContext ctx
    ) {
        List<Statement> list = new ArrayList<>();
        ctx.statement().forEach(s -> list.addAll(Statement.recognize(context, scope, s)));
        return list;
    }
}