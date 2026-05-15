package model.cards;

import java.util.Collection;

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
 * Réponse : sa sémantique métier. Un Board n'est pas une liste de cartes
 * quelconque : c'est le tapis de jeu. Ce type distinct documente l'intention,
 * permet un typage plus sûr et laisse la porte ouverte à des règles
 * spécifiques au tapis (par exemple un tapis qui refuse plus de N cartes).
 * 
 * @author francoise.perrin
 */
public class Board extends AbstractCardsCollection {

	public Board() {
		super();
	}

	public Board(Collection<Card> collection) {
		super(collection);
	}

	public Board(ICardsCollection iCardsCollection) {
		super(iCardsCollection);
	}
}
