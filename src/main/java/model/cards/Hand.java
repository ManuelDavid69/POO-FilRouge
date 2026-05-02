package model.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
public class Hand implements ICardsCollection {

	private final List<Card> cards; /* ToChange Atelier2 */

	/*
	 * TODO ToChange Atelier2
	 */
	public Hand() {
		super();
		cards = new ArrayList<Card>();
	}

	/*
	 * TODO ToChange Atelier2
	 */
	public Hand(Collection<Card> collection) {
		super();
		cards = new ArrayList<Card>(collection);
	}

	/*
	 * TODO ToChange Atelier2
	 */
	public Hand(ICardsCollection iCardsCollection) {
		super();		// TODO - à supprimer Atelier 2
		cards = null; 	// TODO - à supprimer Atelier 2
		//super(iCardsCollection); // TODO - à activer Atelier 2

	}

	@Override
	public final void addCard(Card pc) {
		/*
		 * TODO Atelier1
		 */
	}

	@Override
	public final Card removeTopCard() {
		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card;
	}

	@Override
	public final Card removeCard(int index) {
		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card;
	}

	@Override
	public final boolean isEmpty() {
		boolean ret = false;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}

	@Override
	public final void clear() {
		/*
		 * TODO Atelier1
		 */
	}

	@Override
	public final int size() {
		int ret = 0;
		/*
		 * TODO Atelier1
		 */
		return ret;
	}

//	@Override
//	public String toString() {
//		return "[" + cards + "]";
//	}

	/**
	 * @param index
	 * @return la carte à jouer si elle existe
	 * Supprime la carte de la liste
	 */
	public final Card playCard(int index) {

		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card;
	}

	/**
	 * @param index
	 * @return true si la carte existe 
	 */
	public final boolean revealeCard(int index) {

		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card != null ? true : false;
	}

	/**
	 * @param index
	 * @return true si la carte existe 
	 */
	public final boolean hideCard(int index) {

		Card card = null;
		/*
		 * TODO Atelier1
		 */
		return card != null ? true : false;
	}


	@Override
	public void sort() {
		/*
		 * TODO Atelier2
		 */
	}

	@Override
	public void sort(Comparator<Card> comparator) {
		/*
		 * TODO Atelier2
		 */
	}


	/**
	 * Mélange les cartes de manière aléatoire
	 * 
	 * Ecrivez et testez cette méthode de 2 manières :
	 *  1 - en utilisant la méthode native shuffle() de la classe Collections
	 *  2 - en utilisant la méthode swap() et un nombre aléatoire (Random)  
	 */
	@Override
	 public final void shuffle() {
		 /*
		  * TODO Atelier2
		  */
	 }

	 @Override
	 public final Card max() {
		 Card card = null;
		 /*
		  * TODO Atelier2
		  */
		 return  card;
	 }

	 @Override
	 public final Card max(Comparator<Card> comparator) {
		 Card card = null;
		 /*
		  * TODO Atelier2
		  */
		 return  card;
	 }

	 /*
	  * Illustration du Design Pattern Iterator
	  * et des classes anonymes
	  * 
	  * [ Cette partie du code sera utile à partir de l'atelier 2
	  * Ce n'est pas grave si vous ne la comprenez pas ...]
	  */
	 @Override
	 public final Iterator<Card> iterator() {

		 return new Iterator<Card>() {
			 Iterator<Card> it =  cards.iterator();
			 @Override
			 public boolean hasNext() {
				 return it.hasNext();
			 }

			 @Override
			 public Card next() {
				 return it.next();
			 }

		 };
	 }


}