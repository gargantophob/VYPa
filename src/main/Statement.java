/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/**
 * Statement.
 */
public class Statement {

    /** Statement type. */
    public static enum Option {
        DECLARATION, ASSIGNMENT, CONDITIONAL, ITERATION, CALL, RETURN;
    }

    /** Context function. */
    public Function contextFunction;

    /** Statement type. */
    public Option option;

    /** Declared variable. */
    public Variable declared;
    
    /** Assignment path. */
    public Path path;
    /** Expression (assignment, conditional, iteration, return statement). */
    public Expression ex;
    
    /** True branch (conditional, iteration). */
    public Block blockTrue;
    /** False branch (conditional). */
    public Block blockFalse;
    
    /** Function call. */
    public Call call;

    /** Parse a statement. */
    public static List<Statement> recognize(
        Function contextFunction, SymbolTable<Variable> scope,
        GrammarParser.StatementContext ctx
    ) {
        List<Statement> list = new ArrayList<>();
        
        if(ctx.variableDeclaration() != null) {
            Variable.recognize(ctx.variableDeclaration()).forEach(v -> {
                // Local variable cannot have the same name as some class or
                // global function
                Class.table.assertNonExistence(v.name);
                Function.table.assertNonExistence(v.name);

                // Register variable
                scope.register(v);

                // Create a statement for initialization
                Statement s = new Statement();
                s.contextFunction = contextFunction;
                s.option = Option.DECLARATION;
                s.declared = v;
                list.add(s);
            });
            return list;
        }
        
        Statement s = new Statement();
        Option option = null;
        Path path = null;
        Expression ex = null;
        Block blockTrue = null;
        Block blockFalse = null;
        Call call = null;

        if(ctx.assignment() != null) {
            option = Option.ASSIGNMENT;
            path = Path.recognize(contextFunction, scope, ctx.assignment().path());
            ex = Expression.recognize(contextFunction, scope, ctx.assignment().ex());;

            if(
                contextFunction.contextClass != null
                && path.path.get(0).name.equals("this")
                && path.path.size() == 1
            ) {
                Recover.semantic("cannot reassign 'this' variable");
            }

            Type lType = path.type;
            Type rType = ex.type;
            if(lType != rType) {
                if(!(lType instanceof Class) || !(rType instanceof Class)) {
                    Recover.type(contextFunction.name + ": assignment primitive type mismatch");
                }

                Class lClass = (Class) lType;
                Class rClass = (Class) rType;
                if(!(rClass.isSubclassOf(lClass))) {
                    Recover.type(
                        contextFunction.name + ": class " + rClass.name + 
                        " cannot be assigned as " + lClass.name
                    );
                } else {
                    // Explicit cast
                    ex = new Expression(lClass, ex);
                }
            }
        } else if(ctx.conditional() != null) {
            option = Option.CONDITIONAL;
            ex = Expression.recognize(contextFunction, scope, ctx.conditional().ex());
            blockTrue = Block.recognize(contextFunction, scope, ctx.conditional().block().get(0));
            blockFalse = Block.recognize(contextFunction, scope, ctx.conditional().block().get(1));

            if(ex.type == Type.STRING) {
                Recover.type(
                    contextFunction.name + ": condition cannot be string"
                );
            }
        } else if(ctx.iteration() != null) {
            option = Option.ITERATION;
            ex = Expression.recognize(contextFunction, scope, ctx.iteration().ex());
            blockTrue = Block.recognize(contextFunction, scope, ctx.iteration().block());

            if(ex.type == Type.STRING) {
                Recover.type(
                    contextFunction.name + ": condition cannot be string"
                );
            }
        } else if(ctx.call() != null) {
            option = Option.CALL;
            call = Call.recognize(contextFunction, scope, ctx.call());
        } else if(ctx.returnStatement() != null) {
            option = Option.RETURN;
            if(ctx.returnStatement().ex() != null) {
                ex = Expression.recognize(contextFunction, scope, ctx.returnStatement().ex());
            }

            if(ex == null) {
                if(contextFunction.type != Type.VOID) {
                    Recover.type(contextFunction.name + ": return statement cannot be empty here");
                }
            } else {
                Type f = contextFunction.type;
                Type e = ex.type;
                if(f != e) {
                    if(!(f instanceof Class) || !(e instanceof Class)) {
                        Recover.type(contextFunction.name + ": return type mismatch");
                    } else if(!(((Class) e).isSubclassOf((Class) f))) {
                        Recover.type(contextFunction.name + ": return type mismatch");
                    }
                }
            }
        } else {
            assert false;
        }

        // Assemble and return
        s.contextFunction = contextFunction;
        s.option = option;
        s.ex = ex;
        s.path = path;
        s.blockTrue = blockTrue;
        s.blockFalse = blockFalse;
        s.call = call;
        list.add(s);
        return list;
    }

