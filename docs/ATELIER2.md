# Atelier 2 — Factorisation, stratégies et rendus

## Objectif

Passer d'un code qui fonctionne à un code **factorisé et extensible** :

- mutualiser le comportement des collections de cartes ;
- générer un paquet de 32 ou 52 cartes ;
- introduire des stratégies de comparaison ;
- implémenter les évaluateurs de pli ;
- protéger le Model derrière des objets de rendu ;
- utiliser l'itérateur et maîtriser `equals`/`hashCode`.

---

## 1. `AbstractCardsCollection`

`Hand`, `Deck` et `Board` partageaient les mêmes méthodes. Elles sont désormais dans une classe mère abstraite qui adapte une `List<Card>` à l'interface `ICardsCollection`.

### Attribut et constructeurs

```java
protected final List<Card> cards;

public AbstractCardsCollection()                                  // liste vide
public AbstractCardsCollection(Collection<Card> collection)       // copie
public AbstractCardsCollection(ICardsCollection iCardsCollection) // copie polymorphe
```

Le constructeur par copie permet d'écrire `new Hand(board)`, `new Deck(hand)` ou `new Board(deck)` : n'importe quelle collection peut en copier une autre.

### Comportement mutualisé

| Méthode | Détail |
|---|---|
| `addCard`, `removeTopCard`, `removeCard`, `clear` | Vérification des bornes et des `null`. |
| `size`, `isEmpty` | Délégation à la liste. |
| `sort()` | Tri naturel (`Collections.sort`) : par rang uniquement. |
| `sort(Comparator<Card>)` | Tri selon une stratégie externe. |
| `shuffle()` | `Collections.shuffle`. |
| `max()` / `max(Comparator)` | `Collections.max`, avec retour `null` si la collection est vide. |
| `iterator()` | Itérateur de la liste interne. |
| `toString()` | `[[...]]`. |

### Sous-classes

| Classe | Spécificité |
|---|---|
| `Hand` | Conserve `playCard`, `revealeCard`, `hideCard`. |
| `Deck` | Sait se fabriquer lui-même à partir d'une taille. |
| `Board` | Aucun comportement propre : sa raison d'être est **sémantique** (c'est le tapis de jeu, pas une liste quelconque). |

### Question du carnet de bord

> « Qu'est-ce qui justifie l'existence de cette classe, au-delà de la factorisation du code commun ? »

Trois éléments, qui ne relèvent pas de la simple économie de lignes :

1. **Un type commun.** Les clients manipulent `ICardsCollection` / `AbstractCardsCollection`, jamais une `List<Card>` nue. Le jour où l'implémentation interne change (liste chaînée, tableau, collection immuable), aucun client ne change.
2. **Un adaptateur.** La classe enveloppe une `List` et contrôle les invariants du domaine : refus des `null`, bornes des index, comportement cohérent de `max()` sur une collection vide. C'est le pattern Adapter.
3. **La substituabilité.** Toutes les collections partagent le même contrat, ce qui rend possible le constructeur par copie polymorphe (`new Hand(board)`, `new Deck(hand)`, etc.) et le passage indifférencié d'un `Deck`, d'un `Hand` ou d'un `Board` à une méthode qui attend un `ICardsCollection`.

---

## 2. `Deck(int deckSize)`

Le paquet est construit à partir des énumérations, rang par rang puis couleur par couleur :

```java
Rank[] ranks = Rank.values();
int firstRank = (deckSize == 32) ? 5 : 0; // _7 est à l'index 5

for (int i = firstRank; i < ranks.length; i++) {
    for (Suit suit : Suit.values()) {
        this.addCard(new Card(ranks[i], suit));
    }
}
```

- `32` : du `_7` à l'`_AS` (4 × 8 = 32 cartes).
- `52` : du `_2` à l'`_AS` (4 × 13 = 52 cartes).
- Toute autre taille lève une `IllegalArgumentException`.

L'ordre obtenu est celui attendu par le TP : `7-Carreau, 7-Coeur, 7-Pique, 7-Trefle, 8-Carreau, …`.

---

## 3. `NewWarGameCardComparator`

La variante « nouvelle bataille » départage les égalités de rang par la couleur :

```java
int diff = card1.getRank().getRank() - card2.getRank().getRank();
if (diff == 0) {
    diff = card1.getSuit().value() - card2.getSuit().value();
}
return diff;
```

