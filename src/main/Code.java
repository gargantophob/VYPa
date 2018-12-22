/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import java.util.List;
import java.util.ArrayList;

/**
 * Target code generator.
 */
public class Code {
    
    /** String builder containing target code. */
    private static StringBuilder sb;
    
    /** Reset code generation. */
    public static void reset() {
        sb = new StringBuilder();
    }

    /** Print a string. */
    public static void print(String str) {
        sb.append(str);
    }

    /** Break the line. */
    public static void newline() {
        sb.append("\n");
    }

    /** Print a string and break the line. */
    public static void println(String str) {
        print(str);
        newline();
    }

    /** Print comment. */
    public static void comment(String str) {
    	println("# " + str);
    }

    /** Print separator.*/
    public static void separator() {
        comment("--------------------------------------------");
    }

    /** Print 3 separators.*/
    public static void separator3() {
        separator();
        separator();
        separator();
    }

    /** Create a label. */
    public static void label(String label) {
        println("LABEL " + label);
    }

    /** Push entity onto stack. */
    public static void push(String src) {
        println("ADDI $SP $SP 1");
        println("SET [$SP] " + src);
    }

    /** Push variable value onto stack. */
    public static void push(Variable src) {
        push(src.code());
    }

    /** Pop to entity. */
    public static void pop(String dst) {
        println("SET " + dst + " [$SP]");
        println("SUBI $SP $SP 1");
    }
    
    /** Pop to local variable. */
    public static void pop(Variable dst) {
        pop(dst.code());
    }

    /** Return. */
    public static void returnVoid() {
        println("SET $SP $FP");
        println("RETURN [$FP]");
    }

    /** Return constructed code. */
    public static String code() {
        return sb.toString();
    }
}