    /** Parse a block of statements. */
    public static List<Statement> recognize(
        Function contextFunction, SymbolTable<Variable> scope,
        GrammarParser.BlockContext ctx
    ) {
        List<Statement> list = new ArrayList<>();
        ctx.statement().forEach(
            sctx -> list.addAll(Statement.recognize(contextFunction, scope, sctx))
        );
        return list;
    }

    /* ************************************************************************/

    /** Indexate the statement. */
    public void indexate() {
        if(declared != null) {
            declared.indexate(contextFunction.nextVariableIndex());
            return;
        }
        
        if(blockTrue != null) {
            blockTrue.indexate();
            if(blockFalse != null) {
                blockFalse.indexate();
            }
        }
    }

    /* ************************************************************************/

    /** Generate the code. */
    public void code() {
        if(option == Option.DECLARATION) {
            // Set default value
            if(declared.type == Type.STRING) {
            	Code.println("SET " + declared.code() + " \"\"");
            } else {
            	Code.println("SET " + declared.code() + " 0");
            }
            return;
        }


        if(option == Option.ASSIGNMENT) {
            // Push RHS onto stack
            ex.code(contextFunction);

            if(path.path.size() == 1) {
                // Simple-path assignment
                Code.pop(path.path.get(0));
                return;
            }

            // Evaluate composite path first
            List<Variable> vars = path.path;

            // Store handle address to $R
            Code.println("SET $R " + vars.get(0).code());

            // Store to $R addresses of attributes (up to the last one)
            for(int i = 1; i < vars.size()-1; i++) {
                Code.println("GETWORD $R $R " + (vars.get(i).index+1));
            }

            // Set the last attribute
            Code.println("SETWORD $R " + (vars.get(vars.size()-1).index+1) + " [$SP]");
            Code.println("SUBI $SP $SP 1");
            return;
        }


        if(option == Option.CONDITIONAL) {
            String labelElse = contextFunction.newLabel();
            String labelEndIf = contextFunction.newLabel();
            
            // Evaluate condition to $R
            ex.code(contextFunction);
            Code.pop("$R");

            // Branch
            Code.println("JUMPZ " + labelElse + " $R");
            blockTrue.code();
            Code.println("JUMP " + labelEndIf);
            Code.label(labelElse);
            blockFalse.code();
            Code.label(labelEndIf);

            return;
        }

        if(option == Option.ITERATION) {
            String labelWhile = contextFunction.newLabel();
            String labelEndWhile = contextFunction.newLabel();

            // Loop
            Code.label(labelWhile);

            // Evaluate condition to $R
            ex.code(contextFunction);
            Code.pop("$R");
            Code.println("JUMPZ " + labelEndWhile + " $R");
            blockTrue.code();
            Code.println("JUMP " + labelWhile);
            Code.label(labelEndWhile);

            return;
        }

        if(option == Option.CALL) {
            call.code();
            return;
        }

        if(option == Option.RETURN) {
            if(ex != null) {
                ex.code(contextFunction);
                Code.pop("$RET");
            }
            Code.returnVoid();
            return;
        }
    }
}