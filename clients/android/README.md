# Build script pro bowling

## Požadavky

- JDK >=1.8
- Android SDK (Build Tools 28)
- Cesta k Android SDK (alespoň jedno)
    - Nastavena v systémové proměnné ANDROID_HOME
    - Definována v souboru `local.properties` (př.: `sdk.dir=C\:\\Users\\User\\AppData\\Local\\Android\\Sdk`)

## Spuštění skriptu

- Windows:
    `gradlew build -p bowling`
- Linux:
    `./gradlew build -p bowling`