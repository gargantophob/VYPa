package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class SymbolTable<T> {

	// public static SymbolTable<Literal> literals;
	public static SymbolTable<Class> classes;
	public static SymbolTable<Function> functions;

	public static void initialize() {
		// literals = new SymbolTable<>();
		classes = new SymbolTable<>();
		functions = new SymbolTable<>();
	}

	public static void collectClassNames(List<parsed.Class> classesList) {
		Class classObject = new Class("Object");
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
		// built-in functions
		Function g;
		
		g = new Function(Type.STRING, "print", new ArrayList<>(), new ArrayList<>(), null);
		functions.register(g.name, g);
		
		g = new Function(Type.INT, "readInt", new ArrayList<>(), new ArrayList<>(), null);
		functions.register(g.name, g);
		
		g = new Function(Type.STRING, "readString", new ArrayList<>(), new ArrayList<>(), null);
		functions.register(g.name, g);
		
		List<Variable> lengthArguments = new ArrayList<>();
		lengthArguments.add(new Variable(Type.STRING, "s"));
		g = new Function(Type.INT, "length", lengthArguments, new ArrayList<>(), null);
		functions.register(g.name, g);

		List<Variable> subStrArguments = new ArrayList<>();
		subStrArguments.add(new Variable(Type.STRING, "s"));
		subStrArguments.add(new Variable(Type.INT, "i"));
		subStrArguments.add(new Variable(Type.INT, "n"));
		g = new Function(Type.STRING, "subStr", subStrArguments, new ArrayList<>(), null);
		functions.register(g.name, g);

		functionsList.forEach(parsed -> {
			Function f = new Function(parsed, null);
			functions.register(f.name, f);
		});

		Function main = functions.lookUp("main");
		if(!main.signatureMatch(Type.VOID, new ArrayList<>())) {
			Recover.exit(3, "bad signature of function " + main.name);
		}
	}

	public static void collectBodies() {
		functions.symbols.values().forEach(f -> f.collectBody());
		classes.symbols.values().forEach(c -> c.collectBody());
	}

	public static void inferType() {
		functions.symbols.values().forEach(f -> f.inferType());
		classes.symbols.values().forEach(c -> c.inferType());
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

	public void remove(String name) {
		symbols.remove(name);
	}

	/**************************************************/

	public SymbolTable<T> parentScope;
	
	public SymbolTable(SymbolTable<T> parentScope) {
		this();
		this.parentScope = parentScope;
	}

	public boolean isDefinedRecursively(String name) {
		if(isDefined(name)) {
			return true;
		} else if(parentScope != null) {
			return parentScope.isDefinedRecursively(name);
		} else {
			return false;
		}
	}

	public void assertExistenceRecursively(String name) {
		if(!isDefinedRecursively(name)) {
            Recover.exit(3, "missing definition of symbol " + name);
        }
	}

	public T lookUpRecursively(String name) {
		assertExistenceRecursively(name);
		if(isDefined(name)) {
			return lookUp(name);
		} else {
			return parentScope.lookUpRecursively(name);
		}
	}
}