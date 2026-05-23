package model.cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests unitaires de la classe {@link Deck} (Atelier 2).
 */
public class DeckTest {

    @Test
    public void deck32ShouldContainThirtyTwoCardsStartingAtSeven() {
        Deck deck = new Deck(32);

        assertEquals(32, deck.size());
        assertFalse(deck.isEmpty());
        assertEquals(new Card(Rank._7, Suit.CARREAU), deck.removeTopCard());
        assertEquals(31, deck.size());
    }

    @Test
    public void deck52ShouldContainEveryCard() {
        Deck deck = new Deck(52);

        assertEquals(52, deck.size());
        assertEquals(new Card(Rank._2, Suit.CARREAU), deck.removeTopCard());
    }

    @Test
    public void deck32ShouldNotContainRanksBelowSeven() {
        Deck deck = new Deck(32);

        for (Card card : deck) {
            assertTrue("Le rang " + card.getRank() + " ne doit pas figurer dans un jeu de 32 cartes",
                    card.getRank().getRank() >= 7);
        }
    }

    @Test
    public void invalidDeckSizeShouldBeRejected() {
        try {
            new Deck(40);
            fail("Un Deck de 40 cartes devrait être refusé");
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    @Test
    public void deckShouldBeSortableAndReturnItsHighestCard() {
        Deck deck = new Deck(32);

        assertEquals(new Card(Rank._AS, Suit.CARREAU), deck.max());

        deck.sort();
        assertEquals(new Card(Rank._7, Suit.CARREAU), deck.removeTopCard());
        assertEquals(new Card(Rank._AS, Suit.TREFLE), deck.removeCard(deck.size() - 1));
    }

    @Test
    public void copyConstructorsShouldAcceptAnyCardsCollection() {
        Deck source = new Deck(32);

        Hand hand = new Hand(source);
        Board board = new Board(source);
        Deck copy = new Deck(source);

        assertEquals(32, hand.size());
        assertEquals(32, board.size());
        assertEquals(32, copy.size());
        assertEquals(board.toString(), hand.toString());
        assertEquals(board.toString(), copy.toString());
    }

    @Test
    public void shuffleShouldKeepEveryCard() {
        Deck deck = new Deck(32);
        Deck reference = new Deck(32);
        List<Card> expected = new ArrayList<Card>();
        for (Card card : reference) {
            expected.add(card);
        }

        deck.shuffle();

        assertEquals(32, deck.size());
        for (Card card : deck) {
            assertTrue("Carte inattendue ou perdue au mélange : " + card, expected.remove(card));
        }
        assertTrue(expected.isEmpty());
    }
}
