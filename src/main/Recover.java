/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

/**
 * Recover procedures.
 */
public class Recover {
    /** Print {@code msg} (if not null) to standard error output. */
    private static void print(String msg) {
        if(msg != null) {
            System.err.println(msg);
        }
    }

    /** Exit with exit code {@code code} and print {@code msg}. */
    private static void exit(int code, String msg) {
        print(msg);
        System.exit(code);
    }

    /** Successful compilation. */
    public static void success() {
        exit(0, null);
    }

    /** Lexical error. */
    public static void lexical(String msg) {
        exit(1, msg);
    }

    /** Syntactic error. */
    public static void syntactic(String msg) {
        exit(2, msg);
    }

    /** Semantic error - type incompatibility. */
    public static void type(String msg) {
        exit(3, msg);
    }

    /** Semantic error - other. */
    public static void semantic(String msg) {
        exit(4, msg);
    }

    /** Code generation error. */
    public static void code(String msg) {
        exit(5, msg);
    }

    /** Internal error. */
    public static void internal(String msg) {
        exit(9, msg);
    }

    /* Auxiliary procedures. */

    public static void here() {
        print("HERE");
    }
    
    public static void warn(String msg) {
        print("Something went wrong: " + msg);
    }    

    public static void notImplemented() {
        exit(42, "not implemented yet");
    }
}