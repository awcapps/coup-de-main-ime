# CONTEXTE.md - Coup de Main

## 🎯 Pourquoi ce projet ?

### Le Problème

Un jeune joueur d'échecs dans une équipe interclubs fait face à un défi particulier :

- Il a des **difficultés motrices** pour tenir un stylo et écrire
- Lors des compétitions d'échecs, il **doit noter ses coups** (obligation réglementaire)
- Il a le **droit d'utiliser une tablette** pour cette notation
- Les claviers standards (AZERTY/QWERTY) ne sont **pas adaptés** à la notation d'échecs

### Le Besoin

Créer une **application Android avec clavier personnalisé** qui permet au joueur de :

1. **Noter ses parties d'échecs** facilement via un clavier tactile simplifié
2. **Basculer vers ce clavier** depuis n'importe quelle application (Word, Google Docs, etc.)
3. **Utiliser de gros boutons** adaptés aux difficultés motrices
4. **Personnaliser l'interface** selon ses besoins (taille, couleurs, vibration)
5. **Conserver un historique** de ses parties pour révision/analyse

---

## 📝 La Notation d'Échecs

### Notation Algébrique Standard (SAN)

C'est le format universel utilisé en compétition :

**Pièces :**
- ♔ **K** (Roi / King)
- ♕ **Q** (Dame / Queen)
- ♖ **R** (Tour / Rook)
- ♗ **B** (Fou / Bishop)
- ♘ **N** (Cavalier / kNight)
- ♙ *[rien]* (Pion / Pawn - pas de lettre)

**Coordonnées :**
- Colonnes : **a, b, c, d, e, f, g, h**
- Rangées : **1, 2, 3, 4, 5, 6, 7, 8**

**Exemples de coups :**
```
e4        → pion en e4
Nf3       → cavalier en f3
Qxe5      → dame prend en e5
Bxf7+     → fou prend en f7, échec
O-O       → petit roque
O-O-O     → grand roque
e8=Q      → promotion pion en dame
Nbd7      → cavalier de b vers d7 (désambiguïsation)
```

**Symboles spéciaux :**
- `+` : échec
- `#` : échec et mat
- `x` : prise
- `=` : égalité proposée
- `!` : bon coup
- `?` : coup douteux
- `!!` : coup brillant
- `??` : gaffe

---

## 👤 Utilisateur Cible

Joueurs d'échecs avec difficultés motrices en compétition :

- **Âge** : Jeune joueur (probablement 8-16 ans)
- **Niveau échecs** : Compétition interclubs
- **Contrainte** : Difficultés motrices pour l'écriture manuscrite
- **Matériel** : Tablette Android
- **Besoin** : Simplicité, gros boutons, feedback clair

### Contraintes d'Accessibilité

- ✅ **Boutons larges** : minimum 56-64dp pour faciliter le toucher
- ✅ **Espacement généreux** : éviter les erreurs de toucher
- ✅ **Feedback haptique** : vibration au toucher pour confirmation
- ✅ **Couleurs contrastées** : facile à distinguer
- ✅ **Pas de double-tap** : un seul toucher suffit
- ✅ **Interface claire** : pas de surcharge cognitive

---

## 🌍 Contexte d'Utilisation

### Environnement

**Compétition d'échecs interclubs :**
- Environnement calme mais concentré
- Temps limité entre chaque coup
- Besoin de rapidité et fiabilité
- **Pas de connexion internet** garantie → application **100% hors-ligne**

### Workflow Typique

1. **Avant la partie** :
   - Ouvrir l'application "Coup de Main"
   - Créer une nouvelle partie (adversaire, couleur jouée)
   
2. **Pendant la partie** :
   - Jouer un coup sur l'échiquier physique
   - Noter le coup via le clavier tactile
   - Validation visuelle du coup enregistré
   - Répéter pour chaque coup

3. **Après la partie** :
   - Résultat final (victoire, défaite, nulle)
   - Export PGN pour partage avec entraîneur/parents
   - Partie sauvegardée dans l'historique

### Cas d'Usage Alternatif

**Utilisation dans Word/Google Docs :**
- L'utilisateur peut basculer vers le clavier "Coup de Main" (IME)
- Noter directement dans un document Word
- Utile pour devoirs, analyses de parties, etc.

---

## 🎨 Inspiration Design

### Référence Visuelle

L'image fournie montre un prototype de clavier avec :

- **Section pièces** : ♙ ♘ ♗ ♖ ♕ ♚
- **Section lettres** : a b c d e f g h
- **Section chiffres** : 1 2 3 4 5 6 7 8
- **Section symboles** : x + # = ! ? O-O ✓

