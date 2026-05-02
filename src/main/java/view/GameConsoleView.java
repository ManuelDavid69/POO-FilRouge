package view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import allShared.CardsCollectionType;
import allShared.GameType;
import allShared.ICard;
import allShared.IGameView;
import allShared.IPlayer;

/**
 * View en mode Console
 * 
 * @author francoise.perrin
 */
public class GameConsoleView extends AbstractGameView implements IGameView {

	public GameConsoleView(boolean isMasterView) {
		super(isMasterView);
	}

	
	protected void createAndShowView() {
		System.out.println(ConsoleTui.renderBanner("Jeu de " + this.gameType.getName(),
				"Affichage console du plateau et du vainqueur du pli"));
	}
	
	protected void display(StringBuilder text) {
		System.out.println(text);
	}

	@Override
	public void showGamingMatAndTrickWinner(Map<IPlayer, ICard> gamingMatRender) {
		List<String> lines = new ArrayList<String>();
		IPlayer trickWinner = null;
		ICard trickWinnerCard = null;

		lines.add(formatRow("Joueur", "Carte", "Statut"));
		lines.add(formatSeparator());

		for (Entry<IPlayer, ICard> entry : gamingMatRender.entrySet()) {
			IPlayer player = entry.getKey();
			ICard card = entry.getValue();
			String status = player.isTrickWinner() ? "remporte le pli" : "en lice";
			lines.add(formatRow(player.getName(), String.valueOf(card), status));

			if (player.isTrickWinner()) {
				trickWinner = player;
				trickWinnerCard = card;
			}
		}

		if (trickWinner != null) {
			lines.add("");
			lines.add("Vainqueur du pli : " + trickWinner.getName() + " avec " + trickWinnerCard);
		} else {
			lines.add("");
			lines.add("Vainqueur du pli : aucun pour ce tour");
		}

		System.out.println(ConsoleTui.renderPanel("Tour de jeu", lines));
	}

	@Override
	public void showWinner(IPlayer winner) {
		List<String> lines = new ArrayList<String>();
		lines.add(winner != null ? "Le gagnant est : " + winner.getName()
				: "Aucun gagnant");
		System.out.println(ConsoleTui.renderPanel("Fin de partie", lines));
	}
	
	/**
	 * En l'absence de décision par la View, le Model décidera des cartes à jouer
	 * [Il devrait être demandé aux utilisateurs...]
	 */
	@Override
	public Map<String, Integer> chooseCardsToPlay(){
		return null;
	}

	/**
	 * le type de Deck est sélectionné par la View 
	 * [Il devrait être demandé aux utilisateurs...]
	 * puis le Deck sera fabriqué par le controller
	 */
	@Override
	public CardsCollectionType getDeckType() {
		CardsCollectionType deckType = CardsCollectionType.DECK32;
		return deckType;
	}

	/**
	 * @param playerNumber
	 * @return liste noms joueurs
	 */
	@Override
	public List<String> getPlayersName() {
	
		List<String> playersNames = new LinkedList<String>();
		
		int playerNumber = 2;
		for(int i=1; i<=playerNumber;i++){
			playersNames.add("Joueur"+i);
		}
		return playersNames;
	}

	@Override
	public void addViewable(IGameView gameView) {
		// Unsupported Method
	}

	
	/*
	 *  Méthode qui demande aux utilisateurs (joueurs) le type de jeu
	 *  [Ici codé en dur dans GameConsoleView]
	 */
	protected GameType chooseGameType() {
		return GameType.WARGAME_CLASSIC;
	}

	private String formatRow(String player, String card, String status) {
		return pad(player, 18) + " | " + pad(card, 18) + " | " + pad(status, 46);
	}

	private String formatSeparator() {
		return "-------------------+--------------------+-----------------------------------------------";
	}

	private String pad(String value, int width) {
		String safeValue = value == null ? "-" : value;
		if (safeValue.length() > width) {
			return safeValue.substring(0, Math.max(0, width - 3)) + "...";
		}
		StringBuilder builder = new StringBuilder(safeValue);
		while (builder.length() < width) {
			builder.append(' ');
		}
		return builder.toString();
	}
}
