# Atelier 1 — Les bases du Model : `Card`, `Hand`, `Player`

## Objectif

Poser les briques élémentaires du jeu : une carte, une main de cartes et un joueur.  
À la fin de cet atelier, `TestAtelier1` et `CardTest` doivent produire les traces attendues.

---

## 1. `model.cards.Card`

Une carte possède un **rang** (`Rank`), une **couleur** (`Suit`) et un état **face visible / face cachée**.

| Élément | Implémentation |
|---|---|
| `Card(Rank, Suit)` | Initialise `isFaceUp = false` : une carte est cachée par défaut. |
| `getRank()` / `getSuit()` | Retournent le rang et la couleur. |
| `isRevealed()` | Retourne `isFaceUp`. |
| `reveale()` / `hide()` | Passent la carte en face visible / face cachée. |
| `toString()` | `?-?` si cachée, `Valeur-Couleur` sinon (ex. `Roi-Carreau`). |
| `equals()` / `hashCode()` | Basés sur le rang **et** la couleur ; l'état visible n'entre pas en compte. |
| `compareTo()` | Compare uniquement les rangs : `this.rank.getRank() - other.rank.getRank()`. |

Conséquences :

- `new Card(_2, CARREAU).equals(new Card(_2, CARREAU))` vaut `true`, même si l'une est révélée et l'autre non.
- `new Card(_2, CARREAU).compareTo(new Card(_2, PIQUE))` vaut `0` : les deux cartes sont à égalité pour la Bataille classique. C'est cette égalité qui déclenche une bataille.

### Pourquoi ne pas révéler par défaut ?

`CardTest` vérifie explicitement qu'une carte nouvellement créée s'affiche `?-?`. Le TP demande par ailleurs, dans `TestAtelier2`, de rendre les cartes visibles pour lire la trace. La bonne solution est de **révéler les cartes dans les programmes de démonstration**, pas de changer le comportement de production.

---

## 2. `model.cards.Hand`

`Hand` encapsule une `List<Card>` (`ArrayList`) et implémente `ICardsCollection`.

| Méthode | Comportement |
|---|---|
| `addCard(Card)` | Ajoute la carte si elle n'est pas `null`. |
| `removeTopCard()` | Retire et retourne la carte d'index `0`, ou `null` si la main est vide. |
| `removeCard(int)` | Retire la carte à l'index donné, ou `null` si l'index est hors bornes. |
| `isEmpty()` / `size()` / `clear()` | Délégation à la liste. |
| `toString()` | `"[" + cards + "]"`, soit `[[2-Carreau, Roi-Carreau]]`. |
| `playCard(int)` | Retire la carte, la **révèle** et la retourne ; `null` si l'index est invalide. |
| `revealeCard(int)` | Révèle la carte et retourne `true` ; `false` si l'index est invalide. |
| `hideCard(int)` | Cache la carte et retourne `true` ; `false` si l'index est invalide. |

Toutes les opérations vérifient les bornes : un index négatif ou supérieur à `size() - 1` ne lève pas d'exception, il renvoie `null` ou `false`, conformément aux traces du TP.

---

## 3. `model.player.Player`

Un joueur possède un nom, une **main** (`Hand`) et une **pile de plis gagnés** (`Hand` utilisée comme `trickPile`).

| Méthode | Comportement |
|---|---|
| `addCardToHand` / `addCardToTrickPile` | Délèguent aux deux mains. |
| `playCard(int)` | Joue une carte de la main (révélée et retirée). |
| `removeCardFromHand` / `removeCardFromTrickPile` | Retirent une carte de la pile correspondante. |
| `revealeCard` / `hideCard` | Délèguent à la main. |
| `isHandEmpty` / `isTrickPileEmpty` | Délèguent aux piles. |
| `isStillActive()` | `!isHandEmpty()` : un joueur peut jouer tant qu'il a des cartes en main. |
| `hasWonAllCards(int deckSize)` | `hand.size() + trickPile.size() == deckSize`. |
| `isTrickWinner` / `setTrickWinner` | Indicateur du vainqueur du pli courant. |
| `isGameWinner` / `setGameWinner` | Indicateur du vainqueur de la partie. |
| `compareTo(IPlayer)` | Ordre alphabétique des noms. |
| `equals` / `hashCode` | Basés sur le nom, cohérents avec `compareTo` (indispensable dans une `TreeMap`). |

`Player` est une façade : il ne réimplémente pas la logique des collections, il la délègue à `Hand`.

### Question du carnet de bord : `isStillActive()` et `isHandEmpty()`

Les deux méthodes ne sont pas redondantes, même si l'implémentation actuelle les relie :

- `isHandEmpty()` est une question **structurelle** posée à l'objet concret `Player` : « ta main est-elle vide ? ».
- `isStillActive()` appartient au contrat `IPlayer` : c'est une question **métier** dont dépend le moteur de jeu. Sa définition pourrait évoluer (par exemple, un joueur dont la pile de plis n'est pas vide reste actif dans certaines variantes) sans que `AbstractWarGame` ait à changer. Le moteur ne dépend que de l'interface.

---

## 4. Validation

```bash
./mvnw exec:java -Dexec.mainClass=launcher.TestAtelier1
./mvnw test
```

Extrait de trace attendu :

```
c1 : ?-?
c1 : 2-Carreau
c1.isRevealed() : true
c1 après hide() : ?-?
c1.isRevealed : false
c1.compareTo(c2) : -11
```

À l'issue de l'atelier 1, les tests `CardTest`, `HandTest` et `PlayerTest` passent (24 tests).
