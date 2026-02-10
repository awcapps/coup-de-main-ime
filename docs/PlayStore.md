# 🏪 Google Play Store - Checklist de Publication

**Date de préparation:** 6 février 2026  
**Application:** Coup de Main - Clavier Notation Échecs  
**Package:** bkh.apps.coupdemain  
**Version cible:** 0.0.2 (code 2)  
**Target SDK:** 35 (Android 15)

---

## 📱 ICÔNES & ASSETS GRAPHIQUES

### ✅ Logo de base
- [x] **squarelogo.png** - Logo carré créé dans `assets/`

### 🎨 Icônes adaptives Android à générer

#### Outils en ligne recommandés:
1. **Icon Kitchen** 🔥 (Recommandé)
   - https://icon.kitchen/
   - Upload ton logo carré
   - Génère automatiquement tous les formats Android (adaptive, legacy, round)
   - Export direct pour Android Studio

2. **Android Asset Studio** (Officiel Google)
   - https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
   - Plus manuel mais très flexible
   - Permet de contrôler les marges et le background

3. **EasyAppIcon**
   - https://easyappicon.com/
   - Simple et rapide
   - Génère pour Android + iOS

4. **Makeappicon**
   - https://makeappicon.com/
   - Upload une image 1024x1024 ou plus
   - Génère toutes les résolutions

#### Formats requis pour Android:
- [x] **Adaptive Icon** (foreground + background layers) ✅
  - `mipmap-xxxhdpi/` (192x192 px)
  - `mipmap-xxhdpi/` (144x144 px)
  - `mipmap-xhdpi/` (96x96 px)
  - `mipmap-hdpi/` (72x72 px)
  - `mipmap-mdpi/` (48x48 px)
- [x] **Legacy Round Icon** ✅
- [x] **Play Store Icon** (512x512 px, PNG 32-bit) ✅ → `assets/playstore-icon-512.png`

#### Instruction:
1. Va sur https://icon.kitchen/
2. Upload `assets/squarelogo.png`
3. Ajuste les marges si nécessaire (Safe Zone)
4. Télécharge le ZIP
5. Remplace les fichiers dans `app/src/main/res/mipmap-*/`

---

## 📸 SCREENSHOTS

### Requis minimum:
- [x] **2 screenshots minimum** (recommandé: 4-8) ✅
- [x] **Format:** JPEG ou PNG 24-bit (pas de transparence) ✅
- [x] **Résolutions acceptées:** ✅
  - Téléphone: 16:9 ou 9:16 (ex: 1080x1920 px)
  - Tablette 7": 16:9 ou 9:16
  - Tablette 10": 16:9 ou 9:16

### Contenu recommandé:
1. **Screenshot 1:** Écran d'accueil (liste des parties)
2. **Screenshot 2:** Notation en cours (clavier visible)
3. **Screenshot 3:** Historique coups d'une partie
4. **Screenshot 4:** Export PGN / Image
5. **Screenshot 5:** Onboarding (optionnel)

### Comment capturer:
```bash
# Lancer l'émulateur ou connecter un téléphone
adb shell screencap -p /sdcard/screenshot1.png
adb pull /sdcard/screenshot1.png ./assets/screenshots/

# Ou utiliser Android Studio: View → Tool Windows → Device Manager → Screenshot
```

### Localisation:
- [x] Screenshots FR (obligatoire) ✅
- [ ] ~~Screenshots EN~~ (non nécessaire - clavier FR uniquement)

---

## 📝 TEXTES & DESCRIPTIONS

### 1. Titre de l'application (max 50 caractères)
**Français:**
```
Coup de Main - Notation Échecs
```
✅ **30 caractères** (OK)

~~**Anglais:**~~ (non nécessaire - clavier FR uniquement)
```
Coup de Main - Chess Notation
```

---

