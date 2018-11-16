package main;

public class Recover {
	public static void exit() {
        System.exit(0);
    }
    
    public static void exit(int code, String msg) {
        System.err.println(msg);
        System.exit(code);
    }
}