package poker;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import poker.Card.Suit;

public class PokerTest {
	
	/** 標準出力をテストするためのフィールドとメソッド*/
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	
	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}
	
	
	@Test
	public void ストレートを正しく判定できる(){
		
		//5枚並んでたらストレート
		int a = Poker.getStartStraightNo(new int[]{0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0});
		assertThat(a, is(2));
		
		//4枚は駄目
		int b = Poker.getStartStraightNo(new int[]{0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0});
		assertThat(b, is(-1));
		
		//1個とびは駄目
		int c = Poker.getStartStraightNo(new int[]{0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0});
		assertThat(c, is(-1));
		
		//1をまたいではいけない
		int d = Poker.getStartStraightNo(new int[]{1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1});
		assertThat(d, is(-1));
	}
	
	@Test
	public void フラッシュを正しく判定できる(){
		Card[] hand = new Card[5];
		
		//ハートの2, 4, 6, 8, 10
		for(int i = 1; i <= 5; i++){
			hand[i-1]= new Card(Suit.HEART, i*2); 
		}
		
		//全部同じだったらフラッシュ
		boolean a = Poker.isFlush(hand);
		assertThat(a, is(true));
		
		//1個変えたら駄目
		hand[2] = new Card(Suit.DIA, 3);
		a = Poker.isFlush(hand);
		assertThat(a, is(false));
	}
	
	@Test
	public void ストレートフラッシュ判定(){
		Card[] hand = new Card[5];
		
		//10, J, Q, K, Aはロイヤルストレートフラッシュ
		for(int i = 9; i <= 13; i++){
			hand[i - 9]= new Card(Suit.HEART, i % 13 + 1); 
		}
		hand[0]= new Card(Suit.HEART, 10); 
		hand[1]= new Card(Suit.HEART, 11); 
		hand[2]= new Card(Suit.HEART, 12); 
		hand[3]= new Card(Suit.HEART, 13); 
		hand[4]= new Card(Suit.HEART, 1); 
		
		String a = Poker.judgeHand(hand);
		assertThat(a, is("ロイヤルストレートフラッシュ"));
		
		//9, 10, J, Q, Kはストレートフラッシュ
		hand[4] = new Card(Suit.HEART, 9);
		String b = Poker.judgeHand(hand);
		assertThat(b, is("ストレートフラッシュ"));
	}
	
	@Test
	public void その他の役判定(){
		Card[] hand = new Card[5];

		hand[0] = new Card(Suit.CLUB,		3);
		hand[1] = new Card(Suit.DIA,		3);
		hand[2] = new Card(Suit.HEART,		3);
		hand[3] = new Card(Suit.SPADE,		3);
		hand[4] = new Card(Suit.HEART,		4);
		
		//3, 3, 3, 3, 4
		String a = Poker.judgeHand(hand);
		assertThat(a, is("フォーカード"));
		
		hand[3] = new Card(Suit.CLUB, 4);
		//3, 3, 3, 4, 4
		String b = Poker.judgeHand(hand);
		assertThat(b, is("フルハウス"));
		
		hand[3] = new Card(Suit.SPADE, 5);
		//3, 3, 3, 4, 5
		String c = Poker.judgeHand(hand);
		assertThat(c, is("スリーカード"));
		
		hand[2] = new Card(Suit.CLUB, 4);
		//3, 3, 4, 4, 5
		String d = Poker.judgeHand(hand);
		assertThat(d, is("ツーペア"));
		
		hand[0] = new Card(Suit.HEART, 2);
		//2, 3, 4, 4, 5
		String e = Poker.judgeHand(hand);
		assertThat(e, is("ワンペア"));
	}
	
	@Test
	public void 入力長が6以上なら6枚以上は交換できませんと出力する(){
		Poker.isValidInput("aaaaaa");
		assertEquals("6枚以上は交換できません。\n", outContent.toString());
	}
	
	@Test
	public void 入力5文字目までに数字以外が含まれていたら入力は数値のみですと出力する(){
		Poker.isValidInput("1234a");
		assertEquals("入力は数値のみです。\n", outContent.toString());
	}
	
	@Test
	public void 入力5文字目までに0が含まれていたら0番のカードはありませんと出力する(){
		Poker.isValidInput("12340");
		assertEquals("0番のカードはありません。\n", outContent.toString());
	}
	
	@Test
	public void 入力に重複があったら入力に重複がありますと出力する(){
		Poker.isValidInput("11234");
		assertEquals("入力に重複があります。\n", outContent.toString());
	}
	
}
