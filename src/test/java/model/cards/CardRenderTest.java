package model.cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests unitaires du proxy {@link CardRender} (Atelier 2).
 */
public class CardRenderTest {

    @Test
    public void renderShouldDelegateEveryGetterToTheCard() {
        Card card = new Card(Rank._ROI, Suit.CARREAU);
        card.reveale();
        CardRender render = new CardRender(card);

        assertEquals(Rank._ROI, render.getRank());
        assertEquals(Suit.CARREAU, render.getSuit());
        assertTrue(render.isRevealed());
        assertEquals("Roi-Carreau", render.toString());
    }

    @Test
    public void renderShouldFollowTheStateOfTheWrappedCard() {
        Card card = new Card(Rank._2, Suit.PIQUE);
        CardRender render = new CardRender(card);

        assertFalse(render.isRevealed());
        assertEquals("?-?", render.toString());

        card.reveale();
        assertTrue(render.isRevealed());
        assertEquals("2-Pique", render.toString());
    }

    @Test
    public void rendersOfTheSameCardShouldBeEqualAndCompareEqual() {
        Card card = new Card(Rank._ROI, Suit.CARREAU);
        CardRender first = new CardRender(card);
        CardRender second = new CardRender(card);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(0, first.compareTo(second));
    }

    @Test
    public void compareToShouldUseTheNaturalCardOrder() {
        CardRender king = new CardRender(new Card(Rank._ROI, Suit.CARREAU));
        CardRender two = new CardRender(new Card(Rank._2, Suit.CARREAU));

        assertTrue(king.compareTo(two) > 0);
        assertTrue(two.compareTo(king) < 0);
    }
}
