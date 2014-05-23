package intermediate;

import intermediate.Card.Suit;

import java.util.Collections;
import java.util.ArrayList;

public class Deck {
	public static final int SUM_WITHOUT_JOKER = 52;	// JOKER以外の総数
	public static final int MAX_TRUMP_NUM = 13;	// トランプの最大の数
	public static final int SUIT_NUM = 4;	// スートの数
	
	private ArrayList<Card> cards;	// 山札
	private int start, length;	// start:デッキの開始位置, length: デッキの全長

	public Deck(int jokers){
		
		if(jokers < 0){
			throw new IllegalArgumentException("引数[" + jokers + "]が不正です");
		}
		
		cards = new ArrayList<Card>(SUM_WITHOUT_JOKER + jokers);
		start = 0;	// デッキの開始位置（1枚引くと1増える）
		length = SUM_WITHOUT_JOKER + jokers;	// デッキの残り枚数
		Suit[] suit = Card.Suit.values();
		//クラブからスペードまでのトランプを入れる
		for(int i = 0; i < SUIT_NUM; i++){
			for(int j = 1; j <= MAX_TRUMP_NUM; j++){
				cards.add(new Card(suit[i], j));
			}
		}
		//JOKERを入れる
		for(int i = 0; i < jokers; i++){
			cards.add(new Card(Card.Suit.JOKER, 0));
		}
		
		//ランダムな順番でカードを保持する
		Collections.shuffle(cards);
	}
	
	/** 現在のデッキの残りをすべて表示する (for DEBUG)*/
	public void printDeck(){
		for(int i = start; i < length; i++){
			System.out.printf("[%d] -> (%s, %d)\n", i, cards.get(i).getSuit().name(), cards.get(i).getNum());
		}
	}
	
	/** デッキの残り枚数を返す  */
	public int getRestCardsNum(){
		return length - start;
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
			res[i] = cards.get(i + start);
		}
		start += n;
		
		return res;
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
		for (int i = start; i < length; i++) {
			if(cards.get(i).getSuit() == Card.Suit.JOKER){
				jSum++;
			}
		}
		return jSum;
	}
}
