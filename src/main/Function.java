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
        if(contextClass != null && contextClass.name.equals(name)) {
            Variable self = parameters.get(0);

            // Call base constructor
            if(contextClass.base != null) {
                List<Variable> path = new ArrayList<>();
                path.add(self);
                Expression ex = new Expression(new Path(path));
                
                Statement s = new Statement();
                s.option = Statement.Option.CALL;
                Call call = new Call();
                call.contextFunction = this;
                call.scope = scope;
                call.coExpression = ex;
                call.name = contextClass.base.name;
                call.isSuper = false;
                call.arguments = new ArrayList<>();
                call.check();
                s.call = call;
                body.add(s);
            }

            // Initialize instance variables
            contextClass.attributes.values().forEach(v -> {
                Statement s = new Statement();
                s.option = Statement.Option.ASSIGNMENT;
                List<Variable> path = new ArrayList<>();
                path.add(self);
                path.add(v);
                s.path = new Path(path);
                s.ex = new Expression(new Literal(v.type));
                body.add(s);
            });

        }

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

        // Indexate local variables with positive indices
        variableIndex = 0;
        body.forEach(s -> s.indexate());
    }

    /** Retrieve next variable index. */
    public int nextVariableIndex() {
        variableIndex++;
        return variableIndex;
    }

    /* ************************************************************************/

    /** Get function label. */
    public String label() {
        String label = "";
        if(contextClass != null) {
            label = contextClass.name;
        }
        label = label + "::" + name;
        return label;
    }

    /** Unique label identifier. */
    public int labelIdentifier;

    /** Generate new unique label. */
    public String newLabel() {
        return label() + ":" + labelIdentifier++;
    }

    /** Generate function code. */
    public void code() {
        labelIdentifier = 0;
     
         // Skip ::print, ::readInt, ::readString which are handled manually
         if(
            this == Function.table.lookUp("print")
            || this == Function.table.lookUp("readInt")
            || this == Function.table.lookUp("readString")
            ) {
             return;
         }

        Code.separator();
        Code.comment("Function " + label() + ":");
        Code.label(label());
        Code.newline();

        // Set frame pointer
        Code.println("SET $FP $SP");

        Code.comment("Allocate local variables");
        Code.println("ADDI $SP $SP " + variableIndex);
        Code.newline();

        // Generate code for built-in global functions
        if(this == Function.table.lookUp("length")) {
            // ::length
            Code.println("GETSIZE $RET " + scope.lookUp("s").code());
            Code.returnVoid();
            return;
        }
        if(this == Function.table.lookUp("subStr")) {
            // $1 = length(s);
            Code.println("GETSIZE $1 [$FP-1]");

            // if(i < 0 || i == $1 || i > $1 || n < 0) {return "";}
            Code.println("LTI $2 [$FP-2] 0");
            Code.println("EQI $3 [$FP-2] $1");
            Code.println("GTI $4 [$FP-2] $1");
            Code.println("LTI $5 [$FP-3] 0");
            Code.println("OR $2 $2 $3");
            Code.println("OR $2 $2 $4");
            Code.println("OR $2 $2 $5");
            String returnLabel = newLabel();
            Code.println("JUMPNZ " + returnLabel + " $2");

            // $2 = i + n - $1
            Code.println("ADDI $2 [$FP-2] [$FP-3]");
            Code.println("SUBI $2 $2 $1");

            // if($2 > 0) {n = n - $2}
            Code.println("GTI $3 $2 0");
            String endIf = newLabel();
            Code.println("JUMPZ " + endIf + " $3");
            Code.println("SUBI $2 [$FP-3] $2");
            Code.println("SET [$FP-3] $2");
            Code.label(endIf);

            // $1 = alloc(n);
            // $2 = s;
            // $3 = 0;
            // while($3 < n) { $1[$3] = $2[i+$3]; $3 = $3 + 1;}
            Code.println("CREATE $1 [$FP-3]");
            Code.println("SET $2 [$FP-1]");
            Code.println("SET $3 0");

            String labelWhile = newLabel();
            String labelEndWhile = newLabel();
            Code.label(labelWhile);
            Code.println("LTI $4 $3 [$FP-3]");
            Code.println("JUMPZ " + labelEndWhile + " $4");
            Code.println("SET $4 [$FP-2]");
            Code.println("ADDI $4 $4 $3");
            Code.println("GETWORD $4 $2 $4");
            Code.println("SETWORD $1 $3 $4");
            Code.println("ADDI $3 $3 1");
            Code.println("JUMP " + labelWhile);
            Code.label(labelEndWhile);

            // return $1;
            Code.returnVoid();

            Code.label(returnLabel);
            // return empty string on error
            Code.println("SET $RET \"\"");
            Code.returnVoid();
            return;
        }

        if(this == Class.table.lookUp("Object").lookUpMethod("toString")) {
            // Object::toString - return chunk id
            Code.println("SET $R [$FP-1]");
            Code.println("INT2STRING $R $R");
            Code.println("SET $RET $R");
            Code.returnVoid();
            return;
        }

        if(this == Class.table.lookUp("Object").lookUpMethod("getClass")) {
            // Object::getClass - return "Object"
            Code.println("SET $RET \"Object\"");
            Code.returnVoid();
            return;
        }
        
        // User-defined function
        Code.comment("Statements:");
        for(int i = 0; i < body.size(); i++) {
            Code.comment(i + ":");
            body.get(i).code();
        }
        Code.newline();

        Code.comment("Implicit return");
        if(type == Type.VOID) {
        } else if(type == Type.STRING) {
            Code.println("SET $RET \"\"");
        } else {
            Code.println("SET $RET 0");
        }
        Code.returnVoid();
        Code.newline();

        Code.comment("End of function " + label());
        Code.newline();
    }
}