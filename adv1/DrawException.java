package poker;

/**
 * Deckクラスにおいて山札の残り枚数以上を引く際に投げられるエラー*/
public class DrawException extends Exception{

	private static final long serialVersionUID = 1L;

	public DrawException (String str) {
		super("too large!: " + str);
	}
}
