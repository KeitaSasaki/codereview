package poker;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import poker.Card.Suit;

/**
 * デッキクラス
 * トランプのカードの山札を持ち、カードを引く事が出来る
 * デッキ作成の際にジョーカーの枚数を非負整数で指定することが出来る
 * */
public class Deck {
	public static final int SUM_WITHOUT_JOKER = 52;	// JOKER以外の総数
	public static final int MAX_TRUMP_NUM = 13;	// トランプの最大の数
	public static final int SUIT_NUM = 4;	// スートの数
	
	private LinkedList<Card> cards;	// 山札

	public Deck(int jokers){
		
		if(jokers < 0){
			throw new IllegalArgumentException("引数[" + jokers + "]が不正です");
		}
		
		cards = new LinkedList<Card>();
		Suit[] suit = Suit.values();
		//クラブからスペードまでのトランプを入れる
		for(int i = 0; i < SUIT_NUM; i++){
			for(int j = 1; j <= MAX_TRUMP_NUM; j++){
				cards.add(new Card(suit[i], j));
			}
		}
		//JOKERを入れる
		for(int i = 0; i < jokers; i++){
			cards.add(new Card(Suit.JOKER, 0));
		}
		
		//ランダムな順番でカードを保持する
		Collections.shuffle(cards);
	}
	
	/** 現在のデッキの残りをすべて表示する (for DEBUG)*/
	public void printDeck(){
		for(Card c: cards){
			System.out.printf("(%s, %d)\n", c.getSuit().name(), c.getNum());
		}
	}
	
	/** デッキの残り枚数を返す  */
	public int getRestCardsNum(){
		return cards.size();
	}
	
	/**
	 * 山からn枚カードを引く関数
	 * @param n 引く枚数
	 * @throws DrawException 
	 * */
	public Card[] draw(int n) throws DrawException{
		
		if(getRestCardsNum() < n) throw new DrawException("山にカードが足りません！");
		
		Card[] res = new Card[n];
		//n枚のカードを返す
		for(int i = 0; i < n; i++){
			res[i] = cards.poll();
		}
		
		return res;
	}
	
	/**
	 * 山から1枚カードを引く関数
	 * @throws DrawException 
	 * */
	public Card draw() throws DrawException{	
		return draw(1)[0];
	}
	
	/** デッキをソートする  */
	public void sort(){
		Collections.sort(cards);
	}
	
	/**
	 * デッキに残っているジョーカーの総数を返す (for JUnit Test)
	 * @return ジョーカーの枚数 */
	public int countJokers(){
		int jSum = 0;
		for (Card c: cards) {
			if(c.getSuit() == Suit.JOKER){
				jSum++;
			}
		}
		return jSum;
	}
}
