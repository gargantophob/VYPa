package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class Scope {

	public Scope parentScope;

	public Map<String, Variable> variables;
	
	public Map<String, Function> functions;

	public Map<String, Literal> literals;

	public Scope(Scope parentScope) {
		this.parentScope = parentScope;
		variables = null;
		functions = null;
		
	}
	
	public void variableAllow() {
		variables = new HashMap<>();
	}

	public void functionAllow() {
		functions = new HashMap<>();
	}

	public void literalAllow() {
		literals = new HashMap<>();
	}

	public boolean variableIsDefinedHere(String name) {
		if(variables == null) {
			return false;
		}
		return variables.containsKey(name);
	}

	public boolean variableIsDefined(String name) {
		if(variableIsDefinedHere(name)) {
			return true;
		} else if(parentScope != null) {
			return parentScope.variableIsDefined(name);
		} else {
			return false;
		}
	}

	public void variableAssertExistence(String name) {
		if(!variableIsDefined(name)) {
            Recover.exit(3, "variable " + name + " was not defined");
        }
	}

	public void variableAssertNonExistenceHere(String name) {
		if(variableIsDefinedHere(name)) {
            Recover.exit(3, "redefinition of variable " + name);
        }
	}

	public Variable variableLookUp(String name) {
		variableAssertExistence(name);
		Variable v = null;
		if(variables != null) {
			v = variables.get(name);
		}
		if(v == null) {
			v = parentScope.variableLookUp(name);
		}
		return v;
	}
	
	public void variableRegister(Variable v) {
		if(variables == null) {
			Recover.warn("registering variable to nowhere");
			return;
		}
		String name = v.name;
		ClassTable.assertNonExistence(name);
		variableAssertNonExistenceHere(name);
		functionAssertNonExistenceHere(name);
		variables.put(name, v);
	}


	public boolean functionIsDefinedHere(String name) {
		if(functions == null) {
			return false;
		}
		return functions.containsKey(name);
	}

	public boolean functionIsDefined(String name) {
		if(functionIsDefinedHere(name)) {
			return true;
		} else if(parentScope != null) {
			return parentScope.functionIsDefined(name);
		} else {
			return false;
		}
	}

	public void functionAssertExistence(String name) {
		if(!functionIsDefined(name)) {
            Recover.exit(3, "function " + name + " was not defined");
        }
	}

	public void functionAssertNonExistenceHere(String name) {
		if(functionIsDefinedHere(name)) {
            Recover.exit(3, "function " + name + " was already defined");
        }
	}
	
	public Function functionLookUp(String name) {
		functionAssertExistence(name);
		Function f = null;
		if(functions != null) {
			f = functions.get(name);
		}
		if(f == null) {
			f = parentScope.functionLookUp(name);
		}
		return f;
	}

	public void functionRegister(Function f) {
		if(functions == null) {
			Recover.warn("registering variable to nowhere");
			return;
		}
		String name = f.name;
		variableAssertNonExistenceHere(name);
		functionAssertNonExistenceHere(name);
		functions.put(name, f);
	}
}