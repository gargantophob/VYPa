package main;

import java.util.List;
import java.util.ArrayList;

public interface Scope {
	public boolean isDefinedVariableHere(String name);
	public boolean isDefinedVariable(String name);
	public boolean isDefinedFunction(String name);
	public boolean isDefinedClass(String name);
	public boolean isDefinedSymbol(String name);
}