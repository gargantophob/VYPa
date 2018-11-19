package main;

public class Variable {
    
    public Type type;
    public String name;
    
    /*public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
    }*/

    public Variable(parsed.Variable ref) {
        type = new Type(ref.type);
        type.assertNonVoid();
        name = ref.name;
    }
}
