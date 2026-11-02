package model.cards;

import java.util.Collection;

import allShared.ICardsCollection;


/**
 * Objet qui contient l'ensemble des cartes de la main d'un joueur
 * 
 * Il est capable d'exécuter les traitements communs à toutes 
 * les collections de cartes (ajouter, supprimer, mélanger, trier, etc.)
 * et en plus révéler, cacher, jouer une carte
 * 
 * @author francoise.perrin
 */
public class Hand extends AbstractCardsCollection {

	public Hand() {
		super();
	}

	public Hand(Collection<Card> collection) {
		super(collection);
	}

	public Hand(ICardsCollection iCardsCollection) {
		super(iCardsCollection);
	}

	/**
	 * @param index
	 * @return la carte à jouer si elle existe
	 * Supprime la carte de la liste
	 */
	public final Card playCard(int index) {

		if (index < 0 || index >= this.cards.size()) {
			return null;
		}
		Card card = this.cards.remove(index);
		card.reveale();
		return card;
	}

	/**
	 * @param index
	 * @return true si la carte existe 
	 */
	public final boolean revealeCard(int index) {

		if (index < 0 || index >= this.cards.size()) {
			return false;
		}
		this.cards.get(index).reveale();
		return true;
	}

	/**
	 * @param index
	 * @return true si la carte existe 
	 */
	public final boolean hideCard(int index) {

		if (index < 0 || index >= this.cards.size()) {
			return false;
		}
		this.cards.get(index).hide();
		return true;
	}
}
