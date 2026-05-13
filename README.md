	Installation Outils :
- Java 17
- NodeJs
- PostgreSQL
- Android Studio
- Pixel 6 et API 33 (android 13)

	Lancement en mode dev :
# Dans le dossier VisiteurApp
npx react-native start
# Dans un autre terminal
npx react-native run-android

	Lancement complet :
# 1. Démarrer le backend Spring Boot port 8083
cd visiteur-api
mvn spring-boot:run

# 2. Démarrer le frontend React Native
cd VisiteurApp
npx react-native start --reset-cache

# 3. Dans un autre terminal, lancer sur émulateur
cd VisiteurApp
npx react-native run-android