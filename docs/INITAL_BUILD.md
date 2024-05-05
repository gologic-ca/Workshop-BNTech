# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Prérequis

Cette étape est destinée à vous aider à préparer votre environnement pour le workshop. 

Vous aurez besoin de:
- Un IDE Java (IntelliJ IDEA ou VsCode)
- Java 17 
- Plugin Sonarlint
- GitHub Copilot

## Récupérer le code source de l'atelier

Le code source de l'atelier est déposé sur GitHub à cet emplacement [https://github.com/gologic-ca/Workshop-dette-technique].

Récupérez de l'une de ces 2 manières:

```shell
gh repo clone gologic-ca/Workshop-dette-technique
git clone https://github.com/gologic-ca/Workshop-dette-technique.git

cd Workshop-dette-technique
```

## Configurer le bon JDK

Il est nécessaire de remplaçer de mettre dans la variable d'environnement JAVA_HOME le chemin vers le JDK 17.

* Pour mac, linux ou gitbash:
```shell
export JAVA_HOME=/path/to/jdk17
```

* Pour cmd sur windows:
```shell
set JAVA_HOME=C:\path\to\jdk17
```

Valider le bonne exécution de gradle wrapper, la version de gradle et du JDK utilisé par le projet en exécutant la commande suivante:
```shell
./gradlew -v
```

## Valider la compilation du projet

Par la suite, vous devez exécuter la commande: 

```shell
./gradlew clean build
```

## Félicication !

Vous voila ainsi prêt pour le workshop en passant à la prochaine étape: [Analyse SonarQube initiale](INITAL_SAST.md)