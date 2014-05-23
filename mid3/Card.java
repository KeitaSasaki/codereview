package intermediate;

public class Card implements Comparable<Card> {
	public static enum Suit {CLUB, DIA, HEART, SPADE, JOKER};
	private Suit suit;	// スート
	private int num;	// トランプの数字

	public Card(Suit suit, int num){
		this.suit = suit;
		this.num = num;
	}

	@Override
	public int compareTo(Card o) {
		int slf = this.suit.ordinal();
		int obj = o.suit.ordinal();
		//スートが同じなら数字を見る
		return slf == obj ? this.num - o.num : slf - obj;
	}

	public Suit getSuit(){
		return suit;
	}

	public int getNum(){
		return num;
	}
}