Ainsi `2-Carreau` et `2-Pique` sont égaux pour `compareTo`, mais `NewWarGameCardComparator` place `2-Pique` au-dessus de `2-Carreau` (Pique = 3, Carreau = 1).

---

## 4. Évaluateurs de pli

### Template Method

`AbstractGameEvaluator.evaluateTrickWinner` définit l'algorithme :

1. trouver la carte maximale avec la stratégie de la sous-classe ;
2. compter combien de cartes sont égales à ce maximum ;
3. si le maximum est unique, c'est la carte gagnante ; sinon, il y a bataille et la méthode renvoie `null`.

```java
Card maxCard = this.max(gamingMat);
if (maxCard == null) return null;

int frequency = 0;
for (Card card : gamingMat) {
    if (this.comparaison(card, maxCard) == 0) frequency++;
}
return (frequency == 1) ? maxCard : null;
```

Le garde `maxCard == null` évite un `NullPointerException` sur un pli vide.

### Stratégies concrètes

| Évaluateur | `max` | `comparaison` |
|---|---|---|
| `ClassicWarGameEvaluator` | boucle avec `compareTo` | `card.compareTo(maxCard)` |
| `NewWarGameEvaluator` | boucle avec `NewWarGameCardComparator` | `comparator.compare(card, maxCard)` |

### Exemple

| Collection | Classique | Nouvelle |
|---|---|---|
| `[2-Carreau, 2-Pique, 7-Carreau, Roi-Pique]` | `Roi-Pique` | `Roi-Pique` |
| `[2-Carreau, 2-Pique]` | `null` (bataille) | `2-Pique` |

### Rôle des classes `ClassicWarGame` et `NewWarGame`

`ClassicWarGame` et `NewWarGame` n'ajoutent aucune règle : elles **fixent la stratégie** d'évaluation en renvoyant l'évaluateur approprié. C'est le point d'assemblage du pattern Strategy. On pourrait les remplacer par une factory d'évaluateurs, mais leur existence rend le choix de la variante explicite dans `GameFactory` et donne un type de jeu concret au Controller.

---

## 5. Rendus : `CardRender` et `PlayerRender`

Les vues ne doivent jamais manipuler directement le Model. Deux proxies en lecture seule sont créés à la frontière Model/View.

### `CardRender`

- enveloppe une `Card` ;
- délègue `getRank`, `getSuit`, `isRevealed`, `compareTo`, `equals`, `hashCode`, `toString` ;
- n'expose **ni** `reveale()` **ni** `hide()`.

### `PlayerRender`

- enveloppe un `IPlayer` ;
- délègue `getName`, `isStillActive`, `isTrickWinner`, `isGameWinner`, `compareTo`, `equals`, `hashCode` ;
- `toString()` renvoie le **nom** du joueur, pour ne pas divulguer sa main à la vue.

Ces objets restent des vues **vivantes** du Model : si la carte sous-jacente est révélée, le `CardRender` reflète immédiatement le changement, sans permettre de le provoquer.

---

## 6. `Player.addWonCardsBackToHand()`

Les cartes gagnées quittent la `trickPile`, rejoignent la main, puis la main est mélangée :

```java
Iterator<Card> iterator = this.trickPile.iterator();
while (iterator.hasNext()) {
    this.hand.addCard(iterator.next());
}
this.trickPile.clear();
this.hand.shuffle();
```

La version `for (Card card : this.trickPile)` (itérateur implicite) produit exactement le même résultat. La version explicite illustre le design pattern Iterator.

---

## 7. Validation

```bash
./mvnw exec:java -Dexec.mainClass=launcher.TestAtelier2
./mvnw test
```

Points de contrôle de la trace :

- `c1.compareTo(c2)` et `comparator.compare(c1, c2)` valent `-11` ;
- `c3.compareTo(c4)` vaut `0`, mais `comparator.compare(c3, c4)` vaut `-2` ;
- le tri naturel regroupe par rang, le tri avec comparateur départage par couleur ;
- `classicEvaluator` renvoie `null` sur `[2-Carreau, 2-Pique]`, `newEvaluator` renvoie `2-Pique` ;
- `playerRender` affiche `Joueur1` (le nom seul).

À l'issue de l'atelier 2, 52 tests passent.
