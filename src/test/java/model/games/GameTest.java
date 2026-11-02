package model.games;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import allShared.ICard;
import allShared.ICardsCollection;
import allShared.IGameEvaluator;
import allShared.IPlayer;
import model.cards.Card;
import model.cards.CardRender;
import model.cards.Deck;
import model.cards.Rank;
import model.cards.Suit;
import model.player.Player;
import model.player.PlayerRender;

/**
 * Tests d'intégration du moteur de jeu (Atelier 3).
 * 
 * La classe {@link ScriptedWarGame} distribue les cartes sans mélanger,
 * ce qui rend les scénarios déterministes.
 */
public class GameTest {

    private static Card card(Rank rank, Suit suit) {
        Card card = new Card(rank, suit);
        card.reveale();
        return card;
    }

    private static List<String> players() {
        return Arrays.asList("Alice", "Bob");
    }

    /**
     * Variante de test qui ne mélange pas le paquet afin de contrôler
     * exactement la main de chaque joueur.
     */
    private static final class ScriptedWarGame extends AbstractGame {

        ScriptedWarGame(List<String> playersNames, ICardsCollection deck) {
            super(playersNames, deck);
        }

        @Override
        protected void dealCardsFromDeck(int nbCards) {
            for (int round = 0; round < nbCards; round++) {
                for (Player player : this.players) {
                    Card card = this.deck.removeTopCard();
                    if (card != null) {
                        player.addCardToHand(card);
                    }
                }
            }
        }

        @Override
        protected IGameEvaluator getGameEvaluator() {
            return new ClassicWarGameEvaluator();
        }

        @Override
        public boolean isGameEnd() {
            for (Player player : this.players) {
                if (player.hasWonAllCards(this.initDeckSize)) {
                    player.setGameWinner(true);
                    return true;
                }
            }
            return false;
        }
    }

    @Test
    public void gameShouldDealCardsAndStartWithAnEmptyMat() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE),
                card(Rank._7, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);

        assertFalse(game.isGameEnd());
        assertEquals("[Alice ** Hand[[2-Carreau, 7-Carreau]] ** trickPile[[]]]",
                game.players.get(0).toString());
        assertEquals("[Bob ** Hand[[Roi-Pique, 2-Pique]] ** trickPile[[]]]",
                game.players.get(1).toString());
        assertTrue(game.getGamingMatRender().isEmpty());
        assertNull(game.theWinnerIs());
    }

    @Test
    public void aTrickShouldBePlayedEvaluatedAndCollected() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE),
                card(Rank._7, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);
        Player alice = game.players.get(0);
        Player bob = game.players.get(1);

        game.PlayCards(null);

        assertEquals(2, game.getGamingMatRender().size());
        assertFalse(alice.isTrickWinner());
        assertFalse(bob.isTrickWinner());

        assertTrue(game.evaluateTrickWinner());
        assertFalse(alice.isTrickWinner());
        assertTrue(bob.isTrickWinner());

        Map<IPlayer, ICard> render = game.getGamingMatRender();
        assertTrue(render.containsValue(new CardRender(card(Rank._ROI, Suit.PIQUE))));

        game.theWinnerTakesItAll();

        assertFalse(bob.isTrickWinner());
        assertFalse(bob.isTrickPileEmpty());
        assertEquals("[Bob ** Hand[[2-Pique]] ** trickPile[[2-Carreau, Roi-Pique]]]",
                bob.toString());
        assertEquals("[Alice ** Hand[[7-Carreau]] ** trickPile[[]]]", alice.toString());
    }

    @Test
    public void battleShouldKeepThePotUntilTheTrickIsWon() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE),
                card(Rank._7, Suit.COEUR),
                card(Rank._ROI, Suit.TREFLE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);
        Player alice = game.players.get(0);
        Player bob = game.players.get(1);

        game.PlayCards(null);
        assertFalse(game.evaluateTrickWinner());
        assertFalse(alice.isTrickWinner());
        assertFalse(bob.isTrickWinner());

        game.PlayCards(null);

        /* le tapis contient les 4 cartes de la bataille, mais le pli courant n'en compte que 2 */
        assertEquals(2, game.getGamingMatRender().size());
        assertTrue(game.evaluateTrickWinner());
        assertTrue(bob.isTrickWinner());

        game.theWinnerTakesItAll();

        assertTrue(bob.hasWonAllCards(4));
        assertTrue(game.isGameEnd());
        assertNotNull(game.theWinnerIs());
        assertEquals("Bob", game.theWinnerIs().getName());
    }

    @Test
    public void chosenCardIndexShouldBeRespected() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE),
                card(Rank._7, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);
        Map<String, Integer> choice = new HashMap<String, Integer>();
        choice.put("Alice", Integer.valueOf(1));
        choice.put("Bob", Integer.valueOf(0));

        game.PlayCards(choice);

        Map<IPlayer, ICard> render = game.getGamingMatRender();
        assertTrue(render.containsValue(new CardRender(card(Rank._7, Suit.CARREAU))));
        assertTrue(render.containsValue(new CardRender(card(Rank._ROI, Suit.PIQUE))));
    }

    @Test
    public void invalidCardIndexShouldFallBackToTheTopCard() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE),
                card(Rank._7, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);
        Map<String, Integer> choice = new HashMap<String, Integer>();
        choice.put("Alice", Integer.valueOf(99));

        game.PlayCards(choice);

        assertTrue(game.getGamingMatRender().containsValue(
                new CardRender(card(Rank._2, Suit.CARREAU))));
    }

    @Test
    public void aPlayerWithoutCardsShouldRecycleWonCardsBeforePlaying() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE),
                card(Rank._7, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);
        Player alice = game.players.get(0);

        while (!alice.isHandEmpty()) {
            alice.addCardToTrickPile(alice.playCard(0));
        }
        assertTrue(alice.isHandEmpty());
        assertFalse(alice.isTrickPileEmpty());

        game.PlayCards(null);

        assertFalse(alice.isHandEmpty());
        assertTrue(alice.isTrickPileEmpty());
        assertEquals(2, game.getGamingMatRender().size());
    }

    @Test
    public void renderShouldExposeOnlyProxiesSortedByPlayerName() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE),
                card(Rank._7, Suit.CARREAU),
                card(Rank._2, Suit.PIQUE)));

        ScriptedWarGame game = new ScriptedWarGame(players(), deck);
        game.PlayCards(null);

        Map<IPlayer, ICard> render = game.getGamingMatRender();
        assertEquals(2, render.size());

        int index = 0;
        for (Map.Entry<IPlayer, ICard> entry : render.entrySet()) {
            assertTrue(entry.getKey() instanceof PlayerRender);
            assertTrue(entry.getValue() instanceof CardRender);
            assertEquals(index == 0 ? "Alice" : "Bob", entry.getKey().getName());
            index++;
        }
    }

    @Test
    public void twoCardGameShouldEndWithAWinner() {
        Deck deck = new Deck(Arrays.asList(
                card(Rank._2, Suit.CARREAU),
                card(Rank._ROI, Suit.PIQUE)));

        ClassicWarGame game = new ClassicWarGame(players(), deck);

        assertFalse(game.isGameEnd());
        game.PlayCards(null);
        assertTrue(game.evaluateTrickWinner());
        game.theWinnerTakesItAll();

        assertTrue(game.isGameEnd());
        PlayerRender winner = game.theWinnerIs();
        assertNotNull(winner);
        assertTrue(winner.isGameWinner());
    }
}
