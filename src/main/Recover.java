package main;

public class Recover {

	public static void warn(String msg) {
		System.err.println("Something went wrong:" + msg);
	}

    public static void notImplemented() {
        exit(42, "not implemented yet");
    }

	public static void exit() {
        System.exit(0);
    }
    
    public static void exit(int code, String msg) {
        System.err.println(msg);
        System.exit(code);
    }
}