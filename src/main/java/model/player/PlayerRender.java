package model.player;

import allShared.IPlayer;

/**
 * Cette classe est un Proxy ou un Adapter [Design Pattern] de la classe Player
 * Ses instances sont utilisées par la View qui
 * n'a accès qu'aux "Getters" de l'objet encapsulé
 * 
 * Cette classe restreint donc le nombre de fonctionnalités existantes de l'objet enveloppé
 * et lui délègue le soin de réaliser les traitements
 * 
 * @author francoise.perrin
 *
 */
public class PlayerRender implements IPlayer, Comparable<IPlayer> {

	private final IPlayer player;
	
	public PlayerRender(IPlayer player) {
		this.player = player;
	}
	
	@Override
	public final String getName() {
		return this.player.getName();
	}
	
	@Override
	public final boolean isStillActive() {
		return this.player.isStillActive();
	}

	@Override
	public final boolean isTrickWinner() {
		return this.player.isTrickWinner();
	}
	

	@Override
	public boolean isGameWinner() {
		return this.player.isGameWinner();
	}

	/**
	 * Le Render ne divulgue que le nom du joueur : la View n'a pas besoin
	 * de connaître le détail de sa main ou de sa pile de plis.
	 */
	@Override
	public String toString() {
		return this.player.getName();
	}

	@Override
	public int hashCode() {
		return this.player.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerRender other = (PlayerRender) obj;
		return this.player.equals(other.player);
	}

	@Override
	public int compareTo(IPlayer o) {
		return this.player.compareTo(o);
	}


}
