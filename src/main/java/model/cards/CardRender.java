package model.cards;

import allShared.ICard;

/**
 * Cette classe peut être vue comme un Proxy ou un Adapter [Design Pattern] de la classe Card
 * Ses instances sont utilisées par la View qui
 * n'a accès qu'aux "Getters" de l'objet encapsulé
 * 
 * Cette classe restreint donc le nombre de fonctionnalités existantes de l'objet enveloppé
 * et lui délègue le soin de réaliser les traitements
 * 
 * @author francoise.perrin
 */
public class CardRender implements ICard, Comparable<CardRender> {
	private final Card card;
	
	public CardRender(Card card) {
		super();
		this.card = card;
	}

	@Override
	public final Rank getRank() {
		return this.card.getRank();
	}

	@Override
	public final Suit getSuit() {
		return this.card.getSuit();
	}
	
	@Override
	public final boolean isRevealed() {
		return this.card.isRevealed();
	}
	
	@Override
	public int compareTo(CardRender o) {
		return this.card.compareTo(o.card);
	}

	@Override
	public int hashCode() {
		return this.card.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardRender other = (CardRender) obj;
		return this.card.equals(other.card);
	}

	@Override
	public String toString() {
		return this.card.toString();
	}
}