### 2. Description courte (max 80 caractères)
**Français:**
```
Clavier intelligent pour noter vos parties d'échecs en temps réel
```
✅ **64 caractères** (OK)

~~**Anglais:**~~ (non nécessaire - clavier FR uniquement)

---

### 3. Description complète (max 4000 caractères)

#### **Français:**
```
🎯 Notez vos parties d'échecs en temps réel avec Coup de Main !

Coup de Main est le premier clavier Android intelligent conçu spécifiquement pour les joueurs d'échecs. Plus besoin de jongler entre votre échiquier et votre téléphone : notre clavier s'intègre directement dans votre expérience Android pour une notation fluide et naturelle.

✨ FONCTIONNALITÉS PRINCIPALES

🎹 Clavier Intelligent
• Disposition optimisée pour la notation algébrique (SAN)
• Touches dédiées aux pièces d'échecs (R, D, T, F, C)
• Symboles spéciaux (roque, échec, mat, promotion)
• Validation en temps réel des coups
• Retour vocal TTS configurable

📋 Gestion des Parties
• Création de parties avec choix des couleurs
• Historique complet de tous vos coups
• Annulation du dernier coup
• Timer automatique par partie
• Interface Material Design 3

💾 Export Professionnel
• Format PGN standard (compatible Lichess, Chess.com)
• Export en image JPG (1200x1600 px)
• Partage direct vers d'autres apps
• Métadonnées complètes (date, joueur, résultat)

♿ Accessibilité
• Support TalkBack complet
• ContentDescription sur tous les éléments
• Taille de cible minimum 44dp
• Animations douces

🎨 Personnalisation
• Thème clair / sombre automatique
• Couleurs adaptatives Material You
• 2 dispositions de clavier (standard/alternative)

🎓 IDÉAL POUR

• Joueurs de club et compétition
• Entraîneurs et professeurs d'échecs
• Parties amicales que vous voulez conserver
• Analyse post-partie
• Création d'un répertoire personnel

🔒 CONFIDENTIALITÉ

• Aucune collecte de données personnelles
• Aucune connexion internet requise
• Stockage 100% local sur votre appareil
• Aucune publicité

📱 COMPATIBILITÉ

• Android 5.0 (Lollipop) et supérieur
• Optimisé pour smartphones et tablettes
• Mode portrait et paysage
• Supporte les écrans de toutes tailles

🚀 UTILISATION

1. Activez le clavier dans les paramètres Android
2. Créez une nouvelle partie
3. Choisissez votre couleur (Blancs/Noirs)
4. Notez vos coups au fur et à mesure
5. Terminez la partie et exportez en PGN

Coup de Main transforme votre téléphone Android en véritable carnet de notation numérique. Que vous jouiez au club, à la maison ou en ligne, gardez une trace de toutes vos parties d'échecs !

♟️ Développé par des joueurs d'échecs, pour des joueurs d'échecs.
```
✅ **2,456 caractères** (OK)

