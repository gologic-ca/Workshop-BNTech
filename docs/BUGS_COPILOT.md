# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Qualité du code avec GitHub Copilot

Dans cet atelier, nous allons voir comment utiliser Github Copilot pour améliorer la qualité du code de notre application.

Le résultat de la nouvelle analyse affiche un niveau de qualité pouvant être améliorer au niveau de la sécurité, de la fiabilité et de la maintenabilité.

### Score au niveau de la sécurité

#### Retirer OpenRewrite

Pour les problèmes de sécurité dans les dépendances, toutes les failles proviennent du plugin `openrewrite-gradle-plugin` qui est utilisé pour la migration.

Maintenant que nous avons fait la migration, nous pouvons retirer ce plugin du projet en utilisant l'assistant GitHub Copilot pour lui demander de supprimer toute référence à ce projet.

Pour cela, ouvrez le chat de Github Copilot et demandez lui par exemple:

```
Dans le fichier build.gradle supprime toute référence à rewrite
```

La réponse retournée devrait vous avoir retirer toute référence à `openrewrite` soit les lignes suivantes ne devraient plus être là: 

- `id "org.openrewrite.rewrite" ...`
- `rewrite { activeRecipe("org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2") }`
- `rewrite("org.openrewrite.recipe:rewrite-spring:5.8.0")`

#### Mise à jour de dépendances

Pour l'ensemble des dépendances à mettre à jour, utilisez IntelliJ pour vous aider.

Exemple: 
- `org.xerial:sqlite-jdbc`, mettre à jour en cliquant sur la CVE et sélectionnant la version `3.41.2.2`

Il restera encore des vulnérabilités non-déclarées dans le `build.gradle`: 
- `jackson-databind`, ajoutez la dépendance dans la section dependencies: `implementation 'com.fasterxml.jackson.core:jackson-databind:2.16.0'`
- `protobuf-java`, ajoutez la dépendance dans la section dependencies: `implementation 'com.google.protobuf:protobuf-java:3.21.7'`

À ce stade le niveau de sécurité est maintenant à `A` !

### Maintenabilité et fiabilité

Pour ce qui est des code smells, nous allons les corriger manuellement, ainsi qu'avec l'aide de Github Copilot.

#### Utilisation des collectors

Nous allons remplacer l'utilisation de `Stream.collect(Collectors.toList())` par `Stream.toList()` dans l'ensemble du projet.

Pour cela, recherchez et remplacez dans l'ensemble du projet (Ctrl+Shift+R):
1. `collect(Collectors.toList())` et remplacez les par `toList()`.
2. `collect(toList())` et remplacez les par `toList()`.

#### Utilisation des références de méthode dans les lambdas

Si vous allez dans la classe `ArticleQueryService.java` vous devriez voir un code smell sur la ligne 160.
IntelliJ nous propose une correction rapide, vous pour l'appliquer en appuyant sur `Ctrl+Alt+Enter` ou en maintenant votre souris sur le code souligné et en choisissant la première option.

#### Utilisation de 'instanceof'

Dans la classe `GraphQLCustomizeExceptionHandler.java`, vous devriez voir un code smell sur la ligne 42.

Pour ce code smell, il n'y a pas de correction rapide disponible, pour l'exercice nous allons utiliser Github Copilot.

Pour cela, ouvrez le chat de Github Copilot et demandez lui un fix pour la description du code smell, par exemple:

```
/fix Replace this instanceof check and cast with 'instanceof ConstraintViolationException constraintviolationexception' Only generate the snippet
```

Il vous proposera alors un code de correction que vous pourrez copier coller dans votre IDE, en validant que la solution proposée est correcte.

#### Nettoyage des imports non-utilisés

Utilisez l'inspection du code de IntelliJ pour vous aider à supprimer toute référence à des imports inutiles.

Sur le projet, sélectionnez `Analyze->Inspect Code`, dans le résultat naviguez dans `Inspection Results -> Java -> Imports -> Unused import` et avec le bouton droit choisir `Delete unnecessary imports` pour nettoyer l'ensemble du projet.

## Félicitation !

La dette technique et la qualité est améliorée, vous voila ainsi prêt pour passer à la dernière étape: [Mise en place de la méthode "Clean as you code"](docs/CAYC.md)