package main;

import java.util.List;
import java.util.ArrayList;

public class Function {
    public Class context;
    
    public Type type;
    public String name;
    // public VariableTable variables;
    public List<Variable> parameters;
    public StatementBlock body;

    public Function(parsed.Function ref, Class context) {
        type = new Type(ref.type);
        name = ref.name;
        this.context = context;

        VariableTable parentScope = context == null ? null : context.attributes;
        variables = new VariableTable(parentScope);
        parameters = new ArrayList<>();
        ref.parameters.forEach(p -> {
            Variable v = new Variable(p);
            variables.register(v);
            parameters.add(v);
        });
        
        // TODO body
    }

    /*public Function(
        Type type, String name, List<Variable> parameters, StatementBlock body
    ) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }*/

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type + " " + name + " (");
        parameters.forEach(p -> sb.append(p + ", "));
        sb.append(") { " + body.toString() + "}");
        return sb.toString();
    }*/

    /*public void assertObject() {
        if(type.type == Type.TypeType.OBJECT) {
            Recover.notImplemented();
        }
    }

    public void assertDefineteness(Scope parentScope) {
        this.parentScope = parentScope;
        parameterNames = new ArrayList<>();
        for(Variable v: parameters) {
            v.assertVoid();
            v.assertObject();
            v.assertDefineteness(this);
            parameterNames.add(v.name);
        }
        body.assertDefineteness(this);
    }

    @Override
    public boolean isDefinedVariableHere(String name) {
        return parameterNames.contains(name);
    }

    @Override
    public boolean isDefinedVariable(String name) {
        return isDefinedVariableHere(name);
    }

    @Override
    public boolean isDefinedFunction(String name) {
        return parentScope.isDefinedFunction(name);
    }

    @Override
    public boolean isDefinedClass(String name) {
        return parentScope.isDefinedClass(name);
    }*/
}