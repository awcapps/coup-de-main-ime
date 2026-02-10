# GitHub Actions Workflows (TODO)

Ce dossier contiendra les workflows CI/CD pour automatiser :

- ✅ Build automatique sur chaque PR
- ✅ Exécution des tests unitaires
- ✅ Vérification du code (lint)
- ✅ Build des APK pour releases
- ✅ Upload des artifacts

## À Implémenter

Créer `.github/workflows/ci.yml` avec :
- Build Gradle
- Tests JUnit
- Analyse de code
- Cache Gradle pour optimiser

## Exemple Workflow

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew build
      - name: Run tests
        run: ./gradlew test
```

<!-- Utilisateurs : ignorer ce fichier, c'est pour les mainteneurs -->
