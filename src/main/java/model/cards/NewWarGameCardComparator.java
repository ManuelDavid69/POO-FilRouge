package model.cards;

import java.util.Comparator;

/**
 * Ce comparateur de Card compare les valeurs (Rank) des cartes
 * et en cas d'égalité leur couleur (Suit) 
 * 
 * @author francoise.perrin
 */
public class NewWarGameCardComparator implements Comparator<Card>{

	@Override
	public int compare(Card card1, Card card2) {
		int diff = card1.getRank().getRank() - card2.getRank().getRank();
		if (diff == 0) {
			diff = card1.getSuit().value() - card2.getSuit().value();
		}
		return diff;
	}
}