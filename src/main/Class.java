package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;

public class Class extends Type {
    
    public parsed.Class prototype;

    public String name;
    public Class base;
    
    public SymbolTable<Variable> attributes;
    public SymbolTable<Function> methods;

    public Function constructor;
    
    public Class(String name) {
        
        // Object
        prototype = null;
        this.name = name;
        base = null;
        attributes = new SymbolTable<>();
        methods = new SymbolTable<>();
        constructor = implicitConstructor();

        List<Statement> toStringBoby = new ArrayList<>();
        // TODO toString body
        Function toString = new Function(
            Type.STRING, "toString", new ArrayList<>(), toStringBoby, this
        );

        List<Statement> getClassBoby = new ArrayList<>();
        // TODO getClass body
        Function getClass = new Function(
            Type.STRING, "getClass", new ArrayList<>(), toStringBoby, this
        );

        methods.register(toString.name, toString);
        methods.register(getClass.name, getClass);
    }

    public Class(parsed.Class prototype) {
        this.prototype = prototype;
        name = prototype.name;
    }

    public void collectBase() {
        if(prototype == null) {
            // Object
            return;
        }
        
        base = SymbolTable.classes.lookUp(prototype.baseName);

        // base class exists: go up the class hierarchy and try to find yourself
        Class current = base;
        while(true) {
            if(this == current) {
                Recover.exit(3, "recursive definition of class " + name);
            }
            current = current.base;
            if(current == null) {
                break;
            }
        }
    }

    public void collectMembers() {
        if(prototype == null) {
            // Object
            return;
        }

        attributes = new SymbolTable<>();
        prototype.attributes.forEach(parsed -> {
            Variable v = new Variable(parsed);
            attributes.register(v.name, v);
        });

        methods = new SymbolTable<>();
        prototype.methods.forEach(parsed -> {
            Function f = new Function(parsed, this);
            methods.register(f.name, f);
        });

        // Check constructor
        if(methods.isDefined(name)) {
            constructor = methods.lookUp(name);
            if(!constructor.signatureMatch(Type.VOID, new ArrayList<>())) {
                Recover.exit(3, "bad constructor signature");
            }
            methods.remove(name);
        } else {
            // Create implicit constructor
            constructor = implicitConstructor();
        }

        // Two fields cannot have the same name
        Set<String> set1 = attributes.symbols.keySet();
        Set<String> set2 = methods.symbols.keySet();
        if(!java.util.Collections.disjoint(set1, set2)) {
            Recover.exit(3, "redefinition of symbol");
        }

        // Attribute cannot have the same name as some parent field
        if(base != null) {
            attributes.symbols.keySet().forEach(name -> {
                if(base.isDefinedField(name)) {
                    Recover.exit(3, "redefinition of symbol");
                }
            });
        }
        
        // Can override only methods with the same signature (override)
        if(base != null) {
            methods.symbols.keySet().forEach(name -> {
                if(base.isDefinedMethod(name)) {
                    Function a = methods.lookUp(name);
                    Function b = base.lookUpMethod(name);
                    if(!a.signatureMatch(b)) {
                        Recover.exit(3, "overriding function with different signature");
                    }
                }
            });
        }
    }

    public Function implicitConstructor() {
        List<Statement> body = new ArrayList<>();
        // TODO body
        return new Function(Type.VOID, name, new ArrayList<>(), body, this);
    }

    public void collectBody() {
        methods.symbols.values().forEach(f -> f.collectBody());
    }

    public void inferType() {
        methods.symbols.values().forEach(f -> f.inferType());
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

    public boolean isDefinedField(String name) {
        return isDefinedAttribute(name) || isDefinedMethod(name);
    }

    public Function lookUpMethod(String name) {
        if(!isDefinedMethod(name)) {
            Recover.exit(3, "method " + name + " was not defined");
        }
        if(methods.isDefined(name)) {
            return methods.lookUp(name);
        } else {
            return base.lookUpMethod(name);
        }
    }

    public Variable lookUpAttribute(String name) {
        if(!isDefinedAttribute(name)) {
            Recover.exit(3, "attribute " + name + " was not defined");
        }
        if(attributes.isDefined(name)) {
            return attributes.lookUp(name);
        } else {
            return base.lookUpAttribute(name);
        }
    }
}