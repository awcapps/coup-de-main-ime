# Security Policy

## Supported Versions

Les versions suivantes de **Coup de Main** reçoivent des mises à jour de sécurité :

| Version | Support          |
| ------- | ---------------- |
| 0.2.x   | ✅ Supportée     |
| 0.1.x   | ⚠️ Partiel      |
| < 0.1   | ❌ Non supportée |

## Reporting a Vulnerability

Si vous découvrez une vulnérabilité de sécurité dans **Coup de Main**, merci de **NE PAS** créer une issue publique.

### Comment Reporter

**Option 1 : GitHub Security Advisory (préféré)**
1. Aller sur l'onglet "Security" du repo
2. Cliquer "Report a vulnerability"
3. Remplir le formulaire avec détails

**Option 2 : Email**
Envoyer un email à : **contact@remybaroukh.fr**

### Informations à Inclure

Pour nous aider à traiter rapidement la vulnérabilité, merci d'inclure :

- **Type de vulnérabilité** (injection, XSS, déni de service, etc.)
- **Localisation** : fichier(s) et ligne(s) de code concernés
- **Configuration** nécessaire pour reproduire
- **Proof of Concept** ou exploit (si possible)
- **Impact potentiel** : que peut faire un attaquant ?
- **Suggestions de correction** (optionnel)

### Ce à Quoi S'Attendre

- **Confirmation** : dans les 48h
- **Évaluation initiale** : dans les 7 jours
- **Plan de correction** : selon la sévérité
  - **Critique** : fix dans les 7 jours
  - **Haute** : fix dans les 30 jours
  - **Moyenne/Basse** : fix dans la prochaine release

### Divulgation Responsable

Nous suivons le principe de **responsible disclosure** :

1. Vous rapportez la vulnérabilité en privé
2. Nous travaillons ensemble sur un fix
3. Nous publions un patch
4. *Ensuite*, la vulnérabilité peut être divulguée publiquement

**Note :** Merci de ne **pas exploiter** la vulnérabilité ou d'accéder à des données non autorisées.

### Scope (Portée)

**✅ En scope :**
- Code source de l'application Android
- Service IME (clavier)
- Stockage local (Room DB, DataStore)

**❌ Hors scope :**
- Infrastructure GitHub (rapporter directement à GitHub)
- Services tiers (Play Store, etc.)
- Failles supposées sans Proof of Concept

### Pas de Bug Bounty

**Coup de Main** est un projet open source à but **non lucratif**, créé pour aider les joueurs d'échecs avec des difficultés motrices. Nous n'offrons **pas de récompense financière** pour les vulnérabilités reportées.

Cependant, nous vous remercions publiquement dans :
- Le changelog de la release qui fixe la vulnérabilité
- Un fichier `SECURITY_THANKS.md` (si vous êtes d'accord)

### Questions ?

Pour toute question sur la politique de sécurité : **contact@remybaroukh.fr**

---

**Merci de nous aider à garder Coup de Main sécurisé ! 🔒**
