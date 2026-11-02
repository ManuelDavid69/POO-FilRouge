package model.cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires du comportement commun aux collections de cartes,
 * exercé ici à travers {@link Board} (Atelier 2).
 */
public class CardsCollectionTest {

    private Card sevenOfDiamonds;
    private Card kingOfSpades;
    private Card twoOfSpades;
    private Card twoOfDiamonds;
    private Board board;

    @Before
    public void setUp() {
        sevenOfDiamonds = new Card(Rank._7, Suit.CARREAU);
        kingOfSpades = new Card(Rank._ROI, Suit.PIQUE);
        twoOfSpades = new Card(Rank._2, Suit.PIQUE);
        twoOfDiamonds = new Card(Rank._2, Suit.CARREAU);
        for (Card card : Arrays.asList(sevenOfDiamonds, kingOfSpades, twoOfSpades, twoOfDiamonds)) {
            card.reveale();
        }
        board = new Board();
    }

    @Test
    public void newBoardShouldBeEmpty() {
        assertTrue(board.isEmpty());
        assertEquals(0, board.size());
        assertEquals("[[]]", board.toString());
        assertNull(board.max());
        assertNull(board.removeTopCard());
        assertNull(board.removeCard(0));
    }

    @Test
    public void addRemoveAndClearShouldBehaveLikeAList() {
        board.addCard(sevenOfDiamonds);
        board.addCard(kingOfSpades);
        board.addCard(null);

        assertEquals(2, board.size());
        assertFalse(board.isEmpty());
        assertEquals("[[7-Carreau, Roi-Pique]]", board.toString());
        assertSame(sevenOfDiamonds, board.removeTopCard());
        assertSame(kingOfSpades, board.removeCard(0));
        assertTrue(board.isEmpty());

        board.addCard(twoOfSpades);
        board.clear();
        assertTrue(board.isEmpty());
    }

    @Test
    public void copyConstructorShouldDuplicateAnyCardsCollection() {
        board.addCard(sevenOfDiamonds);
        board.addCard(kingOfSpades);

        Board copy = new Board(board);
        Hand hand = new Hand(board);
        Deck deck = new Deck(board);

        assertEquals("[[7-Carreau, Roi-Pique]]", copy.toString());
        assertEquals("[[7-Carreau, Roi-Pique]]", hand.toString());
        assertEquals("[[7-Carreau, Roi-Pique]]", deck.toString());

        copy.removeTopCard();
        assertEquals(2, board.size());
    }

    @Test
    public void naturalSortShouldOnlyCompareRanks() {
        board.addCard(sevenOfDiamonds);
        board.addCard(kingOfSpades);
        board.addCard(twoOfSpades);
        board.addCard(twoOfDiamonds);

        board.sort();

        assertEquals("[[2-Pique, 2-Carreau, 7-Carreau, Roi-Pique]]", board.toString());
    }

    @Test
    public void comparatorSortShouldBreakTiesWithTheSuit() {
        board.addCard(sevenOfDiamonds);
        board.addCard(kingOfSpades);
        board.addCard(twoOfSpades);
        board.addCard(twoOfDiamonds);

        board.sort(new NewWarGameCardComparator());

        assertEquals("[[2-Carreau, 2-Pique, 7-Carreau, Roi-Pique]]", board.toString());
    }

    @Test
    public void maxShouldDependOnTheComparisonStrategy() {
        Board tie = new Board(Arrays.asList(twoOfDiamonds, twoOfSpades));

        assertEquals(twoOfDiamonds, tie.max());
        assertEquals(twoOfSpades, tie.max(new NewWarGameCardComparator()));
    }

    @Test
    public void shuffleShouldOnlyReorderCards() {
        board.addCard(sevenOfDiamonds);
        board.addCard(kingOfSpades);
        board.addCard(twoOfSpades);
        board.addCard(twoOfDiamonds);
        List<Card> expected = new ArrayList<Card>(Arrays.asList(
                sevenOfDiamonds, kingOfSpades, twoOfSpades, twoOfDiamonds));

        board.shuffle();

        assertEquals(4, board.size());
        for (Card card : board) {
            assertTrue(expected.remove(card));
        }
        assertTrue(expected.isEmpty());
    }

    @Test
    public void iteratorShouldVisitEveryCardInOrder() {
        board.addCard(sevenOfDiamonds);
        board.addCard(kingOfSpades);

        List<Card> visited = new ArrayList<Card>();
        for (Card card : board) {
            visited.add(card);
        }

        assertEquals(Arrays.asList(sevenOfDiamonds, kingOfSpades), visited);
    }
}
