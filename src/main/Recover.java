package main;

public class Recover {
    private static void exit(int code, String msg) {
        if(msg != null) {
            System.err.println(msg);
        }
        System.exit(code);
    }

    public static void success() {
        exit(0, null);
    }

    public static void lexical(String msg) {
        exit(1, msg);
    }

    public static void syntactic(String msg) {
        exit(2, msg);
    }

    public static void type(String msg) {
        exit(3, msg);
    }

    public static void semantic(String msg) {
        exit(4, msg);
    }

    public static void code(String msg) {
        exit(5, msg);
    }

    public static void internal(String msg) {
        exit(9, msg);
    }

    /******************/

    public static void here() {
        System.err.println("HERE");
    }
    
    public static void warn(String msg) {
        System.err.println("Something went wrong: " + msg);
    }    

    public static void notImplemented() {
        exit(42, "not implemented yet");
    }

    
}