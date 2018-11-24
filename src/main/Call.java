package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Call  {

    public Function context;
    public SymbolTable<Variable> scope;
    
    public Expression contextObject;
    public String name;
    public List<Expression> arguments;
    
    public Function function;
    public Type type;

    protected Call(
        Function context, SymbolTable<Variable> scope,
        boolean isSuper, Expression contextObject, String name,
        List<GrammarParser.ExContext> arguments
    ) {
        this.context = context;
        this.scope = scope;
        this.contextObject = contextObject;
        this.name = name;
        
        if(isSuper) {
            Class contextClass = context.context;
            if(contextClass == null) {
                Recover.semantic(context.name + ": 'super' keyword in a global function");
            }
            function = contextClass.lookUpSuperMethod(name);
        } else if(contextObject != null) {
            if(!(contextObject.type instanceof Class)) {
                Recover.type(context.name + ": coexpression is not of object type");
            }
            function = ((Class) contextObject.type).lookUpMethod(name);
        } else {
            function = SymbolTable.functions.lookUp(name);
        }
                
        type = function.type;

        this.arguments = new ArrayList<>();
        arguments.forEach(e -> this.arguments.add(Expression.recognize(context, scope, e)));

        List<Type> signature = new ArrayList<>();
        this.arguments.forEach(e -> signature.add(e.type));
        if(!function.signatureMatch(type, signature)) {
            Recover.type(context.name + ": invalid function call");
        }
    }

    public static Call recognize(
        Function context, SymbolTable<Variable> scope, GrammarParser.CallContext ctx
    ) {
        String name = ctx.name().getText();
        List<GrammarParser.ExContext> arguments = ctx.arguments().ex();
        
        boolean isSuper = false;
        Expression contextObject = null;
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.ex() != null) {
                contextObject = Expression.recognize(context, scope, ctx.ex());
            } else {
                isSuper = true;
            }
        }

        return new Call(context, scope, isSuper, contextObject, name, arguments);
    }

    public static Call recognize(
        Function context, SymbolTable<Variable> scope, GrammarParser.ExContext ctx
    ) {
        String name = ctx.name().getText();
        List<GrammarParser.ExContext> arguments = ctx.arguments().ex();
        
        boolean isSuper = false;
        Expression contextObject = null;
        if(ctx.getChild(1).getText().equals(".")) {
            if(ctx.ex().size() > 0) {
                contextObject = Expression.recognize(context, scope, ctx.ex().get(0));
            } else {
                isSuper = true;
            }
        }

        return new Call(context, scope, isSuper, contextObject, name, arguments);
    }    
}