/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/**
 * Function definition.
 */
public class Function implements Named {

    /** Table of global functions. */
    public static SymbolTable<Function> table;

    /** Parser context. */
    private GrammarParser.FunctionDefinitionContext ctx;
    /** Return type. */
    public Type type;
    /** Function name. */
    public String name;
    /**
     * List of formal parameters in the declaration order (will include
     * self-parameter).
     */
    public List<Variable> parameters;
    
    @Override
    public String name() {
        return name;
    }

    /** Parse function definition context. */
    public static Function recognize(
        GrammarParser.FunctionDefinitionContext ctx
    ) {
        Function f = new Function();
        f.ctx = ctx;
        f.name = ctx.name().getText();
        f.type = Type.recognize(ctx.type());
        f.parameters = new ArrayList<>();
        ctx.paramList().formalParameter().forEach(
            vctx -> f.parameters.add(Variable.recognize(vctx))
        );
        return f;
    }

    /* ************************************************************************/

    /** Context class. */
    public Class contextClass;
    /** Scope of local variables. */
    public SymbolTable<Variable> scope;
    /** Precompiled signature for fast comparison. */
    public List<Type> signature;

    /** Process function defition. */
    public void initialize() {
        // Check names of parameters
        for(Variable v: parameters) {
            Class.table.assertNonExistence(v.name);
            Function.table.assertNonExistence(v.name);
        }

        // Add self-parameter
        if(contextClass != null) {
            parameters.add(0, new Variable(contextClass, "this"));
        }

        // Compile the scope and the signature
        scope = new SymbolTable<>();
        signature = new ArrayList<>();
        for(Variable v: parameters) {
            scope.register(v);
            signature.add(v.type);
        }
    }

    /** @return true on the exact match of formal parameter types */
    public boolean signatureMatchExact(List<Type> signature) {
        if(this == Function.table.lookUp("print")) {
            // can print any primitive type
            for(Type arg: signature) {
                if(arg instanceof Class) {
                    return false;
                }
            }
            return true;
        }

        return this.signature.equals(signature);
    }

    /** @return true on the signature match (modulo subtypes) */
    public boolean signatureMatchSubtype(List<Type> signature) {
        // Special case: ::print
        if(this == Function.table.lookUp("print")) {
            return signatureMatchExact(signature);
        }

        // Check number of parameters
        if(this.signature.size() != signature.size()) {
            return false;
        }

        // Check each parameter
        for(int i = 0; i < signature.size(); i++) {
            Type requiredType = this.signature.get(i);
            Type actualType = signature.get(i);
            if(requiredType == actualType) {
                continue;
            }
            if(!(actualType instanceof Class) || !(requiredType instanceof Class)) {
                return false;
            }
            if(!((Class) actualType).isSubclassOf((Class) requiredType)) {
                return false;
            }
        }

        // Success
        return true;
    }

    /* ************************************************************************/
    
    /** Body statements. */
    public List<Statement> body;

    /** Collect function body. */
    public void collectBody() {
        body = new ArrayList<>();

        // Create implicit commands for constructors
        /*if(contextClass != null && name == contextClass.name) {
            Variable self = parameters.get(0);

            // Call base constructor
            if(base != null) {
                List<Expression> arguments = new ArrayList<>();
                arguments.add(new Expression(f,s,new Path(f,s,self,null)));
                f.body.add(new Statement(f, s, new Call(
                    f, s, base.lookUpMethod(base.name), arguments
                )));
            }

            // Initialize instance variables
            attributes.values().forEach(v -> {
                List<Variable> path = new ArrayList<>();
                path.add(v);
                Expression e = new Expression(f,s,new Literal(v.type, "", 0));
                f.body.add(new Statement(f, s, new Path(f,s,self,path), e));
            });
        }*/

        // Parse statement contexts
        if(ctx == null) {
            return;
        }
        body.addAll(Statement.recognize(this, scope, ctx.block()));
    }

    /* ************************************************************************/

    /** Function index. */
    public int index;
    /** Variable index. */
    public int variableIndex;
    
    /** Indexate the function. */
    public void indexate(int index) {
        this.index = index;

        // Indexate formal parameters with negative indices
        variableIndex = -1;
        for(Variable v: parameters) {
            v.indexate(variableIndex--);
        }

        // Indexate local variable with positive indices
        variableIndex = 0;
        body.forEach(s -> s.indexate());
    }

    /** Retrieve next variable index. */
    public int nextVariableIndex() {
        variableIndex++;
        return variableIndex;
    }
}