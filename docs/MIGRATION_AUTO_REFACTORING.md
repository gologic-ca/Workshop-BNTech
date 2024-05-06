# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Migration automatisé avec OpenRewrite vers Springboot 3.x

### Build du projet avec le wrapper gradle

Ce projet compile au complet avec le JDK 11, vous devez donc changer la variable d'environnement de JAVA pour pointer vers la JDK11:

```
set JAVA_HOME=/path/to/jdk11
./gradlew -v
```

Ensuite compilez le projet avec la commande suivante:

```
./gradlew clean build
```

### Lancer la migration

Notre environnement est enfin prêt pour lancer la migration de notre application vers Springboot 3. Pour cela, nous allons utiliser OpenRewrite, un outil de refactoring automatisé qui nous permettra de migrer notre application en quelques étapes simples.

La configuration de l'outil est déjà faite dans le projet avec le plugin OpenRewrite dans le build.gradle en spécifiant la recette suivante à appliquer: 

```
rewrite {
  activeRecipe("org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2")
}
```

Déclenchez la tâche de migration en exécutant la tâche suivante: 

```
./gradlew rewriteRun
```

### Build du projet après la migration

Lorsque la migration a été complétée avec succès, nous allons essayer de build le projet en java 17.

Pour cela vous devez faire évoluer la version java de gradle:

```
set JAVA_HOME=/path/to/jdk17
./gradlew -v
```

Ensuite compilez le projet avec la commande suivante:

```
./gradlew clean build
```

À ce stade vous avez des erreurs de compilation car les recettes de migration appliquées modifient le code par rapport aux versions des cadres de travail utilisés mais OpenRewrite ne peut pas migrer le code spécifique à l'application.

## Félicitation !

Vous voila ainsi prêt pour finaliser la migration du projet en passant à la prochaine étape: [Fixer la migration avec GitHub Copilot](MIGRATION_COPILOT.md)