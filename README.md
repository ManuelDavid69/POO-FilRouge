# POO Fil Rouge — Le jeu de la Bataille (Java)

![Java CI](https://github.com/ManuelDavid69/POO-FilRouge/actions/workflows/maven.yml/badge.svg)
![Java 25](https://img.shields.io/badge/Java-25-orange)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-blue)

Projet du cours de **Programmation Orientée Objet** (CPE Lyon) : implémentation complète du jeu de la **Bataille** en Java, en architecture **MVC**, avec une progression en trois ateliers et une mise en pratique de plusieurs design patterns.

> Squelette initial fourni par Françoise Perrin (CPE Lyon). Les ateliers 1 à 3, les tests et la documentation ont été réalisés dans ce dépôt.

---

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Règles implémentées](#règles-implémentées)
- [Choix techniques et points d'attention](#choix-techniques-et-points-dattention)
- [Tests](#tests)
- [Compiler et exécuter](#compiler-et-exécuter)
- [Structure du dépôt](#structure-du-dépôt)
- [Les trois ateliers](#les-trois-ateliers)
- [Crédits](#crédits)

---

## Fonctionnalités

- Jeu de cartes **32 ou 52 cartes** généré depuis les énumérations `Rank` et `Suit`.
- Deux variantes de la Bataille :
  - **classique** : seule la valeur (rang) départage les cartes ;
  - **nouvelle** : la valeur puis la couleur départagent les cartes.
- Gestion complète d'un pli : pose des cartes, évaluation, bataille en cas d'égalité, ramassage du pot.
- Distribution réelle des cartes, recyclage de la pile de plis quand la main est vide, détection du vainqueur.
- Deux vues : **console** (TUI ASCII) et **Swing**, agrégées par un Composite.
- Objets de rendu en lecture seule (`CardRender`, `PlayerRender`) exposés aux vues.
- **60 tests JUnit** couvrant le modèle, les évaluateurs, les rendus et le moteur de jeu.

---

## Architecture

Le projet applique le découpage **Model – View – Controller** :

```
launcher.GameLauncher
        │
        ▼
controller.GameController  ──────────────►  allShared.IGameView
        │                                   (GameViews : Composite)
        │ crée via GameFactory                      │
        ▼                                           ├── view.GameConsoleView
model.games.AbstractGame  ◄── IGame                 └── view.GameSwingView
        │
        ├── model.player.Player
        │       ├── model.cards.Hand
        │       └── model.cards.Hand (trickPile)
        ├── model.cards.Deck / Board
        └── model.games.IGameEvaluator (Strategy)
```

Le Controller ne connaît que les interfaces `IGame` et `IGameView`. Le Model ne connaît pas les vues : il leur transmet uniquement des proxies en lecture seule.

### Design patterns utilisés

| Pattern | Classes | Rôle |
|---|---|---|
| **MVC / Mediator** | `GameController` | Orchestre les échanges entre le Model et les Views sans qu'ils se connaissent. |
| **Facade** | `AbstractGame`, `Player`, `Hand` | Chaque niveau expose une façade simple au niveau supérieur. |
| **Factory** | `GameFactory`, `CardsCollectionFactory` | Centralise la création des jeux et des collections de cartes. |
| **Strategy** | `IGameEvaluator`, `ClassicWarGameEvaluator`, `NewWarGameEvaluator`, `Comparator<Card>` | Rend la comparaison des cartes interchangeable selon la variante. |
| **Template Method** | `AbstractGameEvaluator`, `AbstractGame`, `AbstractGameView` | Squelette d'algorithme fixe, étapes variables déléguées aux sous-classes. |
| **Composite** | `GameViews` | Le Controller manipule une seule vue qui propage aux vues réelles. |
| **Proxy / Protection** | `CardRender`, `PlayerRender` | La vue ne voit que des getters, jamais les setters du Model. |
| **Adapter** | `AbstractCardsCollection` | Adapte une `List<Card>` à l'interface `ICardsCollection`. |
| **Iterator** | `ICardsCollection extends Iterable<Card>` | Parcours uniforme des mains, du paquet et du tapis. |

---

## Règles implémentées

1. Le paquet est **mélangé** puis distribué **une carte à la fois**, en tournante entre les joueurs.
2. À chaque tour, chaque joueur actif joue une carte (par défaut celle du dessus de sa main).
3. Le pli est évalué **sur les cartes du tour courant uniquement**. Si le meilleur rang est partagé (Bataille classique), ou la meilleure carte apparaît plusieurs fois, il y a **bataille** : personne ne remporte le pli.
4. Lors d'une bataille, les cartes restent sur le tapis (`Board`) et les joueurs rejouent. Le pot s'accumule.
5. Dès qu'un joueur remporte le pli, il ramasse **toutes** les cartes du tapis dans sa `trickPile`.
6. Quand la main d'un joueur est vide, sa `trickPile` est mélangée et redevient sa main.
7. La partie se termine quand un joueur détient **toutes les cartes du jeu** (main + pile de plis) ; il est alors déclaré vainqueur.

---

## Choix techniques et points d'attention

### 1. Évaluer le tour courant, pas tout le tapis

Le Javadoc du squelette est explicite : *« Elle est stockée dans le gamingMatMap qui sera évalué pour déterminer le gagnant du pli ; puis dans le Board qui contient toutes les cartes jouées lors d'un tour de jeu. Si "Bataille", nb cartes Board > nb de cartes gamingMatMap. »*

`evaluateTrickWinner()` construit donc une collection temporaire à partir de `gamingMatMap.values()` et l'envoie à l'évaluateur. Évaluer le `Board` complet ferait gagner une carte d'un tour précédent lors d'une bataille.

### 2. `Card` : égalité et ordre naturel

- `equals()` / `hashCode()` reposent sur le **rang et la couleur**, indépendamment de l'état face visible/cachée.
- `compareTo()` ne compare que le **rang** : `2-Carreau` et `2-Pique` sont donc égaux au sens de l'ordre naturel, ce qui permet à la Bataille classique de détecter les égalités.
- La couleur n'intervient que dans `NewWarGameCardComparator`.

### 3. Cartes cachées par défaut

Une carte nouvellement créée est **face cachée** (`?-?`), conformément à `CardTest`. Les programmes de démonstration (`TestAtelier1`, `TestAtelier2`) révèlent explicitement les cartes dont ils veulent afficher la valeur. C'est préférable à un changement du défaut de production, qui casserait le test d'obfuscation.

### 4. Fin de partie avec `hasWonAllCards`

`AbstractWarGame.isGameEnd()` utilise `player.hasWonAllCards(initDeckSize)`, c'est-à-dire `main + trickPile == taille du jeu initial`. Compter simplement les joueurs encore actifs serait faux pendant une bataille : des cartes sont encore sur le tapis et n'appartiennent à personne.

### 5. `Collections.max` et les égalités

`Collections.max` renvoie le **premier** maximum rencontré. C'est pourquoi `hand.max()` sur `[[2-Carreau, 2-Pique]]` renvoie `2-Carreau`, alors que `hand.max(new NewWarGameCardComparator())` renvoie `2-Pique`.

### 6. Rendu protecteur

`PlayerRender.toString()` renvoie le **nom** du joueur, pas l'état complet de sa main. Une vue n'a pas besoin de connaître les cartes de l'adversaire, et le proxy évite toute fuite d'information ou modification accidentelle du Model.

### 7. Constructeur par copie polymorphe

`AbstractCardsCollection(ICardsCollection)` permet de construire un `Hand`, un `Deck` ou un `Board` à partir de n'importe quelle autre collection. Il s'appuie sur le champ protégé `cards` et suppose donc une instance d'`AbstractCardsCollection` — c'est le cas de toutes les collections du projet.

### 8. Corrections apportées au squelette

- `pom.xml` : `1.25` n'est pas une version de langage valide pour `javac` ; remplacé par `<maven.compiler.release>25</maven.compiler.release>` et plugins de compilation/tests mis à jour.
- `CardTest` : l'assertion après `hide()` vérifiait `isRevealed() == true` au lieu de `false` ; corrigée.
- `TestAtelier1` : un appel `revealeCard(0)` manquait et plusieurs traces attendues étaient obsolètes ; réalignées sur l'implémentation.
- `TestAtelier2` : les cartes sont révélées explicitement et les attentes dépendant d'un ordre aléatoire (mélange, `Collections.max` après `shuffle`) ont été rendues déterministes.

---

## Tests

```bash
mvn clean test
```

**60 tests**, répartis ainsi :

| Fichier | Couverture |
|---|---|
| `CardTest` | État caché/révélé, `toString`, `equals`/`hashCode`. |
| `HandTest` | Ajout, suppression, bornes, `playCard`, révélation, itérateur. |
| `PlayerTest` | Main/pile de plis, indicateurs, `hasWonAllCards`, `compareTo`, recyclage. |
| `DeckTest` | Jeux de 32/52 cartes, contenu, tri, maximum, constructeurs par copie. |
| `CardsCollectionTest` | Comportement commun des collections, tri naturel/comparateur, `max`, `shuffle`. |
| `GameEvaluatorTest` | Évaluateurs classique et nouvelle règle, égalités et bataille. |
| `CardRenderTest` / `PlayerRenderTest` | Délégation des proxies, égalité, comparaison. |
| `GameTest` | Distribution, pli, bataille, pot, recyclage, rendu, fin de partie. |

---

## Compiler et exécuter

Prérequis : **JDK 25** et **Maven 3.9+**.

```bash
# Compiler et lancer tous les tests
mvn clean test

# Lancer le jeu (vues console + Swing)
mvn exec:java -Dexec.mainClass=launcher.GameLauncher

# Rejouer les traces des ateliers
mvn exec:java -Dexec.mainClass=launcher.TestAtelier1
mvn exec:java -Dexec.mainClass=launcher.TestAtelier2
```

> Sous PowerShell, mettre la propriété entre guillemets :
> `mvn exec:java "-Dexec.mainClass=launcher.GameLauncher"`.

Sans Maven, après `mvn package` :

```bash
java -cp target/classes launcher.GameLauncher
```

La vue Swing reste ouverte à la fin de la partie : fermez la fenêtre pour terminer le processus.

---

## Structure du dépôt

```
src/main/java/
├── allShared/               Interfaces partagées (IGame, ICard, IPlayer, IGameView…)
├── model/
│   ├── cards/               Card, Rank, Suit, Deck, Hand, Board, comparateur, rendus
│   ├── player/              Player, PlayerRender
│   └── games/               AbstractGame, AbstractWarGame, évaluateurs, variantes
├── view/                    ConsoleTui, GameConsoleView, GameSwingView, GameViews
├── controller/              GameController, GameFactory, CardsCollectionFactory
└── launcher/                GameLauncher, TestAtelier1, TestAtelier2

src/test/java/
├── model/cards/             CardTest, HandTest, DeckTest, CardsCollectionTest, CardRenderTest
├── model/player/            PlayerTest, PlayerRenderTest
└── model/games/             GameEvaluatorTest, GameTest

docs/                        Détail pédagogique des trois ateliers
```

---

## Les trois ateliers

| Atelier | Contenu | Documentation |
|---|---|---|
| **1 — Bases du Model** | `Card`, `Hand`, `Player` : état, collections, égalité, comparaison. | [docs/ATELIER1.md](docs/ATELIER1.md) |
| **2 — Factorisation et stratégies** | `AbstractCardsCollection`, `Deck`, `Board`, `Comparator`, évaluateurs, proxies de rendu, itérateur. | [docs/ATELIER2.md](docs/ATELIER2.md) |
| **3 — Moteur de jeu** | Distribution, `PlayCards`, évaluation des plis, ramassage du pot, fin de partie, intégration MVC. | [docs/ATELIER3.md](docs/ATELIER3.md) |

---

## Crédits

- **Squelette, énoncé et interfaces** : Françoise Perrin, CPE Lyon (`cpe-lyon/POO-FilRouge`, branche `students`).
- **Implémentation des ateliers, tests et documentation** : [ManuelDavid69](https://github.com/ManuelDavid69).