~~#### **Anglais:**~~ (non nécessaire - clavier FR uniquement)
```
🎯 Record your chess games in real time with Coup de Main!

Coup de Main is the first intelligent Android keyboard designed specifically for chess players. No more juggling between your chessboard and your phone: our keyboard integrates directly into your Android experience for smooth and natural notation.

✨ KEY FEATURES

🎹 Smart Keyboard
• Optimized layout for algebraic notation (SAN)
• Dedicated keys for chess pieces (K, Q, R, B, N)
• Special symbols (castling, check, mate, promotion)
• Real-time move validation
• Configurable TTS voice feedback

📋 Game Management
• Create games with color selection
• Complete history of all your moves
• Undo last move
• Automatic timer per game
• Material Design 3 interface

💾 Professional Export
• Standard PGN format (Lichess, Chess.com compatible)
• JPG image export (1200x1600 px)
• Direct sharing to other apps
• Complete metadata (date, player, result)

♿ Accessibility
• Full TalkBack support
• ContentDescription on all elements
• 44dp minimum target size
• Smooth animations

🎨 Customization
• Automatic light/dark theme
• Material You adaptive colors
• 2 keyboard layouts (standard/alternative)

🎓 IDEAL FOR

• Club and tournament players
• Chess coaches and teachers
• Friendly games you want to keep
• Post-game analysis
• Building your personal repertoire

🔒 PRIVACY

• No personal data collection
• No internet connection required
• 100% local storage on your device
• No ads

📱 COMPATIBILITY

• Android 5.0 (Lollipop) and higher
• Optimized for smartphones and tablets
• Portrait and landscape mode
• Supports all screen sizes

🚀 HOW TO USE

1. Enable the keyboard in Android settings
2. Create a new game
3. Choose your color (White/Black)
4. Record your moves as you play
5. Finish the game and export to PGN

Coup de Main turns your Android phone into a real digital scorebook. Whether you play at the club, at home or online, keep track of all your chess games!

♟️ Developed by chess players, for chess players.
```
✅ **2,008 characters** (OK)

---

### 4. Nouveautés de cette version (max 500 caractères)
**Version 1.0.0 - Lancement initial**

**Français:**
```
🚀 Première version de Coup de Main !

✨ Fonctionnalités incluses :
• Clavier intelligent pour notation échecs
• Gestion complète des parties
• Export PGN et image
• Support TalkBack
• Thème clair/sombre adaptatif

♟️ Notez vos parties d'échecs comme jamais !
```
✅ **240 caractères** (OK)

~~**Anglais:**~~ (non nécessaire - clavier FR uniquement)
```
🚀 First release of Coup de Main!

✨ Features included:
• Smart keyboard for chess notation
• Complete game management
• PGN and image export
• TalkBack support
• Adaptive light/dark theme

♟️ Record your chess games like never before!
```
✅ **220 characters** (OK)

---

## 🎬 IMAGE PROMOTIONNELLE (Feature Graphic)

### Spécifications:
- [x] **Dimensions:** 1024 x 500 px ✅
- [x] **Format:** PNG ou JPEG 24-bit ✅
- [x] **Poids max:** 1 MB ✅
- [x] **Utilisation:** Bannière en haut de la fiche Play Store ✅

### Contenu suggéré:
- Logo de l'app à gauche
- Titre "Coup de Main"
- Slogan: "Notation d'échecs intelligente"
- Fond dégradé aux couleurs de l'app
- Aperçu du clavier ou de l'interface

