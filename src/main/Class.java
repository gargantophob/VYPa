package main;

import java.util.List;
import java.util.ArrayList;

public class Class {
    
    public parsed.Class prototype;

    public String name;
    
    public Scope scope;

    public Class(String name, Class base, List<Variable> attributes, List<Function> methods) {
        
        prototype = null;
        this.name = name;
        Scope parentScope = base == null ? null : base.scope;
        scope = new Scope(parentScope);
        scope.variableAllow();
        scope.functionAllow();
        
        attributes.forEach(v -> scope.variableRegister(v));
        methods.forEach(f -> scope.functionRegister(f));
    }

    public Class(parsed.Class prototype) {
        this.prototype = prototype;
        name = prototype.name;
        scope = null;
    }

    public void lookUpBase() {
        if(prototype == null) {
            return;
        }
        if(name.equals(prototype.baseName)) {
            Recover.exit(3, "class " + name + " has itself as its base");
        }
        scope = new Scope(ClassTable.lookUp(prototype.baseName).scope);
        scope.variableAllow();
        scope.functionAllow();

        scope.variableRegister(new Variable(new Type(Type.Option.OBJECT, this), "this"));
        if(prototype == null) {
            return;
        }
        prototype.attributes.forEach(v -> scope.variableRegister(new Variable(v)));
        prototype.methods.forEach(f -> scope.functionRegister(new Function(f, scope)));
    }

    public void processBody() {
        scope.functionProcessBody();
    }

    public static Class defaultClassObject() {
        String name = "Object";
        List<Function> methods = new ArrayList<>();

        // TODO string toString(void)
        
        // TODO string getClass(void)

        return new Class(name, null, new ArrayList<>(), methods);
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