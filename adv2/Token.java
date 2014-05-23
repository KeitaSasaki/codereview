package calc;

/**
 * @author a13596
 * トークンを表すクラス
 * トークンの型と、生の文字列を持つ
 */
public class Token {
	public static enum Type {
		NUMBER, 	// 数字
		OPPAREN, 	// 開き丸括弧「(」
		CLPAREN, 	// 閉じ丸括弧「(」 
		FACTOP,  	// 「*」, 「/」
		TERMOP 		// 「+」, 「-」
	};
	
	private Type type;
	private String elem;
	
	public Token(String elem){
		if(elem == null || elem.isEmpty()){
			throw new IllegalArgumentException("不明なトークンです。： " + elem);
		}
		char fstChar = elem.charAt(0);
		if('0' <= fstChar && fstChar <= '9'){
			for (int i = 0; i < elem.length(); i++) {
				char c = elem.charAt(i);
				if(c < '0' || '9' < c){
					throw new CalcFormatException("不正な値：For input string: \"" + c + "\"");
				}
			}
			type = Type.NUMBER;
		} else{
			switch(elem){
			case "(":
				type = Type.OPPAREN;
				break;
			case ")":
				type = Type.CLPAREN;
				break;
			case "*":
			case "/":
				type = Type.FACTOP;
				break;
			case "+":
			case "-":
				type = Type.TERMOP;
				break;
			default:
				throw new IllegalArgumentException("不正な値：For input string: \"" + elem + "\"");
			}
		}
		this.elem = elem;
	}
	
	public Type getType(){
		return type;
	}
	
	@Override
	public String toString(){
		return elem;
	}
}
