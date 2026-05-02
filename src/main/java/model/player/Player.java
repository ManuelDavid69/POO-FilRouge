package model.player;

import java.util.Iterator;

import allShared.CardsCollectionType;
import allShared.IPlayer;
import controller.CardsCollectionFactory;
import model.cards.Card;
import model.cards.Hand;

/**
 * Classe qui définit le comportement des Joueurs
 * 
 * Discussion Carnet de Bord : pertinence isStillActive() + isHandEmpty() ?
 * 
 * @author francoise.perrin
 */

public class Player implements IPlayer{
	private final String name;
	private final Hand hand;
	private final Hand trickPile;
	private boolean isTrickWinner;
	private boolean isGameWinner;
	
	public Player(String name) {
		this.name = name;
		this.hand = (Hand) CardsCollectionFactory.getCardsCollection(CardsCollectionType.HAND);
		this.trickPile = (Hand) CardsCollectionFactory.getCardsCollection(CardsCollectionType.HAND);
		this.isTrickWinner = false;
		this.isGameWinner = false;
	}
	
	public final void addCardToHand(Card pc) {
		/*
		 * TODO Atelier1
		 */
	}
	
	public final void addCardToTrickPile(Card pc) {
		/*
		 * TODO Atelier1
		 */
	}
	
	public final Card playCard(int index) {
		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card;
	}
	
	public final Card removeCardFromHand(int index) {
		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card;
	}
	
	public final Card removeCardFromTrickPile(int index) {
		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card;
	}
	
	public final boolean revealeCard(int index) {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}
	
	public final boolean hideCard(int index) {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}
	
	@Override
	public final String getName() {
		return this.name;
	}
	
	public final boolean isHandEmpty() {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}
	
	public final boolean isTrickPileEmpty() {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}
	
	/**
	 * return true si la main du joueur n'est pas vide
	 */
	@Override
	public final boolean isStillActive() {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}

	
	/**
	 * retourne true si le joueur possède toutes les cartes 
	 * du deck initial 
	 */
	public final boolean hasWonAllCards(int deckSize) {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}
	
	@Override
	public final boolean isTrickWinner() {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}

	public final void setTrickWinner(boolean isTrickWinner) {
		/*
		 * TODO Atelier1
		 */
	}

	public final boolean isGameWinner() {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}

	public final void setGameWinner(boolean isGameWinner) {
		/*
		 * TODO Atelier1
		 */
	}

	@Override
	public String toString() {
		return "["+ name + " ** Hand" + hand + " ** trickPile" + trickPile + "]";
	}

	/*
	La comparaison s'effectue
	selon l'ordre alphabétique des noms de joueurs
	 */
	@Override
	public int compareTo(IPlayer arg0) {
		int ret = -999999;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}

	/**
	 * Toutes les cartes gagnées retournent dans la main du joueur
	 * après avoir été mélangées
	 * * Ecrivez et testez cette méthode de 2 manières :
	 *  1 - en utilisant l'itérator de manière implicite
	 *  2 - en utilisant l'Iterator de manière explicite  
	 */
	public void addWonCardsBackToHand() {

		/*
		 * TODO Atelier2
		 */
		  
	}
	
}
