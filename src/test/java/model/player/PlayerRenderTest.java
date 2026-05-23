package model.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.cards.Card;
import model.cards.Rank;
import model.cards.Suit;

/**
 * Tests unitaires du proxy {@link PlayerRender} (Atelier 2).
 */
public class PlayerRenderTest {

    @Test
    public void renderShouldExposeThePlayerState() {
        Player player = new Player("Alice");
        PlayerRender render = new PlayerRender(player);

        assertEquals("Alice", render.getName());
        assertEquals("Alice", render.toString());
        assertFalse(render.isStillActive());
        assertFalse(render.isTrickWinner());
        assertFalse(render.isGameWinner());

        player.addCardToHand(new Card(Rank._2, Suit.CARREAU));
        player.setTrickWinner(true);
        player.setGameWinner(true);

        assertTrue(render.isStillActive());
        assertTrue(render.isTrickWinner());
        assertTrue(render.isGameWinner());
    }

    @Test
    public void rendersOfTheSamePlayerShouldBeEqualAndCompareEqual() {
        PlayerRender first = new PlayerRender(new Player("Alice"));
        PlayerRender second = new PlayerRender(new Player("Alice"));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(0, first.compareTo(second));
    }

    @Test
    public void compareToShouldUseTheAlphabeticalPlayerOrder() {
        PlayerRender alice = new PlayerRender(new Player("Alice"));
        PlayerRender bob = new PlayerRender(new Player("Bob"));

        assertTrue(alice.compareTo(bob) < 0);
        assertTrue(bob.compareTo(alice) > 0);
    }
}
