/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

/**
 * Class definition.
 */
public class Class extends Type implements Named {
    
    /** Table of classes. */
    public static SymbolTable<Class> table;
    
    /** Parser context. */
    private GrammarParser.ClassDefinitionContext ctx;
    /** Class name. */
    public String name;
    
    @Override
    public String name() {
        return name;
    }

    /** Parse function definition context. */
    public static Class recognize(GrammarParser.ClassDefinitionContext ctx) {
        Class c = new Class();
        c.ctx = ctx;
        c.name = ctx.name().get(0).getText();
        return c;
    }

    /* ************************************************************************/
   
    /** Class base. */
    public Class base;

    /** Look up base in the table of classes. */
    public void collectBase() {
        // Skip classes without context
        if(ctx == null) {
            base = null;
            return;
        }
        String baseName = ctx.name().get(1).getText();
        base = Class.table.lookUp(baseName);
    }

    /**
     * @return true if {@code this} is a valid subtype of {@code superclass}
     */
    public boolean isSubclassOf(Class superclass) {
        if(superclass == this) {
            return true;
        } else if(base != null) {
            return base.isSubclassOf(superclass);
        } else {
            return false;
        }
    }

    /** Assert that bases are not subclasses of this class. */
    public void checkBase() {
        if(base != null && base.isSubclassOf(this)) {
            Recover.semantic("recursive definition of class " + this.name);
        }
    }

    /* ********************************************************************** */
    
    /** True if the member headers were processed. */
    public boolean membersCollected = false;
    /** Table of instance variables. */
    public SymbolTable<Variable> attributes;
    /** Table of methods. */
    public SymbolTable<Function> methods;

    /** Process class definition context and collect attributes and methods. */
    public void collectMembers() {
        if(membersCollected) {
            return;
        }

        boolean isObject = name.equals("Object");

        // Process base first
        if(base != null) {
            base.collectMembers();
        }

        // Collect attributes
        attributes = new SymbolTable<>();
        if(isObject) {
            attributes.register(new Variable(Type.STRING, "@name"));
        } else {
            ctx.variableDeclaration().forEach(vctx -> 
                Variable.recognize(vctx).forEach(v -> attributes.register(v))
            );
        }

        // Attribute cannot have the same name as some parent field
        if(base != null) {
            attributes.values().forEach(v -> {
                if(base.isDefinedAttribute(v.name) || base.isDefinedMethod(v.name)) {
                    Recover.semantic(this.name + ": redefinition of attribute " + v.name);
                }
            });
        }

        // Collect methods
        methods = new SymbolTable<>();
        if(isObject) {
            Function f;

            f = new Function();
            f.type = Type.STRING;
            f.name = "toString";
            f.parameters = new ArrayList<>();
            methods.register(f);

            f = new Function();
            f.type = Type.STRING;
            f.name = "getClass";
            f.parameters = new ArrayList<>();
            methods.register(f);
        } else {
            ctx.functionDefinition().forEach(fctx ->
                methods.register(Function.recognize(fctx))
            );
        }

        // Initialize methods
        methods.values().forEach(f -> {
            f.contextClass = this;
            f.initialize();
        });
        
        // Collect constructor
        if(methods.isDefined(name)) {
            // Explicit constructor: check signature
            Function f = methods.lookUp(name);
            if(f.type != Type.VOID || f.parameters.size() != 1) {
                Recover.type(this.name + ": bad constructor signature");
            }
        } else {
            // Implicit constructor
            Function f = new Function();
            f.type = Type.VOID;
            f.name = this.name;
            f.parameters = new ArrayList<>();
            f.contextClass = this;
            f.initialize();
            methods.register(f);
        }

        // Methods cannot have the same name as some attribute
        methods.values().forEach(f -> {
            if(attributes.isDefined(f.name)) {
                Recover.semantic(this.name + ": redefinition of symbol " + f.name);
            }
        });

        // Can override only methods with the same signature
        if(base != null) {
            methods.values().forEach(f -> {
                if(base.isDefinedMethod(f.name)) {
                    Function o = base.lookUpMethod(f.name);
                    List<Type> signature = new ArrayList<>(f.signature);
                    signature.set(0, o.contextClass);
                    if(!o.signatureMatchExact(signature)) {
                        Recover.semantic(this.name + ": overriding method with a different signature");
                    }
                }
            });
        }
        
        // Success
        membersCollected = true;
    }

    /** @return true if the atribute is defined here or in the base */
    public boolean isDefinedAttribute(String name) {
        if(attributes.isDefined(name)) {
            return true;
        } else if(base != null) {
            return base.isDefinedAttribute(name);
        } else {
            return false;
        }
    }

    /** Look attribute up here or in the bases. */
    public Variable lookUpAttribute(String name) {
        if(!isDefinedAttribute(name)) {
            Recover.semantic(this.name + ": attribute " + name + " was not defined");
        }
        if(attributes.isDefined(name)) {
            return attributes.lookUp(name);
        } else {
            return base.lookUpAttribute(name);
        }
    }

    /** @return true if the method is defined here or in the bases */
    public boolean isDefinedMethod(String name) {
        if(methods.isDefined(name)) {
            return true;
        } else if(base != null) {
            return base.isDefinedMethod(name);
        } else {
            return false;
        }
    }

    /** Look method up here or in the bases. */
    public Function lookUpMethod(String name) {
        if(!isDefinedMethod(name)) {
            Recover.semantic(this.name + ": method " + name + " was not defined");
        }
        if(methods.isDefined(name)) {
            return methods.lookUp(name);
        } else {
            return base.lookUpMethod(name);
        }
    }

    /** Look up overriden method in the bases. */
    public Function lookUpSuperMethod(String name) {
        if(!methods.isDefined(name)) {
            Recover.semantic(this.name + ": method " + name + " is not being overridden");
        }
        if(base == null) {
            Recover.semantic(this.name + ": method " + name + " cannot be 'super'");
        }
        return base.lookUpMethod(name);
    }


    /* ********************************************************************** */

    public void collectBody() {
        methods.values().forEach(f -> f.collectBody());
    }

    /* ********************************************************************** */

    /** Establish topological order. */
    public void order(List<Class> list) {
        if(list.contains(this)) {
            return;
        }
        if(base != null) {
            base.order(list);
        }
        list.add(this);
    }

    /** Global class index. */
    public int index;
    /** Current attribute index, inherited from base. */
    public int attributeIndex;
    /** Current method index, inherited from base. */
    public int methodIndex;

    /** Indexate the class. */
    public void indexate(int index) {
        this.index = index;
        
        // Attributes
        attributeIndex = base == null ? 0 : base.attributeIndex;
        attributes.values().forEach(v -> {
            v.indexate(attributeIndex);
            attributeIndex++;
        });

        // Methods
        methodIndex = base == null ? 0 : base.methodIndex;
        methods.values().forEach(f -> {
            if(base != null && base.isDefinedMethod(f.name)) {
                f.indexate(base.lookUpMethod(f.name).index);
            } else {
                f.indexate(methodIndex++);
            }
        });
    }

    /** Look up method according to the index. */
    public Function lookUpMethod(int index) {
        assert index < methodIndex;
        for(Function f: methods.values()) {
            if(f.index == index) {
                return f;
            }
        }
        return base.lookUpMethod(index);
    }

    /** Create a list of all methods for this class. */
    public List<Function> VMT() {
        List<Function> list = new ArrayList<>();
        for(int i = 0; i < methodIndex; i++) {
            list.add(lookUpMethod(i));
        }
        return list;
    }
}