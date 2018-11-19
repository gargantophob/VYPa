package main;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

public class ClassTable {
	
	public static Map<String, Class> classes;

	public static void initialize() {
		classes = new HashMap<>();		
		register(Class.defaultClassObject());
	}

	public static boolean isDefined(String name) {
		return classes.containsKey(name);
	}

	public static void assertExistence(String name) {
		if(!isDefined(name)) {
            Recover.exit(3, "class " + name + " was not defined");
        }
	}

	public static void assertNonExistence(String name) {
		if(isDefined(name)) {
            Recover.exit(3, "class " + name + " was already defined");
        }
	}

	public static Class lookUp(String name) {
		assertExistence(name);
		return classes.get(name);
	}
	
	public static void register(Class c) {
		assertNonExistence(c.name);
		classes.put(c.name, c);
	}

	public static void lookUpBase() {
		classes.values().forEach(c -> c.lookUpBase());
	}

	public static void collectDefinitions() {
		classes.values().forEach(c -> c.collectDefinitions());
	}

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