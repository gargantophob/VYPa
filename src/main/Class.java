package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Class extends Type {
    
    public String name;
    public String baseName;
    public Class base;

    public List<GrammarParser.VariableDeclarationContext> attributeDefinitions;
    public List<GrammarParser.FunctionDefinitionContext> methodDefinitions;
    public List<Variable> attributeList;
    public List<Function> methodList;

    public Class() {
        // Assemble Object class
        name = "Object";
        baseName = null;
        base = null;
        attributeDefinitions = null;
        methodDefinitions = null;
        
        attributeList = new ArrayList<>();
        attributeList.add(new Variable(Type.STRING, "@name"));
        
        methodList = new ArrayList<>();
        methodList.add(new Function(Type.STRING, "toString", null, this));
        methodList.add(new Function(Type.STRING, "getClass", null, this));
    }

    public Class(GrammarParser.ClassDefinitionContext ctx) {
        name = ctx.name().get(0).getText();
        baseName = ctx.name().get(1).getText();
        base = null;
        attributeDefinitions = ctx.variableDeclaration();
        methodDefinitions = ctx.functionDefinition();
    }

    /*****************************/
    
    public void collectBase() {
        if(baseName != null) {
            base = SymbolTable.classes.lookUp(baseName);
        }
    }

    public boolean isSubclassOf(Class superclass) {
        if(superclass == this) {
            return true;
        } else if(base != null) {
            return base.isSubclassOf(superclass);
        } else {
            return false;
        }
    }

    public void checkBase() {
        if(base != null && base.isSubclassOf(this)) {
            Recover.semantic(this.name + ": recursive definition of a class");
        }
    }

    /*****************************/

    public SymbolTable<Variable> attributes;
    public Function constructor;
    public SymbolTable<Function> methods;
    public Variable self;

    public void collectMembers() {

        // Collect attributes
        if(attributeDefinitions != null) {
            attributeList = new ArrayList<>();
            attributeDefinitions.forEach(ctx -> 
                Variable.recognize(ctx).forEach(v -> attributeList.add(v))
            );
        }
        attributes = new SymbolTable<>();
        attributeList.forEach(v -> attributes.register(v.name, v));

        // Collect methods
        if(methodDefinitions != null) {
            methodList = new ArrayList<>();
            methodDefinitions.forEach(ctx -> 
                methodList.add(new Function(ctx, this))
            );
        }
        methods = new SymbolTable<>();
        methodList.forEach(f -> methods.register(f.name, f));

        // Collect constructor
        if(methods.isDefined(name)) {
            // Explicit constructor
            constructor = methods.lookUp(name);
            if(!constructor.signatureMatch(Type.VOID, new ArrayList<>())) {
                Recover.type(this.name + ": bad constructor signature");
            }
            methods.remove(name);
        } else {
            // Implicit constructor
            constructor = new Function(Type.VOID, name, null, this);
        }

    }

    public void checkMembers() {
        // Two fields cannot have the same name
        if(!java.util.Collections.disjoint(attributes.names(), methods.names())) {
            Recover.semantic(this.name + ": redefinition of a field");
        }

        // Attribute cannot have the same name as some parent field
        if(base != null) {
            attributes.names().forEach(name -> {
                if(base.isDefinedAttribute(name) || base.isDefinedMethod(name)) {
                    Recover.semantic(this.name + ": redefinition of attribute " + name);
                }
            });
        }
        
        // Can override only methods with the same signature
        if(base != null) {
            methods.names().forEach(name -> {
                if(base.isDefinedMethod(name)) {
                    Function a = methods.lookUp(name);
                    Function b = base.lookUpMethod(name);
                    if(!a.signatureMatch(b.type, b.signature)) {
                        Recover.semantic(this.name + ": overriding method with different signature");
                    }
                }
            });
        }
    }

    public boolean isDefinedAttribute(String name) {
        if(attributes.isDefined(name)) {
            return true;
        } else if(base != null) {
            return base.isDefinedAttribute(name);
        } else {
            return false;
        }
    }

    public boolean isDefinedMethod(String name) {
        if(methods.isDefined(name)) {
            return true;
        } else if(base != null) {
            return base.isDefinedMethod(name);
        } else {
            return false;
        }
    }

    public Variable lookUpAttribute(String name) {
        if(!isDefinedAttribute(name)) {
            Recover.semantic(this.name + ": attribute " + name + " was not defined");
        }
        if(attributes.isDefined(name)) {
            return attributes.lookUp(name);
        } else {
            return base.lookUpAttribute(name);
        }
    }

    public Function lookUpMethod(String name) {
        if(!isDefinedMethod(name)) {
            Recover.semantic(this.name + ": method " + name + " was not defined");
        }
        if(methods.isDefined(name)) {
            return methods.lookUp(name);
        } else {
            return base.lookUpMethod(name);
        }
    }

    public Function lookUpSuperMethod(String name) {
        if(!methods.isDefined(name)) {
            Recover.semantic(this.name + ": method " + name + " is not being overridden");
        }
        if(base == null) {
            Recover.semantic(this.name + ": method " + name + " cannot be 'super'");
        }
        return base.lookUpMethod(name);
    }    

    /*****************************/

    public void collectBody() {
        constructor.collectBody();
        methods.values().forEach(f -> f.collectBody());
    }
}