package intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

public class CardTest {

	@Test
	public void スートが同じカードの大小関係が正しい(){
		Card smallCard, largeCard;
		
		//クラブ
		smallCard = new Card(Card.Suit.CLUB, 1);
		largeCard = new Card(Card.Suit.CLUB, 13);
		assertThat(smallCard.compareTo(largeCard) < 0, is(true));
		
		//ダイア
		smallCard = new Card(Card.Suit.DIA, 2);
		largeCard = new Card(Card.Suit.DIA, 4);
		assertThat(smallCard.compareTo(largeCard) < 0, is(true));
		
		//ハート
		smallCard = new Card(Card.Suit.HEART, 12);
		largeCard = new Card(Card.Suit.HEART, 11);
		assertThat(smallCard.compareTo(largeCard) > 0, is(true));
		
		//スペード
		smallCard = new Card(Card.Suit.DIA, 6);
		largeCard = new Card(Card.Suit.DIA, 2);
		assertThat(smallCard.compareTo(largeCard) > 0, is(true));
	}
	
	@Test
	public void 比較の時にスートは数字よりも優先順位が高い(){
		//弱いスートの数字を高くする
		//クラブの4、ダイヤの3、ハートの2、スペードの1、ジョーカーを入れる
		Card club	=	new Card(Card.Suit.CLUB,	4);
		Card dia	=	new Card(Card.Suit.DIA,		3);
		Card heart	=	new Card(Card.Suit.HEART,	2);
		Card spade	=	new Card(Card.Suit.SPADE,	1);
		Card joker	=	new Card(Card.Suit.JOKER,	0);
		
		//上の並びの下の奴ほど強いはず
		assertThat(club.compareTo(dia) < 0,		is(true));
		assertThat(club.compareTo(heart) < 0,	is(true));
		assertThat(club.compareTo(spade) < 0,	is(true));
		assertThat(club.compareTo(joker) < 0,	is(true));
		
		assertThat(dia.compareTo(heart) < 0,	is(true));
		assertThat(dia.compareTo(spade) < 0,	is(true));
		assertThat(dia.compareTo(joker) < 0,	is(true));
		
		assertThat(heart.compareTo(spade) < 0,	is(true));
		assertThat(heart.compareTo(joker) < 0,	is(true));
		
		assertThat(spade.compareTo(joker) < 0,	is(true));
	}
}
