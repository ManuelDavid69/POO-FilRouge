# Atelier 3 — Le moteur de jeu

## Objectif

Assembler les briques des ateliers 1 et 2 pour faire fonctionner une partie complète :

- distribuer réellement le paquet ;
- jouer un tour, évaluer le pli, gérer les batailles ;
- ramasser le pot et détecter la fin de partie ;
- exposer au Controller et aux vues des données protégées.

---

## Séquence d'une partie

```
GameController.run()
├── view.getGameType() / getPlayersName() / getDeckType()
├── GameFactory.getGame(...)
│
└── tant que la partie n'est pas finie
    ├── tant que le pli n'est pas gagné et que la partie continue
    │   ├── game.PlayCards(view.chooseCardsToPlay())
    │   ├── game.evaluateTrickWinner()
    │   ├── game.getGamingMatRender()
    │   └── views.showGamingMatAndTrickWinner(...)
    ├── game.theWinnerTakesItAll()
    └── game.isGameEnd()
└── views.showWinner(game.theWinnerIs())
```

---

## 1. Distribution — `AbstractWarGame.dealCardsFromDeck`

```java
this.deck.shuffle();
for (int round = 0; round < nbCards; round++) {
    for (Player player : this.players) {
        Card card = this.deck.removeTopCard();
        if (card != null) player.addCardToHand(card);
    }
}
```

Le constructeur d'`AbstractGame` a été nettoyé : les quatre cartes codées en dur pour les tests ont été remplacées par la distribution réelle.

```java
this.initDeckSize = this.deck.size();
this.dealCardsFromDeck(this.initDeckSize / this.players.size());
```

---

## 2. `PlayCards(Map<String, Integer>)`

Pour chaque joueur :

1. si sa main est vide mais que sa pile de plis ne l'est pas, il **recycle** ses cartes gagnées ;
2. s'il n'a plus aucune carte, il ne joue pas ce tour ;
3. sinon, il joue l'index demandé par la vue, ou la carte du dessus par défaut ;
4. la carte est stockée dans `gamingMatMap` (qui a joué quoi) et ajoutée au `Board` (le pot).

```java
this.gamingMatMap.clear();

for (Player player : this.players) {
    if (player.isHandEmpty() && !player.isTrickPileEmpty()) {
        player.addWonCardsBackToHand();
    }
    if (!player.isStillActive()) continue;

    Card playedCard = player.playCard(cardIndex);
    if (playedCard == null && cardIndex != 0) playedCard = player.playCard(0);

    if (playedCard != null) {
        this.gamingMatMap.put(player, playedCard);
        this.board.addCard(playedCard);
    }
}
```

`gamingMatMap` représente **le tour courant** ; `board` représente **tout le pli**, batailles comprises.

---

## 3. `evaluateTrickWinner()`

```java
for (Player player : this.players) player.setTrickWinner(false);

ICardsCollection currentTrick = new Board();
for (Card card : this.gamingMatMap.values()) {
    if (card != null) currentTrick.addCard(card);
}

Card winnerCard = this.getGameEvaluator().evaluateTrickWinner(currentTrick);
...
```

### Pourquoi évaluer `gamingMatMap` et non `board` ?

Lors d'une bataille, le `Board` contient les cartes des tours précédents. Si on l'évaluait en entier, une carte déjà posée pourrait remporter le nouveau pli, ce qui est contraire aux règles. Le Javadoc du squelette confirme d'ailleurs que c'est le `gamingMatMap` qui « sera évalué pour déterminer le gagnant du pli ».

L'évaluateur renvoie `null` en cas d'égalité : `evaluateTrickWinner()` retourne alors `false`, et la boucle du Controller enchaîne un nouveau tour. Le tapis conserve les cartes : c'est le pot.

---

## 4. `theWinnerTakesItAll()`

Le joueur marqué `isTrickWinner` récupère **toutes** les cartes du `Board` dans sa `trickPile`, puis le tapis est vidé et les indicateurs de pli sont réinitialisés.

```java
Player winner = ...; // joueur dont isTrickWinner() est vrai
for (Card card : this.board) winner.addCardToTrickPile(card);
this.board.clear();
for (Player player : this.players) player.setTrickWinner(false);
```

Les cartes ne retournent pas immédiatement dans la main : elles restent dans la pile de plis jusqu'à ce que la main soit vide.

---

## 5. `getGamingMatRender()` et `theWinnerIs()`

```java
gamingMatMapRender.put(new PlayerRender(player), new CardRender(card));
```

La vue reçoit une `Map<IPlayer, ICard>` triée par nom de joueur, contenant uniquement des proxies. Aucune référence directe au Model n'est exposée.

`theWinnerIs()` renvoie de la même façon un `PlayerRender` du joueur marqué `isGameWinner`, ou `null`.

---

## 6. Fin de partie — `AbstractWarGame.isGameEnd()`

```java
for (Player player : this.players) {
    if (player.hasWonAllCards(this.initDeckSize)) {
        player.setGameWinner(true);
        return true;
    }
}
return false;
```

Un joueur a gagné lorsqu'il détient **toutes** les cartes du jeu. Cette condition est plus fiable qu'un simple comptage de joueurs actifs : pendant une bataille, des cartes sont sur le tapis et n'appartiennent encore à personne.

---

## 7. Exemple de bataille

Paquet scripté : `[2-Carreau, 2-Pique, 7-Coeur, Roi-Trefle]`, deux joueurs, distribution sans mélange.

| Tour | Alice | Bob | Résultat |
|---|---|---|---|
| 1 | `2-Carreau` | `2-Pique` | égalité de rang → bataille, pot = 2 cartes |
| 2 | `7-Coeur` | `Roi-Trefle` | Bob gagne, pot = 4 cartes |
| — | — | — | Bob ramasse les 4 cartes ; il détient tout le paquet → fin |

Ce scénario est vérifié par `GameTest.battleShouldKeepThePotUntilTheTrickIsWon`.

---

## 8. Validation

```bash
./mvnw clean test
./mvnw exec:java -Dexec.mainClass=launcher.GameLauncher
```

- `GameTest` couvre la distribution, la pose, l'évaluation, la bataille, le ramassage, le recyclage et la fin de partie.
- Une simulation headless de 32 cartes se termine bien :

```
Partie terminee : true
Plis gagnes    : 114
Tours joues    : 126
Vainqueur      : Alice
```

Le nombre de plis dépend du mélange : seule la terminaison et la désignation d'un vainqueur sont garanties par les tests.

À l'issue de l'atelier 3, **60 tests** passent.