### Outils recommandés:
- **Canva** (https://www.canva.com/) - Templates Play Store
- **Figma** (https://www.figma.com/) - Design professionnel
- **GIMP** / **Photoshop** - Retouche manuelle

---

## 🎥 VIDÉO PROMOTIONNELLE (Optionnel)

### Spécifications:
- [ ] **Durée:** 30 secondes à 2 minutes
- [ ] **Format:** MP4, MOV, ou AVI
- [ ] **Résolution:** 720p minimum (1080p recommandé)
- [ ] **Poids max:** 100 MB
- [ ] **Hébergement:** YouTube (URL requise)

### Contenu suggéré:
1. Intro: Logo + titre (3s)
2. Problème: Notation manuelle fastidieuse (5s)
3. Solution: Démonstration du clavier (20s)
4. Export PGN vers Lichess (5s)
5. Call to action: "Téléchargez maintenant" (2s)

---

## 📋 INFORMATIONS DE L'APPLICATION

### Catégorie principale:
- [ ] **Jeux** → **Plateau**
OU
- [ ] **Outils**

**Recommandation:** Jeux > Plateau (plus visible pour les joueurs d'échecs)

### Tags (jusqu'à 5):
1. Échecs
2. Notation
3. PGN
4. Clavier
5. Chess

### Type d'application:
- [ ] **Application** (pas un jeu complet)
- [ ] **Gratuite**

### Contenu:
- [ ] **Classification:** PEGI 3 / Everyone (tout public)
- [ ] **Pas de publicités**
- [ ] **Pas d'achats intégrés**

---

## 🔒 POLITIQUE DE CONFIDENTIALITÉ

### Requis obligatoire:
- [ ] **URL de politique de confidentialité**

### Options:
1. **Créer une page sur ton site web**
2. **Héberger sur GitHub Pages** (gratuit)
3. **Service gratuit:** https://www.privacypolicies.com/

### Contenu minimum requis:
```markdown
# Privacy Policy - Coup de Main

Last updated: February 6, 2026

## Data Collection
Coup de Main does NOT collect, store, or share any personal data.

## Local Storage
All chess games are stored locally on your device using Android Room Database.
No data is transmitted to external servers.

## Permissions
- VIBRATE: Used for haptic feedback on key presses
No internet permission is requested.

## Third-party Services
This app does not use any third-party analytics or tracking services.

## Contact
For questions: [ton-email@example.com]
```

---

## 🏗️ BUILD & SIGNATURE

### 1. Générer le Keystore (si pas déjà fait)
```powershell
cd D:\OpenClawFiles\dev\coup-de-main
keytool -genkey -v -keystore coup-de-main-release.keystore -alias coupdemain -keyalg RSA -keysize 2048 -validity 10000
```

**Informations à fournir:**
- [ ] Mot de passe du keystore (GARDE LE PRÉCIEUSEMENT!)
- [ ] Nom complet
- [ ] Organisation (optionnel)
- [ ] Ville / Localité
- [ ] Pays (FR)

⚠️ **CRITIQUE:** Sauvegarde ce fichier et le mot de passe dans un endroit sûr (1Password, Bitwarden, etc.)
Sans lui, tu ne pourras plus jamais mettre à jour l'app!

### 2. Configurer le fichier de signature
Créer `app/keystore.properties`:
```properties
storeFile=../coup-de-main-release.keystore
storePassword=[TON_MOT_DE_PASSE]
keyAlias=coupdemain
keyPassword=[TON_MOT_DE_PASSE]
```

⚠️ **Ajouter à .gitignore:** Ne jamais commit ce fichier!

### 3. Modifier `app/build.gradle.kts`
```kotlin
android {
    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("app/keystore.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 4. Builder le AAB (Android App Bundle)
```powershell
.\gradlew bundleRelease
```

**Fichier généré:**
```
app/build/outputs/bundle/release/app-release.aab
```

### 5. Tester le AAB
```powershell
# Installer bundletool
# Download from: https://github.com/google/bundletool/releases

java -jar bundletool-all.jar build-apks --bundle=app/build/outputs/bundle/release/app-release.aab --output=app.apks --mode=universal

# Installer sur appareil
java -jar bundletool-all.jar install-apks --apks=app.apks
```

---

## 📋 CHECKLIST FINALE AVANT SOUMISSION

### Assets graphiques:
- [x] Icône adaptive générée et intégrée (tous les mipmap)
- [x] Icône 512x512 pour Play Store (`assets/playstore-icon-512.png`)
- [x] Feature Graphic 1024x500 ✅
- [x] 4-8 screenshots téléphone ✅
- [ ] 2 screenshots tablette (optionnel)

### Textes:
- [x] Titre (FR) ✅
- [x] Description courte (FR) ✅
- [x] Description longue (FR) ✅
- [x] Notes de version (FR) ✅
- [ ] ~~Versions EN~~ (non nécessaire - clavier FR uniquement)

### Technique:
- [x] Keystore généré et sauvegardé ✅
- [x] AAB signé généré ✅
- [x] Version testée sur émulateur ✅
- [x] Version testée sur device physique ✅
- [ ] Politique de confidentialité publiée (URL)

### Google Play Console:
- [ ] Compte Google Play Developer actif (25$ one-time fee)
- [ ] Informations de contact renseignées
- [ ] Adresse email de support
- [ ] Pays de distribution sélectionnés
- [ ] Classification du contenu complétée
- [ ] Prix défini (gratuit)

### Juridique:
- [ ] Déclaration de conformité aux règles du Play Store
- [ ] Confirmation des droits sur l'app
- [ ] Politique de confidentialité conforme RGPD (si EU)

---

## 🚀 PROCESSUS DE SOUMISSION

### Étape 1: Créer l'application sur Play Console
1. Va sur https://play.google.com/console
2. "Créer une application"
3. Remplis les infos de base

### Étape 2: Production Track
1. "Production" → "Créer une version"
2. Upload `app-release.aab`
3. Remplis les notes de version

### Étape 3: Fiche du Store
1. "Liste du magasin" → "Principale"
2. Upload tous les assets graphiques
3. Colle les descriptions
4. Sélectionne la catégorie

### Étape 4: Classification du contenu
1. "Classification du contenu"
2. Réponds au questionnaire (app tout public, pas de contenu sensible)

### Étape 5: Tarification et distribution
1. "Tarification et distribution"
2. Sélectionne "Gratuit"
3. Choisis les pays (monde entier ou ciblé)
4. Coche les déclarations

### Étape 6: Privacy Policy
1. "Politique de confidentialité"
2. Entre l'URL de ta privacy policy

### Étape 7: Révision finale
1. Vérifie tous les onglets (pastille verte partout)
2. "Réviser la version" → "Démarrer le déploiement en production"

### Étape 8: Attendre l'approbation
- **Délai:** 1-7 jours (généralement 1-3 jours)
- **Email de confirmation** quand c'est publié
- **Tu peux suivre le statut dans Play Console**

---

## 📊 POST-LANCEMENT

### Analytics à surveiller:
- Nombre d'installations
- Note moyenne
- Commentaires utilisateurs
- Taux de crash (Android Vitals)
- Pays d'installation

### Support utilisateurs:
- Répondre aux commentaires
- Analyser les retours
- Préparer mises à jour

---

## 🎯 RÉSUMÉ DES TÂCHES IMMÉDIATES

### Phase 1: Assets ✅ TERMINÉ
1. ✅ Logo carré existant dans `assets/squarelogo.png`
2. ✅ Générer icônes adaptives sur https://icon.kitchen/
3. ✅ Créer Feature Graphic 1024x500
4. ✅ Capturer screenshots

### Phase 2: Keystore ✅ TERMINÉ
1. ✅ Générer keystore avec keytool
2. ✅ Créer `app/keystore.properties`
3. ✅ Modifier `app/build.gradle.kts` pour signature
4. ✅ Builder AAB: `.\gradlew bundleRelease`
5. ✅ Mise à jour target SDK 35 (requis Play Store 2026)

**🎉 Fichier généré:** `app/build/outputs/bundle/release/app-release.aab` (2.86 MB)

### Phase 3: Textes (30 min)
1. [ ] Créer page Privacy Policy (GitHub Pages ou site perso)
2. [x] Vérifier descriptions FR ci-dessus ✅
3. [ ] Préparer adresse email support

### Phase 4: Soumission (1h)
1. [ ] Créer app sur Play Console
2. [ ] Upload AAB + screenshots + textes
3. [ ] Remplir questionnaire classification
4. [ ] Submit for review

**TEMPS TOTAL ESTIMÉ: 3-4 heures**

---

## 📞 CONTACTS & LIENS UTILES

### Documentation officielle:
- **Play Console:** https://play.google.com/console
- **Guide développeur:** https://developer.android.com/distribute
- **Règles du Play Store:** https://play.google.com/about/developer-content-policy/

### Support:
- **Help Center:** https://support.google.com/googleplay/android-developer
- **Community:** https://www.reddit.com/r/androiddev

---

**🎉 Bonne chance pour le lancement !**
