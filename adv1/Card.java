package poker;

/**
 * Cardクラス
 * トランプのカードを表現するクラス
 * 各カードは数字とスートを持つ
 * */
public class Card implements Comparable<Card> {
	public static enum Suit {CLUB, DIA, HEART, SPADE, JOKER};
	private static final String[] toStr = {"クラブ", "ダイヤ", "ハート", "スペード", "ジョーカー"};
	private Suit suit;	// スート
	private int num;	// トランプの数字
	private String str;	// 対応する文字列

	public Card(Suit suit, int num){
		this.suit = suit;
		this.num = num;
		this.str = toStr[suit.ordinal()];
		if(suit != Suit.JOKER){
			this.str += "の" + num;
		}
	}

	@Override
	public int compareTo(Card o) {
		int slf = this.suit.ordinal();
		int obj = o.suit.ordinal();
		//スートが同じなら数字を見る
		return slf == obj ? this.num - o.num : slf - obj;
	}
	
	@Override
	public String toString(){
		return str;
	}

	public Suit getSuit(){
		return suit;
	}

	public int getNum(){
		return num;
	}
}
