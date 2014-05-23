package intermediate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class DeckTest {

	@Test
	public void デッキの残り枚数が正しい() throws DrawException{
		Deck deck = new Deck(0);
		assertThat(deck.getRestCardsNum(), is(52));
		deck.draw(10);
		assertThat(deck.getRestCardsNum(), is(42));
	}
	
	@Test(expected = DrawException.class)
	public void デッキの残り枚数より大きい数を引くと例外を投げる() throws DrawException{
		Deck deck = new Deck(0);	// 52枚
		deck.draw(53);
	}
	
	@Test
	public void ジョーカーの枚数がデッキ作成時に指定した枚数だけある(){
		Deck deck;
		int jSum;

		deck = new Deck(3);
		jSum = deck.countJokers();
		assertThat(jSum, is(3));

		deck = new Deck(0);
		jSum = deck.countJokers();
		assertThat(jSum, is(0));

		deck = new Deck(100);
		jSum = deck.countJokers();
		assertThat(jSum, is(100));

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void デッキ作成時のジョーカーの枚数が負だと例外を投げる(){
		new Deck(-1);
	}

}
