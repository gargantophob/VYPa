/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/** Function call. */
public class Call  {

    /** Context function. */
    public Function contextFunction;
    /** Current scope. */
    public SymbolTable<Variable> scope;
    
    /** Co-expression. */
    public Expression coExpression;
    /** Callee name. */
    public String name;
    /** Overridden call. */
    public boolean isSuper;

    /** List of arguments. */
    public List<Expression> arguments;
    
    /** Explicit callee. */
    public Function function;
    /** Inferred type. */
    public Type type;

    /** Recognize statement call. */
    public static Call recognize(
        Function contextFunction, SymbolTable<Variable> scope,
        GrammarParser.CallContext ctx
    ) {
        Call call = new Call();
        call.contextFunction = contextFunction;
        call.scope = scope;
        call.name = ctx.name().getText();
        call.isSuper = false;
        
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.ex() != null) {
                call.coExpression = Expression.recognize(contextFunction, scope, ctx.ex());
            } else {
                call.isSuper = true;
            }
        }

        call.arguments = new ArrayList<>();
        ctx.arguments().ex().forEach(ectx -> 
            call.arguments.add(Expression.recognize(contextFunction, scope, ectx))
        );

        call.check();
        return call;
    }

    /** Recognize expression call. */
    public static Call recognize(
        Function contextFunction, SymbolTable<Variable> scope, GrammarParser.ExContext ctx
    ) {
        Call call = new Call();
        call.contextFunction = contextFunction;
        call.scope = scope;
        call.name = ctx.name().getText();
        call.isSuper = false;
        
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.ex().size() > 0) {
                call.coExpression = Expression.recognize(contextFunction, scope, ctx.ex().get(0));
            } else {
                call.isSuper = true;
            }
        }

        call.arguments = new ArrayList<>();
        ctx.arguments().ex().forEach(ectx -> 
            call.arguments.add(Expression.recognize(contextFunction, scope, ectx))
        );
        
        call.check();
        return call;
    }

    /** Parse call context. */
    public void check() {
        // Detect function signature
        if(isSuper) {
            Class contextClass = contextFunction.contextClass;
            if(contextClass == null) {
                Recover.semantic(contextFunction.name + ": 'super' keyword in a global function");
            }
            function = contextClass.lookUpSuperMethod(name);
            
            // Add implicit self-parameter
            List<Variable> path = new ArrayList<>();
            path.add(contextFunction.parameters.get(0));
            arguments.add(0, new Expression(
                new Path(contextFunction, contextFunction.scope, path)
            ));
        } else if(coExpression != null) {
            if(!(coExpression.type instanceof Class)) {
                Recover.type(contextFunction.name + ": coexpression is not of object type");
            }
            function = ((Class) coExpression.type).lookUpMethod(name);
            
            // Add implicit self-parameter
            arguments.add(0, coExpression);
        } else {
            function = Function.table.lookUp(name);
        }
             
        // Infer the type   
        type = function.type;

        // Check signature
        List<Type> signature = new ArrayList<>();
        arguments.forEach(e -> signature.add(e.type));
        if(!function.signatureMatchSubtype(signature)) {
            Recover.type(contextFunction.name + ": invalid function call");
        }
    }
}