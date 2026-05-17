package model.cards;

import java.util.Collection;

import allShared.ICardsCollection;

/**
 * Objet qui fabrique et contient l'ensemble des cartes du Game
 * en fonction de la taille du Deck (32 ou 52 cartes)
 *
 * Il est capable d'exécuter les traitements communs à toutes 
 * les collections de cartes (ajouter, supprimer, mélanger, trier, etc.)
 * et en plus fabriquer un jeu de carte
 * 
 * @author francoise.perrin
 */
public class Deck extends AbstractCardsCollection {

	/** Index du rang _7 dans Rank.values() : premier rang d'un jeu de 32 cartes. */
	private static final int FIRST_RANK_OF_32_DECK = 5;

	/**
	 * Fabrique un Deck vide.
	 */
	public Deck() {
		super();
	}

	/**
	 * le constructeur fabrique un jeu de 32 ou de 52 cartes
	 * selon la taille du Deck passée en paramètre
	 * 
	 * Ex si deckSize = 32, le contenu du Deck est le suivant :
	 * 	[[7-Carreau, 7-Coeur, 7-Pique, 7-Trefle, 
	 * 	8-Carreau, 8-Coeur, 8-Pique, 8-Trefle, 
	 * 	9-Carreau, 9-Coeur, 9-Pique, 9-Trefle, 
	 * 	10-Carreau, 10-Coeur, 10-Pique, 10-Trefle, 
	 * 	Valet-Carreau, Valet-Coeur, Valet-Pique, Valet-Trefle, 
	 * 	Dame-Carreau, Dame-Coeur, Dame-Pique, Dame-Trefle, 
	 * 	Roi-Carreau, Roi-Coeur, Roi-Pique, Roi-Trefle, 
	 * 	As-Carreau, As-Coeur, As-Pique, As-Trefle]]
	 * 
	 * @param deckSize 32 (sans les 2 à 6) ou 52 (jeu complet)
	 */
	public Deck(int deckSize) {
		super();

		int firstRank;
		if (deckSize == 32) {
			firstRank = FIRST_RANK_OF_32_DECK;
		} else if (deckSize == 52) {
			firstRank = 0;
		} else {
			throw new IllegalArgumentException(
					"Un Deck doit contenir 32 ou 52 cartes (demandé : " + deckSize + ")");
		}

		Rank[] ranks = Rank.values();
		for (int rankIndex = firstRank; rankIndex < ranks.length; rankIndex++) {
			for (Suit suit : Suit.values()) {
				this.addCard(new Card(ranks[rankIndex], suit));
			}
		}
	}

	public Deck(Collection<Card> collection) {
		super(collection);
	}

	public Deck(ICardsCollection iCardsCollection) {
		super(iCardsCollection);
	}
}
