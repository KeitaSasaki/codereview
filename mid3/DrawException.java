package intermediate;

public class DrawException extends Exception{
	public DrawException (String str) {
		super("too large!: " + str);
	}
}
