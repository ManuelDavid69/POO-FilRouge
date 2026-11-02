package model.cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires de la classe {@link Hand} (Atelier 1).
 */
public class HandTest {

    private Card twoOfDiamonds;
    private Card kingOfDiamonds;
    private Card twoOfSpades;
    private Hand hand;

    @Before
    public void setUp() {
        twoOfDiamonds = new Card(Rank._2, Suit.CARREAU);
        kingOfDiamonds = new Card(Rank._ROI, Suit.CARREAU);
        twoOfSpades = new Card(Rank._2, Suit.PIQUE);
        twoOfDiamonds.reveale();
        kingOfDiamonds.reveale();
        twoOfSpades.reveale();
        hand = new Hand();
    }

    @Test
    public void newHandShouldBeEmpty() {
        assertTrue(hand.isEmpty());
        assertEquals(0, hand.size());
        assertEquals("[[]]", hand.toString());
    }

    @Test
    public void addCardShouldAppendAndIgnoreNull() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);
        hand.addCard(null);

        assertEquals(2, hand.size());
        assertFalse(hand.isEmpty());
        assertEquals("[[2-Carreau, Roi-Carreau]]", hand.toString());
    }

    @Test
    public void secondConstructorShouldCopyTheCollection() {
        Hand copy = new Hand(Arrays.asList(twoOfDiamonds, kingOfDiamonds));

        assertEquals(2, copy.size());
        assertEquals("[[2-Carreau, Roi-Carreau]]", copy.toString());
    }

    @Test
    public void removeTopCardShouldRemoveTheFirstCard() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);

        assertSame(twoOfDiamonds, hand.removeTopCard());
        assertEquals("[[Roi-Carreau]]", hand.toString());
    }

    @Test
    public void removeTopCardOnEmptyHandShouldReturnNull() {
        assertNull(hand.removeTopCard());
        assertTrue(hand.isEmpty());
    }

    @Test
    public void removeCardShouldCheckBounds() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);

        assertSame(kingOfDiamonds, hand.removeCard(1));
        assertNull(hand.removeCard(-1));
        assertNull(hand.removeCard(6));
        assertEquals(1, hand.size());
    }

    @Test
    public void clearShouldRemoveEveryCard() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);

        hand.clear();

        assertTrue(hand.isEmpty());
        assertEquals(0, hand.size());
    }

    @Test
    public void playCardShouldRevealAndRemoveTheCard() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);

        Card played = hand.playCard(0);

        assertSame(twoOfDiamonds, played);
        assertTrue(played.isRevealed());
        assertEquals("[[Roi-Carreau]]", hand.toString());
    }

    @Test
    public void playCardWithInvalidIndexShouldReturnNull() {
        hand.addCard(twoOfDiamonds);

        assertNull(hand.playCard(6));
        assertNull(hand.playCard(-1));
        assertEquals(1, hand.size());
    }

    @Test
    public void revealeAndHideShouldOnlyChangeTheTargetedCard() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);
        hand.hideCard(0);
        hand.hideCard(1);

        assertTrue(hand.revealeCard(0));
        assertTrue(twoOfDiamonds.isRevealed());
        assertFalse(kingOfDiamonds.isRevealed());
        assertEquals("[[2-Carreau, ?-?]]", hand.toString());

        assertTrue(hand.hideCard(0));
        assertFalse(twoOfDiamonds.isRevealed());
        assertEquals("[[?-?, ?-?]]", hand.toString());
    }

    @Test
    public void revealeAndHideWithInvalidIndexShouldReturnFalse() {
        hand.addCard(twoOfDiamonds);

        assertFalse(hand.revealeCard(6));
        assertFalse(hand.hideCard(-1));
    }

    @Test
    public void iteratorShouldBrowseEveryCard() {
        hand.addCard(twoOfDiamonds);
        hand.addCard(kingOfDiamonds);
        hand.addCard(twoOfSpades);

        int count = 0;
        for (Card card : hand) {
            assertTrue(card == twoOfDiamonds || card == kingOfDiamonds || card == twoOfSpades);
            count++;
        }
        assertEquals(3, count);
    }
}
