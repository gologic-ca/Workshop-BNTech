# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Mise en contexte
[OpenRewrite](https://github.com/openrewrite) est un outil maintenu par l'entreprise [Moderne](https://www.moderne.io/) et permet d'opérer des transformations sur votre code source. 

L'outil transforme le code source dans un arbre de syntaxe abstraite ce qui lui permet de se déplacer dans les différentes parties du code et d'appliquer des transformations.

![lst](image.png)

L'outil utilise un concept de recette à appliquer sur le code source. La communauté de développeur contribue énormément à l'amélioration des recettes ce qui rend les transformations extrêmement robustes et fiables. 

> [!NOTE]
­Il existe actuellement plus de 1000 recettes disponibles 
pour une multitude de frameworks et de langages de  programmation( Java, Terraform, Python, etc).


## Migration automatisé avec OpenRewrite vers Springboot 3.x

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

Lorsque la migration a été complétée avec succès, nous allons essayer de build le projet migré vers Springboot 3.

Pour cela exécutez la commande suivante:

```
./gradlew clean build
```

À ce stade vous avez des erreurs de compilation car les recettes de migration appliquées modifient le code par rapport aux versions des cadres de travail utilisés mais OpenRewrite ne peut pas migrer le code spécifique à l'application.

## Félicitation !

Vous voila ainsi prêt pour finaliser la migration du projet en passant à la prochaine étape: [Fixer la migration avec GitHub Copilot](MIGRATION_COPILOT.md)
