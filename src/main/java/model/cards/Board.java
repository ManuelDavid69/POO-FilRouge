package model.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import allShared.ICardsCollection;

/**
 * Objet qui contient l'ensemble des cartes déposées sur le tapis
 *
 * Il est capable d'exécuter les traitements communs à toutes 
 * les collections de cartes (ajouter, supprimer, mélanger, trier, etc.)
 *  
 * Question CB : qu'est-ce qui justifie existence de cette classe 
 * qui n'a aucun comportement spécifique ?
 * 
 * @author francoise.perrin
 */
public  class Board extends AbstractCardsCollection {
	
	
	/*
	 *************** TODO ToChange Atelier2 ***************
	 */
	
	private final List<Card> cards; 

	public Board() {
		super();
		cards = new ArrayList<Card>();
	}

	public Board(Collection<Card> collection) {
		super();
		cards = new ArrayList<Card>(collection);
	}

	public Board(ICardsCollection iCardsCollection) {
		super();
		cards = null;
	}

	@Override
	public void shuffle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Card removeTopCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Card removeCard(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCard(Card pc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Card max() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Card max(Comparator<Card> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sort() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sort(Comparator<Card> comparator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Card> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
