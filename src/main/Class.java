package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;

public class Class {
    
    public parsed.Class prototype;

    public String name;
    public Class base;
    
    public SymbolTable<Variable> attributes;
    public SymbolTable<Function> methods;
    
    public Class() {
        
        // Object
        prototype = null;
        name = "Object";
        base = null;
        attributes = new SymbolTable<>();
        methods = new SymbolTable<>();

        // TODO string toString(void)
        // TODO string getClass(void)
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
        if(this == base) {
            Recover.exit(3, "class " + name + " has itself as its base");
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
            Function f = new Function(parsed);
            methods.register(f.name, f);
        });

        // Two fields cannot have the same name
        Set<String> set1 = attributes.symbols.keySet();
        Set<String> set2 = methods.symbols.keySet();
        if(!java.util.Collections.disjoint(set1, set2)) {
            Recover.exit(3, "redefinition of symbol");
        }

        // Attribute cannot have the same as some parent field
        if(base != null) {
            attributes.symbols.keySet().forEach(name -> {
                if(base.isDefinedField(name)) {
                    Recover.exit(3, "redefinition of symbol");
                }
            });
        }
        
        // TODO can redefine method with the same signature (override)
    }

    public void collectBody() {
        methods.symbols.values().forEach(f -> f.collectBody());
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
}