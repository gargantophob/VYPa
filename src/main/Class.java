package main;

import java.util.List;
import java.util.ArrayList;

public class Class {
    public parsed.Class prototype;

    public String name;
    public Class base;

    /*public List<Variable> attributes;
    public List<Function> methods;*/

    public Scope scope;

    public Class(String name, Class base, List<Variable> attributes, List<Function> methods) {
        prototype = null;

        this.name = name;
        this.base = base;

        scope = new Scope(base.scope);
        scope.variableInitialize();
        scope.functionInitialize();
        
        attributes.forEach(v -> scope.variableRegister(v));
        methods.forEach(f -> scope.functionRegister(f));
    }

    public Class(parsed.Class prototype) {
        this.prototype = prototype;
        name = prototype.name;
        base = null;
        attributes = null;
        methods = null;
    }

    public void lookUpBase() {
        if(prototype == null) {
            return;
        }
        if(name.equals(prototype.baseName)) {
            Recover.exit(3, "class " + name + " has itself as its base");
        }
        base = ClassTable.lookUp(prototype.baseName);
    }

    public void collectDefinitions() {
        if(prototype == null) {
            return;
        }

        attributes = new VariableTable(null);
        prototype.attributes.forEach(a -> attributes.register(new Variable(a)));

        // TODO collect methods
        methods = new FunctionTable();
        prototype.methods.forEach(m -> methods.register(new Function(m, this)));
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " : " + baseClass + ". ");

        sb.append("Attributes: ");
        attributes.forEach(p -> sb.append(p + ", "));

        sb.append("Methods: ");
        methods.forEach(m -> sb.append(m + ", "));

        return sb.toString();
    }*/
}