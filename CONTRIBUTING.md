# Contributing to Coup de Main

Merci de considérer contribuer à **Coup de Main** ! 🎯

Ce projet a été créé pour aider les joueurs d'échecs avec des difficultés motrices à noter leurs parties. Toute contribution qui améliore l'accessibilité est particulièrement bienvenue.

---

## 🚀 Comment Contribuer

### 1. Fork & Clone

```bash
# Fork le repo sur GitHub, puis clone
git clone https://github.com/awcapps/coup-de-main-ime.git
cd coup-de-main-ime
```

### 2. Setup du Projet

**Prérequis :**
- JDK 17+
- Android SDK (API 21-35)
- Android Studio ou VS Code avec extensions Kotlin/Android

**Installation :**
```bash
# Vérifier que Gradle fonctionne
./gradlew build

# Lancer les tests
./gradlew test

# Installer sur émulateur/device
./gradlew installDebug
```

**Note Signature :** Le projet utilise un keystore pour les builds release, mais vous n'en avez **pas besoin** pour développer. Les builds debug utilisent le keystore par défaut d'Android.

### 3. Créer une Branche

```bash
git checkout -b feature/ma-super-feature
# ou
git checkout -b fix/correction-bug-xyz
```

**Conventions de nommage :**
- `feature/` : nouvelle fonctionnalité
- `fix/` : correction de bug
- `docs/` : documentation
- `refactor/` : refactoring code
- `test/` : ajout/correction tests

### 4. Coder

**Style de Code Kotlin :**
- Suivre les [conventions Kotlin officielles](https://kotlinlang.org/docs/coding-conventions.html)
- Noms de variables/fonctions en camelCase
- Noms de classes en PascalCase
- Indentation : 4 espaces
- Ligne max : 120 caractères

**Exemple :**
```kotlin
// ✅ Bon
class ChessKeyboardIME : InputMethodService() {
    private var currentNotation: String = ""
    
    fun buildNotation(piece: Piece, square: Square): String {
        return "$piece$square"
    }
}

// ❌ Mauvais
class chess_keyboard_ime : InputMethodService() {
    var currentnotation:String=""
    fun build_notation(p:Piece,s:Square):String{return "$p$s"}
}
```

**Tests :**
- Ajouter des tests unitaires pour nouvelle logique métier
- Tester sur émulateur **ET** device physique si possible
- Tests existants : `./gradlew test`

### 5. Commit

**Messages de commit :**
```bash
# Format : <type>: <description courte>
git commit -m "feat: ajoute support notation Fischer (Chess960)"
git commit -m "fix: corrige crash au toucher rapide des boutons"
git commit -m "docs: améliore README avec screenshots"
```

**Types de commit :**
- `feat`: nouvelle fonctionnalité
- `fix`: correction de bug
- `docs`: documentation
- `style`: formatage (pas de changement de code)
- `refactor`: refactoring
- `test`: ajout/modification tests
- `chore`: tâches maintenance (build, deps)

### 6. Push & Pull Request

```bash
git push origin feature/ma-super-feature
```

Puis sur GitHub :
1. Cliquer "Compare & pull request"
2. Décrire les changements clairement
3. Référencer les issues liées (`Fixes #42`)
4. Attendre review

---

## 🐛 Reporter un Bug

**Avant de créer une issue :**
1. Vérifier qu'elle n'existe pas déjà
2. Tester sur la dernière version

**Créer une issue avec :**
- **Description claire** du problème
- **Steps to reproduce** (étapes pour reproduire)
- **Version Android** et device testé
- **Logs/screenshots** si possible

**Template :**
```markdown
### Bug Description
[Description courte du bug]

### To Reproduce
1. Ouvrir l'app
2. Cliquer sur '...'
3. Observer l'erreur

### Expected Behavior
[Ce qui devrait se passer]

### Actual Behavior
[Ce qui se passe réellement]

### Environment
- Device: Samsung Galaxy S21
- Android: 13
- App version: 0.2.0
```

---

## 💡 Proposer une Fonctionnalité

**Avant de coder une grosse feature :**
1. Créer une issue "Feature Request"
2. Discuter de l'approche avec les mainteneurs
3. Attendre validation avant de démarrer

**Cela évite :**
- De coder quelque chose qui ne sera pas mergé
- De dupliquer le travail de quelqu'un d'autre
- De partir dans une mauvaise direction

---

## 🎯 Priorités du Projet

**High Priority :**
- **Accessibilité** : tout ce qui aide les utilisateurs avec difficultés motrices
- **Performance** : fluidité du clavier IME
- **Stabilité** : 0 crash en production

**Medium Priority :**
- Nouvelles features de notation (annotations, variantes)
- Support multi-langues
- Modes avancés (Chess960, problèmes)

**Low Priority :**
- Fonctionnalités "nice to have"
- Optimisations mineures

---

## 📚 Documentation

**Où documenter :**
- Code : docstrings KDoc pour fonctions publiques
- Architecture : `docs/STACK_TECHNIQUE.md`
- UX : `docs/CONTEXTE.md`
- Nouveautés : `CHANGELOG.md` (à jour par mainteneurs)

**Exemple KDoc :**
```kotlin
/**
 * Construit une notation d'échecs au format SAN (Standard Algebraic Notation).
 *
 * @param piece La pièce qui se déplace (null pour pion)
 * @param destination La case de destination (e.g., "e4")
 * @param isCapture Indique si c'est une prise
 * @return La notation complète (e.g., "Nxf3")
 */
fun buildNotation(
    piece: Piece?,
    destination: String,
    isCapture: Boolean = false
): String
```

---

## 🧪 Testing

**Tests Unitaires :**
```bash
# Lancer tous les tests
./gradlew test

# Lancer tests d'une classe spécifique
./gradlew test --tests NotationBuilderTest
```

**Tests UI (Espresso) :**
```bash
# Lancer sur émulateur/device connecté
./gradlew connectedAndroidTest
```

**Couverture de code :**
- Viser 80%+ pour logique métier (NotationBuilder, etc.)
- Tests UI optionnels mais appréciés

---

## ✅ Checklist avant Pull Request

- [ ] Code compile sans erreur (`./gradlew build`)
- [ ] Tests unitaires passent (`./gradlew test`)
- [ ] Testé sur émulateur/device
- [ ] Code formaté selon conventions Kotlin
- [ ] Documentation à jour (si nécessaire)
- [ ] Commit messages clairs
- [ ] Branche à jour avec `main`

---

## 🆘 Besoin d'Aide ?

- **Issues GitHub** : poser des questions avec label "question"
- **Discussions GitHub** : pour les questions générales
- **Email** : contact@remybaroukh.fr

---

## 📜 Code of Conduct

Ce projet suit le [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). En contribuant, vous acceptez de respecter ces règles.

---

**Merci pour votre contribution ! 🎉**

Chaque ligne de code, chaque bug reporté, chaque suggestion aide à rendre les échecs plus accessibles. ♟️
