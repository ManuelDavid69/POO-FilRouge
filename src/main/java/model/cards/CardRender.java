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
		Rank ret = null;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}

	@Override
	public final Suit getSuit() {
		Suit ret = null;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}
	
	@Override
	public final boolean isRevealed() {
		boolean ret = false;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}
	
	@Override
	public int compareTo(CardRender o) {
		int ret = -99999;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}
// regénérer equals et hashcode avec IDE
	@Override
	public int hashCode() {
		int ret = -99999;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}

	@Override
	public String toString() {
		String ret = null;
		/*
		 * TODO Atelier2
		 */
		return ret;
	}
}

