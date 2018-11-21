package main;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.Collection;

public class SymbolTable<T> {

	public Map<String,T> symbols;
	
	public SymbolTable() {
		symbols = new HashMap<>();
	}

	public Set<String> names() {
		return symbols.keySet();
	}
	
	public Collection<T> values() {
		return symbols.values();
	}

	public boolean isDefined(String name) {
		return symbols.containsKey(name);
	}

	public void assertExistence(String name) {
		if(!isDefined(name)) {
            Recover.semantic("missing definition of symbol " + name);
        }
	}

	public void assertNonExistence(String name) {
		if(isDefined(name)) {
            Recover.semantic("redefinition of symbol " + name);
        }
	}

	public void register(String name, T t) {
		assertNonExistence(name);
		symbols.put(name, t);
	}

	public void remove(String name) {
		symbols.remove(name);
	}

	public T lookUp(String name) {
		assertExistence(name);
		return symbols.get(name);
	}
	
	/**************************************************/

	public static SymbolTable<Class> classes;
	public static SymbolTable<Function> functions;

	public static void initialize() {
		classes = new SymbolTable<>();
		functions = new SymbolTable<>();
	}
	
	public static Collection<Class> classes() {
		return classes.values();
	}

	public static Collection<Function> functions() {
		return functions.values();
	}

	public static void registerClass(Class c) {
		classes.register(c.name, c);
	}

	public static void registerFunction(Function f) {
		functions.register(f.name, f);
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
            Recover.semantic("missing definition of symbol " + name);
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