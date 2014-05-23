package poker;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * ポーカーを行うクラス
 * カードをHAND_NUM枚引いた後交換を行い、役判定を行って表示を行う
 * */
public class Poker {
	
	private final static int HAND_NUM = 5;	// 手札の枚数
	private final static int NOT_STRAIGHT = -1;	// ストレートで無かった場合に返す数字
	private final static int STARTNUM_OF_ROYAL_SF = 9;	// ロイヤルストレートだった場合の開始の数字

	/**
	 * 与えられた手札がフラッシュかどうか判定
	 * @return フラッシュならtrue
	 * */
	public static boolean isFlush(Card[] hand){
		for(int i = 0; i < hand.length - 1; i++){
			//手札のi番目がi+1番目のスートと同じならフラッシュ
			if(hand[i].getSuit() != hand[i + 1].getSuit()){
				return false;
			}
		}
		return true;
	}
	/**
	 * 与えられた手札の数字の分布がロイヤルストレートフラッシュかどうか判定
	 * @param dist 手札の数字の分布
	 * @return ロイヤルストレートフラッシュならtrue、違えばfalseを返す
	 * */
	public static boolean isRoyalStraightFlash(int[] dist){
		//START_NUM_OF_ROYAL_SFからHAND_NUM枚全部1ならtrue
		for (int i = 0; i < HAND_NUM; i++) {
			if(dist[(STARTNUM_OF_ROYAL_SF + i) % Deck.MAX_TRUMP_NUM] != 1){
				return false;
			}
		}
		return true;
	}

	/**
	 * 与えられた手札の数字の分布がストレートかどうか判定
	 * ロイヤルストレートフラッシュはNOT_STRAIGHTを返す
	 * @param dist 手札の数字の分布
	 * @return ストレートならストレートの開始番号を返し、違えばNOT_STRAIGHTを返す
	 * */
	public static int getStartStraightNo(int[] dist){
		//i番目の数字からHAND_NUM枚を調べる
		for(int i = 0; i <= Deck.MAX_TRUMP_NUM - HAND_NUM; i++){
			//分布が1である数字があったら
			if(dist[i] == 1){
				//HAND_NUMつの数字が全て1枚ずつ並んでいればストレート
				for(int j = 1; j < HAND_NUM; j++){
					if(dist[i + j] != 1){
						//そこから1つでも1じゃない分布があったらストレートはあり得ない
						return NOT_STRAIGHT;
					}
				}
				return i;
			}
		}
		return NOT_STRAIGHT;
	}
	
	/**
	 * 与えられた手札の数字の分布がNカードかどうか判定
	 * @return Nカードならtrue
	 * @param n NカードのN
	 * @param dist 手札の数字の分布*/
	public static boolean isNOfAKind(int n, int[] dist){
		for (int i : dist) {
			if(i == n) return true;
		}
		return false;
	}
	
	/**
	 * 与えられた手札の数字の分布がNペアかどうか判定
	 * @return Nペアならtrue
	 * @param n NペアのN
	 * @param dist 手札の数字の分布*/
	public static boolean isNPair(int n, int[] dist){
		int ret = 0;
		for (int i : dist) {
			if(i == 2) ret++;
		}
		return ret == n;
	}
	
	/**
	 * 与えられた手札がポーカーのどの役か判定
	 * 対応する文字列を返す
	 * @param hand 手札
	 * @return 役に対応する文字列
	 * */
	public static String judgeHand(Card[] hand){
		String handStr = "ノーペア";
		//手札のトランプの数字の分布を調べる。例えばnums[0] = 2ならAが2枚
		int[] dist = new int[13];
		for (Card c : hand) {
			dist[c.getNum()-1]++;
		}
		boolean isFlush = isFlush(hand);
		int startStraightNo = getStartStraightNo(dist);
		
		if(isRoyalStraightFlash(dist)){
			handStr = "ロイヤルストレートフラッシュ";
		} else if(isFlush && startStraightNo != NOT_STRAIGHT){
			handStr = "ストレートフラッシュ";
		} else if(isNOfAKind(4, dist)){
			handStr = "フォーカード";
		} else if(isNOfAKind(3, dist) && isNOfAKind(2, dist)){
			handStr = "フルハウス";
		} else if(isFlush){
			handStr = "フラッシュ";
		} else if(startStraightNo != NOT_STRAIGHT){
			handStr = "ストレート";
		} else if(isNOfAKind(3, dist)){
			handStr = "スリーカード";
		} else if(isNPair(2, dist)){
			handStr = "ツーペア";
		} else if(isNPair(1, dist)){
			handStr = "ワンペア";
		}
		
		return handStr;
	}
	
