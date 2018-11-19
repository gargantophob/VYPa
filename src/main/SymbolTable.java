package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class SymbolTable<T> {

	public static SymbolTable<Class> classes;
	public static SymbolTable<Function> functions;

	public static void initialize() {
		classes = new SymbolTable<>();
		functions = new SymbolTable<>();
	}

	public static void collectClassNames(List<parsed.Class> classesList) {
		Class classObject = new Class();
		classes.register(classObject.name, classObject);
		classesList.forEach(parsed -> {
			Class c = new Class(parsed);
			classes.register(c.name, c);
		});
	}

	public static void collectClassBases() {
		classes.symbols.values().forEach(c -> c.collectBase());
	}

	public static void collectClassMembers() {
		classes.symbols.values().forEach(c -> c.collectMembers());
	}
	
	public static void collectFunctionHeaders(List<parsed.Function> functionsList) {
		functionsList.forEach(parsed -> {
			Function f = new Function(parsed);
			functions.register(f.name, f);
		});

		// TODO existence of void main(void);
	}

	public static void collectBodies() {
		functions.symbols.values().forEach(f -> f.collectBody());
		classes.symbols.values().forEach(c -> c.collectBody());
	}

	public Map<String,T> symbols;
	
	public SymbolTable() {
		symbols = new HashMap<>();
	}
	
	public boolean isDefined(String name) {
		return symbols.containsKey(name);
	}

	public void assertExistence(String name) {
		if(!isDefined(name)) {
            Recover.exit(3, "missing definition of symbol " + name);
        }
	}

	public void assertNonExistence(String name) {
		if(isDefined(name)) {
            Recover.exit(3, "redefinition of symbol " + name);
        }
	}

	public T lookUp(String name) {
		assertExistence(name);
		return symbols.get(name);
	}
	
	public void register(String name, T t) {
		assertNonExistence(name);
		symbols.put(name, t);
	}

	/*public static void lookUpBase() {
		classes.values().forEach(c -> c.lookUpBase());
	}

	public static void processBody() {
		classes.values().forEach(c -> c.processBody());
	}*/

	// string toString(void);
    /*public static Function toStringCreate() {
        Type type = new Type(Type.TypeType.STRING, null);
        String name = "toString";
        List<Variable> parameters = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();
        statements.add(new Statement(new Expression(
            Expression.ExpressionType.STRING, null, null, null, 
            new Literal("TODO"), null, null, null
        )));
        StatementBlock body = new StatementBlock(statements);

        return new Function(type, name, parameters, body);
    }*/

    // string getClass(void);
    /*public static Function getClassCreate() {
        Type type = new Type(Type.TypeType.STRING, null);
        String name = "getClass";
        List<Variable> parameters = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();
        statements.add(new Statement(new Expression(
            Expression.ExpressionType.STRING, null, null, null, 
            new Literal("Object"), null, null, null
        )));
        StatementBlock body = new StatementBlock(statements);

        return new Function(type, name, parameters, body);
    }*/
}