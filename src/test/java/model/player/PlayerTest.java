package model.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import allShared.IPlayer;
import model.cards.Card;
import model.cards.Rank;
import model.cards.Suit;

/**
 * Tests unitaires de la classe {@link Player} (Atelier 1).
 */
public class PlayerTest {

    private Card twoOfDiamonds;
    private Card kingOfDiamonds;
    private Card twoOfSpades;
    private Player player;

    @Before
    public void setUp() {
        twoOfDiamonds = new Card(Rank._2, Suit.CARREAU);
        kingOfDiamonds = new Card(Rank._ROI, Suit.CARREAU);
        twoOfSpades = new Card(Rank._2, Suit.PIQUE);
        twoOfDiamonds.reveale();
        kingOfDiamonds.reveale();
        twoOfSpades.reveale();
        player = new Player("Joueur1");
    }

    @Test
    public void newPlayerShouldHaveAnEmptyHandAndTrickPile() {
        assertEquals("Joueur1", player.getName());
        assertTrue(player.isHandEmpty());
        assertTrue(player.isTrickPileEmpty());
        assertFalse(player.isStillActive());
        assertEquals("[Joueur1 ** Hand[[]] ** trickPile[[]]]", player.toString());
    }

    @Test
    public void addCardShouldFillTheRightPile() {
        player.addCardToHand(twoOfDiamonds);
        player.addCardToTrickPile(kingOfDiamonds);
        player.addCardToTrickPile(twoOfSpades);

        assertFalse(player.isHandEmpty());
        assertFalse(player.isTrickPileEmpty());
        assertTrue(player.isStillActive());
        assertEquals("[Joueur1 ** Hand[[2-Carreau]] ** trickPile[[Roi-Carreau, 2-Pique]]]",
                player.toString());
    }

    @Test
    public void playCardShouldRemoveTheCardFromTheHandAndRevealIt() {
        player.addCardToHand(twoOfDiamonds);
        player.addCardToHand(kingOfDiamonds);

        Card played = player.playCard(0);

        assertSame(twoOfDiamonds, played);
        assertTrue(played.isRevealed());
        assertFalse(player.isHandEmpty());
        assertEquals("[Joueur1 ** Hand[[Roi-Carreau]] ** trickPile[[]]]", player.toString());
    }

    @Test
    public void removeCardFromHandShouldDelegateToTheHand() {
        player.addCardToHand(twoOfDiamonds);
        player.addCardToHand(kingOfDiamonds);

        assertSame(kingOfDiamonds, player.removeCardFromHand(1));
        assertNull(player.removeCardFromHand(6));
    }

    @Test
    public void removeCardFromTrickPileShouldDelegateToTheTrickPile() {
        player.addCardToTrickPile(twoOfDiamonds);
        player.addCardToTrickPile(kingOfDiamonds);

        assertSame(twoOfDiamonds, player.removeCardFromTrickPile(0));
        assertNull(player.removeCardFromTrickPile(6));
    }

    @Test
    public void revealeAndHideShouldDelegateToTheHand() {
        player.addCardToHand(twoOfDiamonds);

        assertTrue(player.revealeCard(0));
        assertTrue(twoOfDiamonds.isRevealed());

        assertTrue(player.hideCard(0));
        assertFalse(twoOfDiamonds.isRevealed());

        assertFalse(player.revealeCard(6));
        assertFalse(player.hideCard(-1));
    }

    @Test
    public void hasWonAllCardsShouldCountBothPiles() {
        player.addCardToHand(twoOfDiamonds);
        player.addCardToTrickPile(kingOfDiamonds);
        player.addCardToTrickPile(twoOfSpades);

        assertTrue(player.hasWonAllCards(3));
        assertFalse(player.hasWonAllCards(4));
    }

    @Test
    public void winnerFlagsShouldBeReadableAndWritable() {
        assertFalse(player.isTrickWinner());
        assertFalse(player.isGameWinner());

        player.setTrickWinner(true);
        player.setGameWinner(true);

        assertTrue(player.isTrickWinner());
        assertTrue(player.isGameWinner());
    }

    @Test
    public void playersShouldBeComparedAndEqualByName() {
        Player other = new Player("Joueur2");
        IPlayer third = new Player("Joueur3");

        assertTrue(player.compareTo(player) == 0);
        assertTrue(player.compareTo(other) < 0);
        assertEquals(2, third.compareTo(player));

        assertEquals(new Player("Joueur1"), player);
        assertEquals(new Player("Joueur1").hashCode(), player.hashCode());
        assertNotEquals(player, other);
    }
}
