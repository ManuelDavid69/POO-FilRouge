package model.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import allShared.ICardsCollection;

/**
 * Classe qui décrit les attributs et méthodes commun(e)s à toutes les Collections de cartes
 * elle implémente l'interface Iterable pour être parcourue par un Iterator [Design pattern]
 * 
 * Elle s'appuie sur les méthodes de sa Collection enveloppée (ici List)  
 * enrichies des méthodes shuffle(), max() et iterator()
 * [On peut la voir comme un adapter de List - Design Pattern]
 * 
 * [Question du Carnet de Bord : qu'est-ce qui justifie l'existence de cette classe ?
 * La réponse n'est pas "... factoriser le code commun à ses classes dérivées" 
 * (même si cette phrase est néanmoins vraie ;-)) ]
 * 
 * @author francoise.perrin
 */
public abstract class AbstractCardsCollection implements ICardsCollection, Iterable<Card> {

	/**
	 * Collection enveloppée : toutes les opérations communes sont déléguées à cette List.
	 * Visible par les sous-classes pour que le constructeur par copie puisse la partager.
	 */
	protected final List<Card> cards;

	public AbstractCardsCollection() {
		this.cards = new ArrayList<Card>();
	}

	public AbstractCardsCollection(Collection<Card> collection) {
		this.cards = new ArrayList<Card>(collection);
	}

	/**
	 * Constructeur par copie polymorphe : fabrique une collection à partir
	 * de n'importe quelle autre collection de cartes (Deck, Hand ou Board).
	 * 
	 * @param iCardsCollection collection source (instance d'AbstractCardsCollection)
	 */
	public AbstractCardsCollection(ICardsCollection iCardsCollection) {
		this(((AbstractCardsCollection) iCardsCollection).cards);
	}

	@Override
	public void addCard(Card pc) {
		if (pc != null) {
			this.cards.add(pc);
		}
	}

	@Override
	public Card removeTopCard() {
		if (this.cards.isEmpty()) {
			return null;
		}
		return this.cards.remove(0);
	}

	@Override
	public Card removeCard(int index) {
		if (index < 0 || index >= this.cards.size()) {
			return null;
		}
		return this.cards.remove(index);
	}

	@Override
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}

	@Override
	public void clear() {
		this.cards.clear();
	}

	@Override
	public int size() {
		return this.cards.size();
	}

	@Override
	public void sort() {
		Collections.sort(this.cards);
	}

	@Override
	public void sort(Comparator<Card> comparator) {
		if (comparator == null) {
			this.sort();
			return;
		}
		Collections.sort(this.cards, comparator);
	}

	@Override
	public void shuffle() {
		Collections.shuffle(this.cards);
	}

	@Override
	public Card max() {
		if (this.cards.isEmpty()) {
			return null;
		}
		return Collections.max(this.cards);
	}

	@Override
	public Card max(Comparator<Card> comparator) {
		if (comparator == null || this.cards.isEmpty()) {
			return this.max();
		}
		return Collections.max(this.cards, comparator);
	}

	@Override
	public Iterator<Card> iterator() {
		return this.cards.iterator();
	}

	@Override
	public String toString() {
		return "[" + this.cards + "]";
	}
}
