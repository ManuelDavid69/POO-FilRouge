package launcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import allShared.CardsCollectionType;
import allShared.ICard;
import allShared.ICardsCollection;
import allShared.IGameEvaluator;
import allShared.IGameView;
import allShared.IPlayer;
import controller.CardsCollectionFactory;
import model.cards.Board;
import model.cards.Card;
import model.cards.CardRender;
import model.cards.Deck;
import model.cards.Hand;
import model.cards.NewWarGameCardComparator;
import model.cards.Rank;
import model.cards.Suit;
import model.games.ClassicWarGameEvaluator;
import model.games.NewWarGameEvaluator;
import model.player.Player;
import model.player.PlayerRender;
import view.ConsoleTui;
import view.GameConsoleView;
import view.GameSwingView;
import view.GameViews;

/**
 * Programme de test des classes du Model pour l'atelier 2
 * La trace d'exécution attendue est écrite en commentaire de chaque instruction
 * 
 * @author francoise.perrin
 */
public class TestAtelier2 {

	private static final ConsoleTui TUI = new ConsoleTui();

	public static void main(String[] args) {
		
		TUI.title("Test Atelier 2", "Scenarios de validation des collections, comparateurs, evaluateurs et views");
		
		Card c1 = new Card(Rank._2, Suit.CARREAU);
		Card c2 = new Card(Rank._ROI, Suit.CARREAU);
		Card c3 = new Card(Rank._2, Suit.CARREAU);
		Card c4 = new Card(Rank._2, Suit.PIQUE);
		
		
		
		////////////////////////////////////////////////////////
		// Test classe Hand - Méthodes de la classe Collections    
		////////////////////////////////////////////////////////

		TUI.section("Test classe Hand");
		Hand hand = new Hand();
		
		hand.addCard(c1);
		hand.addCard(c2);
		hand.addCard(c3);
		hand.addCard(c4);
		
		/* 
		 * Vérifiez dans constructeur classe Card que 
		 * ligne "this.isFaceUp : true;" ne soit pas commentée 
		 */
		TUI.note("Precondition", "Verifiez dans le constructeur de Card que this.isFaceUp = true n'est pas commente.");
		TUI.check("hand", hand, "[[2-Carreau, Roi-Carreau, 2-Carreau, 2-Pique]]");
	
		
		/* Test shuffle() avec 2 algos différents (commentez l'un puis l'autre pour tester) */
		hand.shuffle();
		// le résultat après mélange ALEATOIRE des cartes sera différent d'une exécution à l'autre...
		TUI.check("hand apres shuffle()", hand, "ordre aleatoire");
		
		
		/* Test sort() */
		hand.sort();
		TUI.check("hand apres sort()", hand, "[[2-Carreau, 2-Carreau, 2-Pique, Roi-Carreau]]");
		
		
		/* Test max() */
		Card maxCard = hand.max();
		TUI.check("maxCard", maxCard, "Roi-Carreau");
		
		
		
		///////////////////////////////////////////////////////////////////////////////////
		// Test classe Deck et AbstractCardsCollection 
		// La classe Deck sait faire quasi les mêmes opérations que la classe Hand
		// Il est temps de factoriser ces méthodes dans la classe AbstractCardsCollection
		//////////////////////////////////////////////////////////////////////////////////
		
		
		/* Déplacez les méthodes communes dans AbstractCardsCollection
		 * Notifiez le lien d'héritage dans la classe Hand
		 * Testez : le résultat doit être le même que précédemment ...
		 */
		
		
		TUI.section("Test classe Deck");
		
		/* Test Deck() et autres méthodes de la classe Deck */
		ICardsCollection deck = new Deck(32);
		TUI.info("Deck", deck);
		deck.shuffle();
		TUI.check("Deck apres shuffle", deck, "ordre aleatoire");
		TUI.check("Max du Deck", deck.max(), "As-Carreau");
		
		
		
		///////////////////////////////////////////////////////////////////////////////////
		// Test classe Board  
		// Elle sait faire les mêmes opérations que la classe AbstractCardsCollection
		//////////////////////////////////////////////////////////////////////////////////
		
		TUI.section("Test classe Board");
		
		/* Test Board() */
		ICardsCollection board1 = CardsCollectionFactory.getCardsCollection(CardsCollectionType.BOARD);
		TUI.check("Board1", board1, "[[]]");
		
		
		/* Test Board(Collection) */
		Card[] tabCards = {new Card(Rank._7, Suit.CARREAU),new Card(Rank._ROI, Suit.PIQUE)};
		List<Card> listCards = Arrays.asList(tabCards);
		ICardsCollection board2 = new Board(listCards);
		TUI.check("Board2", board2, "[[7-Carreau, Roi-Pique]]");
		
		
		/* Test autres méthodes héritées de AbstractCardsCollection */
		board2.addCard(c4);
		TUI.check("Board2 apres add", board2, "[[7-Carreau, Roi-Pique, 2-Pique]]");
		board2.removeCard(2);
		TUI.check("Board2 apres remove", board2, "[[7-Carreau, Roi-Pique]]");
		
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// Test construction d'une ICardsCollection à partir de n'importe quelle autre
		// Complétez ce constructeur dans les classes AbstractCardsCollection, Hand, Deck et Board 
		// et décommentez les lignes suivantes pour tester
		////////////////////////////////////////////////////////////////////////////////////////// 
		
		TUI.section("Test constructeur par copie");
		
		ICardsCollection board3 = new Board(board2);
		TUI.check("Board3", board3, "[[7-Carreau, Roi-Pique]]");
		TUI.check("Hand2", new Hand(board2), "[[7-Carreau, Roi-Pique]]");
		TUI.check("Deck2", new Deck(board2), "[[7-Carreau, Roi-Pique]]");
		
		
		
		//////////////////////////////////
		// Test Comparator     
		//////////////////////////////////
		
		TUI.section("Test comparateur de cartes");
		
		
		/* Test compareTo() et compare() sur objets de type Card */
		NewWarGameCardComparator cardComparator = new NewWarGameCardComparator();
		TUI.check("Comparaison c1/c2", c1.compareTo(c2) + " *** " + cardComparator.compare(c1, c2), "-11 *** -11");
		TUI.check("Comparaison c3/c4", c3.compareTo(c4) + " *** " + cardComparator.compare(c3, c4), "0 *** -2");
		
		
		/* Test tri selon l'ordre naturel ou avec le Comparator */
		board2.addCard(c4);
		board2.addCard(c1);
		board3 = new Board(board2);
		TUI.check("board2 avant tri", board2, "[[7-Carreau, Roi-Pique, 2-Pique, 2-Carreau]]");
		TUI.check("board3 avant tri", board3, "[[7-Carreau, Roi-Pique, 2-Pique, 2-Carreau]]");
		board2.sort();
		board3.sort(cardComparator);
		TUI.check("board2 apres tri", board2, "[[2-Pique, 2-Carreau, 7-Carreau, Roi-Pique]]");
		TUI.check("board3 apres tri Comparator", board3, "[[2-Carreau, 2-Pique, 7-Carreau, Roi-Pique]]");
		
		
		/* Test recherche du max selon l'ordre naturel ou avec le Comparator */
		hand.clear();
		hand.addCard(c1);
		hand.addCard(c4);
		TUI.check("hand", hand, "[[2-Carreau, 2-Pique]]");
		TUI.check("hand.max()", hand.max(), "2-Carreau");
		TUI.check("hand.max(cardComparator)", hand.max(cardComparator), "2-Pique");
		
	
		
		/////////////////////////////////////////////////////////////
		// Test Iterator de ICardsCollection 
		// dans la méthode addWonCardsBackToHand() de la classe Player
		////////////////////////////////////////////////////////////
		
		TUI.section("Test Iterator dans Player.addWonCardsBackToHand()");
		Player p1 = new Player("Joueur1");
	
		p1.addCardToHand(c1);
		p1.addCardToTrickPile(c2);
		p1.addCardToTrickPile(c4);
		TUI.check("p1", p1, "[Joueur1 ** Hand[[2-Carreau]] ** trickPile[[Roi-Carreau, 2-Pique]]]");

		
		/* Test addWonCardsBackToHand() avec un iterator implicite, puis explicite */
		p1.addWonCardsBackToHand();
		TUI.check("Apres addWonCardsBackToHand()", p1, "[Joueur1 ** Hand[[2-Carreau, 2-Pique, Roi-Carreau]] ** trickPile[[]]]");
		
	
		
		////////////////////////////////////////////////////////////////////////
		// Test Evaluateurs de plis - classe AbstractGameEvaluator et dérivées
		// Etudiez et comprenez la structure de evaluateTrickWinner() 
		// puis codez les méthodes dans les classes dérivées
		////////////////////////////////////////////////////////////////////////
		
		TUI.section("Test evaluateTrickWinner()");
		TUI.check("hand", hand, "[[2-Carreau, 2-Pique]]");
		TUI.check("board3", board3, "[[2-Carreau, 2-Pique, 7-Carreau, Roi-Pique]]");
		
		
		/* Test evaluateTrickWinner() de classe ClassicWarGameEvaluator */
		IGameEvaluator classicEvaluator = new ClassicWarGameEvaluator();
		TUI.check("classicEvaluator board3", classicEvaluator.evaluateTrickWinner(board3), "Roi-Pique");
		TUI.check("classicEvaluator hand", classicEvaluator.evaluateTrickWinner(hand), "null");
		
		
		/* Test evaluateTrickWinner() de classe NewWarGameEvaluator */
		IGameEvaluator newEvaluator = new NewWarGameEvaluator();
		TUI.check("newEvaluator board3", newEvaluator.evaluateTrickWinner(board3), "Roi-Pique");
		TUI.check("newEvaluator hand", newEvaluator.evaluateTrickWinner(hand), "2-Pique");
		
		
		
		////////////////////////////////////////////////////////////////////////
		// Test CardRender et PlayerRender
		// Ces objets se substituent aux objets initiaux 
		// et réduisent leur nb de méthodes accessibles
		////////////////////////////////////////////////////////////////////////
		
		TUI.section("Test CardRender et PlayerRender");
		
		/* Test CardRender */
		ICard cardRender = new CardRender(c2); /* un objet CardRender enveloppe un objet Card */
		ICard cardRender2 = new CardRender(c2); /* un objet CardRender enveloppe un objet Card */
		TUI.check("c2", c2, "Roi-Carreau");
		TUI.check("cardRender", cardRender, "Roi-Carreau");
		TUI.check("cardRender.getRank()", cardRender.getRank(), "_ROI");
		TUI.check("cardRender.getSuit()", cardRender.getSuit(), "CARREAU");
		TUI.check("cardRender.isRevealed()", cardRender.isRevealed(), "true");
		TUI.check("cardRender.compareTo()", ((Comparable) cardRender).compareTo(cardRender2), "0");
		
		/* Décommentez la ligne suivante et constatez que vous ne pouvez invoquer que des
		 * méthodes définies dans ICard et non pas dans Card sur cet objet CardRender
		 */
		//		System.out.println("cardRender.reveale() : " + cardRender.reveale());	// 
		
		
		/* COMPLETEZ le programme ci-dessous pour tester les autres méthodes de CardRender */
		TUI.note("A completer", "Les autres methodes de CardRender peuvent etre ajoutees ici si besoin.");
		
		/* Test PlayerRender */
		IPlayer playerRender = new PlayerRender(p1);
		TUI.check("p1", p1, "[Joueur1 ** Hand[[2-Carreau]] ** trickPile[[Roi-Carreau, 2-Pique]]]");
		TUI.check("playerRender", playerRender, "Joueur1");
		
		/* COMPLETEZ le programme ci-dessous pour tester les autres méthodes de PlayerRender */
		TUI.note("A completer", "Les autres methodes de PlayerRender peuvent etre ajoutees ici si besoin.");
		
		
		////////////////////////////////////////////////////////////////////////
		// Etudiez et testez les méthodes des classes du package view
		// Il n'y a rien à coder, seulement à comprendre la conception et le code
		// et en particulier les DP Composite et Template Method
		//
		// Les instructions suivantes vous serviront d'inspiration pour 
		// coder les classes du package model.game dans l'atelier 3
		// Inutile de les comprendre toutes maintenant, en particulier 
		// les instructions liées aux Map
		////////////////////////////////////////////////////////////////////////
		
		TUI.section("Simulation Jeu et Test Views");
		TUI.note("Affichage", "La simulation affiche maintenant un rendu TUI sur la console et conserve la GUI Swing.");
		
		
		/* Initialisation du jeu */
		Player player1 = new Player("Joueur1");
		Player player2 = new Player("Joueur2");
		Player trickWinnerPlayer = null;
		player1.addCardToHand(c1);
		player2.addCardToHand(c2);
		
		Map<Player, Card> gamingMatMap = new TreeMap<Player, Card>();
		gamingMatMap.put(player1, c1);
		gamingMatMap.put(player2, c2);
		
		
		/* Evaluation pli */
		ICardsCollection boardToEvaluate = new Board(gamingMatMap.values());
		Card trickWinnerCard = newEvaluator.evaluateTrickWinner(boardToEvaluate);
		
		
		/* Tag vainqueur du pli */
		if(trickWinnerCard!=null) {
			for (Entry<Player, Card> entry : gamingMatMap.entrySet()) {
				if (entry.getValue().equals(trickWinnerCard)) {
					trickWinnerPlayer = entry.getKey();
					trickWinnerPlayer.setTrickWinner(true);
					break;
				}
			}
		}
		
		
		/* Fabrication de la Map que les Views afficheront */
		Map<IPlayer, ICard> gamingMatMapRender = new TreeMap<IPlayer, ICard>();
		for (Entry<Player, Card> entry : gamingMatMap.entrySet()) {
			Player player = entry.getKey();	
			playerRender = new PlayerRender(player);
			cardRender = new CardRender(entry.getValue());
			gamingMatMapRender.put(playerRender, cardRender);
		}
		
			
		/* Affichage du jeu par les Views */
		
		IGameView views = new GameViews(false);
		IGameView commandLineView = new GameConsoleView(true);
		IGameView gameSwingView = new GameSwingView(false);
		views.addViewable(commandLineView);
		views.addViewable(gameSwingView);
		views.showGamingMatAndTrickWinner(gamingMatMapRender);
		
		
		//////////////////////////////////////////////////////////////////////////////////
		// Après toutes ces évolutions, effectuez un test de non régresion
		// pour vérifier  que ce qui a fonctionné tout au long du TP fonctionne toujours
		// Vérifiez également la trace d'exécution du TestAtelier1 
		// re-activez au préalable "isfaceUp=false" dans la classe Card
		//
		// Si tout est OK, Bravo !
		//////////////////////////////////////////////////////////////////////////////////
	}
	
	

}


