package model.games;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import allShared.IGameEvaluator;
import model.cards.Board;
import model.cards.Card;
import model.cards.Rank;
import model.cards.Suit;

/**
 * Tests unitaires des évaluateurs de pli (Atelier 2).
 */
public class GameEvaluatorTest {

    private static Card card(Rank rank, Suit suit) {
        Card card = new Card(rank, suit);
        card.reveale();
        return card;
    }

    @Test
    public void classicEvaluatorShouldReturnTheUniqueHighestCard() {
        IGameEvaluator evaluator = new ClassicWarGameEvaluator();
        Board board = new Board();
        board.addCard(card(Rank._2, Suit.CARREAU));
        board.addCard(card(Rank._2, Suit.PIQUE));
        board.addCard(card(Rank._7, Suit.CARREAU));
        board.addCard(card(Rank._ROI, Suit.PIQUE));

        assertEquals(card(Rank._ROI, Suit.PIQUE), evaluator.evaluateTrickWinner(board));
    }

    @Test
    public void classicEvaluatorShouldReturnNullWhenTheBestRankIsTied() {
        IGameEvaluator evaluator = new ClassicWarGameEvaluator();
        Board board = new Board();
        board.addCard(card(Rank._2, Suit.CARREAU));
        board.addCard(card(Rank._2, Suit.PIQUE));

        assertNull(evaluator.evaluateTrickWinner(board));
    }

    @Test
    public void newEvaluatorShouldBreakRankTiesWithTheSuit() {
        IGameEvaluator evaluator = new NewWarGameEvaluator();
        Board board = new Board();
        board.addCard(card(Rank._2, Suit.CARREAU));
        board.addCard(card(Rank._2, Suit.PIQUE));

        assertEquals(card(Rank._2, Suit.PIQUE), evaluator.evaluateTrickWinner(board));
    }

    @Test
    public void newEvaluatorShouldReturnNullWhenTheSameCardIsPlayedTwice() {
        IGameEvaluator evaluator = new NewWarGameEvaluator();
        Board board = new Board();
        board.addCard(card(Rank._ROI, Suit.CARREAU));
        board.addCard(card(Rank._ROI, Suit.CARREAU));

        assertNull(evaluator.evaluateTrickWinner(board));
    }

    @Test
    public void evaluatorsShouldReturnNullOnAnEmptyTrick() {
        assertNull(new ClassicWarGameEvaluator().evaluateTrickWinner(new Board()));
        assertNull(new NewWarGameEvaluator().evaluateTrickWinner(new Board()));
    }
}