	/**
	 * 入力文字列が規則に従っているか調べる
	 * 正しくなければコメントを出力
	 * @param input 調べる文字列
	 * @return 正しければtrue
	 */
	public static boolean isValidInput(String input){
		if(input.length() > HAND_NUM){
			System.out.printf("%d枚以上は交換できません。\n", HAND_NUM+1);
			return false;
		}
		
		try{
			int e = Integer.parseInt(input);
			if(e < 0){
				System.out.println("負数の入力です。");
				return false;
			}
		} catch(NumberFormatException e){
			System.out.println("入力は数値のみです。");
			return false;
		}
		
		boolean[] isExchange = new boolean[HAND_NUM];
		//重複チェック & '0'の文字が含まれていないかチェック
		for(int i = 0; i < input.length(); i++){
			char c = input.charAt(i);
			if(c == '0'){
				System.out.println("0番のカードはありません。");
				return false;
			} else if(HAND_NUM + '0' < c){
				System.out.println("交換出来る手札は1〜5です。" + c + "は交換出来ません。");
				return false;
			} else{
				int ex = input.charAt(i) - '1';
				if(isExchange[ex]){
					System.out.println("入力に重複があります。");
					return false;
				} else{
					isExchange[ex]= true; 
				}
			}
		}
		
		return true;
	}
	
	/** * ポーカーを行うメソッド * */
	public static void pokerGame(){
		//多分ジョーカーはなしでいいはず・・・
		Deck dk = new Deck(0);
		Card[] hand = null;
		//まず初期手札を作る
		try {
			hand = dk.draw(HAND_NUM);
		} catch (DrawException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("交換するカードの番号を入力してください(例：135)。");
		System.out.println("0を入力するとカードを交換しません。");
		for (int i = 0; i < hand.length; i++) {
			System.out.printf("%d:%s\n", i+1, hand[i].toString());
		}
		
		Scanner sc = new Scanner(System.in);
		String input = null;
		boolean isStay = false;
		//正しい入力が与えられるまで入力を繰り返す
		while(true){
			try {
				input = sc.nextLine();
			} catch (NoSuchElementException e) {
				System.err.println("入力行が見つかりません");
				return;
			} catch (IllegalStateException e){
				System.err.println("スキャナがクローズしています");
				return;
			}
			//交換しない
			if(input.equals("0")){
				isStay = true;
				break;
			} else if(isValidInput(input)){ //正しい入力か判定
				break;
			}
		}

		//i枚めの札を交換する場合はtrue、しない場合はfalse
		boolean[] isExchange = new boolean[HAND_NUM];
		
		//手札を交換する場合
		if(!isStay){
			for(int i = 0; i < input.length(); i++){
				int ex = input.charAt(i) - '1';
				isExchange[ex]= true; 
			}
		}

		//交換後の手札を作る
		for(int i = 0; i < isExchange.length; i++){
			if(isExchange[i]){	// 交換する場合
				try {
					hand[i] = dk.draw();	// 1枚引く
				} catch (DrawException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < hand.length; i++) {
			System.out.printf("%s\n", hand[i].toString());
		}
		
		System.out.printf("役は %s でした。\n", judgeHand(hand));
	}

	public static void main(String[] args) {
		pokerGame();
	}

}