Layout **grid** (grille) avec boutons uniformes, espacement clair.

### Principes de Design

**Clarté avant tout :**
- Chaque bouton fait **une seule chose**
- Pas de combinaisons complexes
- Affichage en temps réel du coup en construction

**Adaptabilité :**
- Taille des boutons ajustable
- Thème clair/sombre/haut contraste
- Hauteur du clavier personnalisable

---

## 📊 Comparaison avec Autres Solutions

| Solution | Avantages | Inconvénients |
|----------|-----------|---------------|
| **Écriture manuscrite** | Format classique | ❌ Difficultés motrices de l'utilisateur |
| **Clavier AZERTY standard** | Disponible partout | ❌ Lent, complexe, erreurs |
| **Applications d'échecs** | Saisie graphique | ❌ Pas utilisable dans Word, format propriétaire |
| **Coup de Main** ✅ | Clavier dédié, IME système, gros boutons | ⚠️ Nécessite installation |

---

## 🚀 Vision du Projet

### MVP (Minimum Viable Product)

**Phase 1 - Validation (2-3 semaines)** :
- Clavier tactile fonctionnel avec gros boutons
- Service IME activable depuis Paramètres Android
- Customisation basique (taille boutons, thème)
- Tests utilisateurs → **ajustements selon feedback**

### Version Complète (1-2 mois)

- Application standalone avec historique des parties
- Export PGN complet
- Presets (Débutant/Standard/Compact)
- Optimisation tablette (layouts adaptatifs)
- Distribution Google Play Store

### Vision Long Terme

- Multi-langues (français, anglais, espagnol)
- Communauté de joueurs avec difficultés motrices
- Open-source pour permettre d'autres adaptations
- Potentiellement adaptable pour **autres jeux** (dames, go, échecs chinois)

---

## 🎯 Objectifs Mesurables

### Critères de Succès

1. **Utilisabilité** : L'utilisateur peut noter une partie complète sans aide
2. **Vitesse** : Noter un coup en < 5 secondes
3. **Précision** : < 1% d'erreurs de saisie
4. **Satisfaction** : Les utilisateurs préfèrent "Coup de Main" à l'écriture manuscrite
5. **Fiabilité** : 0 crash pendant une partie de 40 coups

### Métriques de Suivi

- Nombre de parties notées
- Temps moyen par coup
- Taux d'erreurs de notation
- Personnalisations utilisées (quelle taille, quel thème)
- Feedback qualitatif (entretiens utilisateurs)

---

## 📝 Historique des Décisions

### Décisions Clés

**Pourquoi Android et pas PWA ?**
- L'utilisateur a une tablette Android
- IME système permet usage dans Word/Docs
- Meilleure intégration native (vibration, performance)

**Pourquoi Room et pas SQLite direct ?**
- Plus moderne, type-safe
- Moins de boilerplate
- Meilleure maintenabilité long terme
- BKH a expérience Android (10+ ans dev mobile)

**Pourquoi "Coup de Main" ?**
- Nom français (accessible, chaleureux)
- Double sens : "aide" + "coup" d'échecs
- Facile à mémoriser
- Bon pour le Play Store

---

## 🤝 Parties Prenantes

### Hadrien
- **Rôle** : Utilisateur principal
- **Implication** : Tests, feedback, validation UX

### Papa d'Hadrien
- **Rôle** : Facilitateur, testeur secondaire
- **Implication** : Installation, coordination avec club

### BKH (Remy)
- **Rôle** : Développeur
- **Compétences** : 10+ ans dev mobile Android
- **Motivation** : Aider Hadrien + projet portfolio/contenu YouTube

### Club d'Échecs
- **Rôle** : Validation réglementaire
- **Implication** : Autoriser usage tablette en compétition

---

## 📚 Références

### Réglementations Échecs
- [FIDE Laws of Chess](https://www.fide.com/fide/handbook.html?id=171&view=article) - Article 8.1 : notation des coups
- Notation autorisée sur support électronique si approuvé par arbitre

### Design Accessibilité
- [Material Design Accessibility](https://m3.material.io/foundations/accessible-design) - Guidelines Google
- [WCAG 2.1](https://www.w3.org/WAI/WCAG21/quickref/) - Touch target minimum 44x44px

### Notation PGN
- [PGN Specification](http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm) - Format standard

---

_Ce document évolue avec le projet. Dernière mise à jour : 2026-02-02_
