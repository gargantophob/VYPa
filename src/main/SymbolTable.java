/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.Collection;

/**
 * Symbol table of named entities.
 */
public class SymbolTable<T extends Named> {

	/** name |-> object mapping. */
	protected Map<String,T> symbols;
	
	/** Initialize a symbol table. */
	public SymbolTable() {
		symbols = new HashMap<>();
	}
	
	/** Retreive set of values. */
	public Collection<T> values() {
		return symbols.values();
	}

	/** @return true if the symbol is defined */
	public boolean isDefined(String name) {
		return symbols.containsKey(name);
	}

	/** Assert existence of a symbol. */
	public void assertExistence(String name) {
		if(!isDefined(name)) {
            Recover.semantic("missing definition of symbol " + name);
        }
	}

	/** Assert non-existence of a symbol. */
	public void assertNonExistence(String name) {
		if(isDefined(name)) {
            Recover.semantic("redefinition of symbol " + name);
        }
	}

	/** Register new symbol. */
	public void register(T t) {
		assertNonExistence(t.name());
		symbols.put(t.name(), t);
	}

	/** Look up a symbol. */
	public T lookUp(String name) {
		assertExistence(name);
		return symbols.get(name);
	}
	
	/**************************************************/

	/** Parent scope for recursive search. */
	public SymbolTable<T> parentScope;

	/** Create a scope with a parent scope. */
	public SymbolTable(SymbolTable<T> parentScope) {
		this();
		this.parentScope = parentScope;
	}

	/** @return true if a symbol is defined here or in the parents. */
	public boolean isDefinedRecursively(String name) {
		if(isDefined(name)) {
			return true;
		} else if(parentScope != null) {
			return parentScope.isDefinedRecursively(name);
		} else {
			return false;
		}
	}

	/** Assert existence of a symbol here or in the parents. */
	public void assertExistenceRecursively(String name) {
		if(!isDefinedRecursively(name)) {
            Recover.semantic("missing definition of symbol " + name);
        }
	}

	/** Look up a symbol here or in the parents. */
	public T lookUpRecursively(String name) {
		assertExistenceRecursively(name);
		if(isDefined(name)) {
			return lookUp(name);
		} else {
			return parentScope.lookUpRecursively(name);
		}
	}
}