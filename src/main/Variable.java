package main;

public class Variable {
    
    public Type type;
    public String name;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
    }
    
    public Variable(parsed.Variable ref) {
        type = Type.recognize(ref.type);
        if(type == Type.VOID) {
            Recover.exit(3, "unexpected void type");
        }
        name = ref.name;
    }
}
