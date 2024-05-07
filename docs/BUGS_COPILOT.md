# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Qualité du code avec GitHub Copilot

Dans cet atelier, nous allons voir comment utiliser Github Copilot pour améliorer la qualité du code de notre application.

Le résultat de la nouvelle analyse affiche un niveau de qualité pouvant être améliorer au niveau de la sécurité, de la fiabilité et de la maintenabilité.

### Lier le plugin SonarLint à son instance SonarQube

Nous allons commencer par lier le plugin SonarLint à l'instance SonarQube pour synchroniser les règles de qualité de code ainsi que les problèmes détectés.

1. Avant de commencer, copiez quelque part en dehors d'IntelliJ votre token d'authentification SonarQube, que vous avez déjà copié dans le fichier build.gradle

2. Allez dans Settings → Tools → SonarLint et cliquez sur le bouton "+". Dans la fenêtre qui s'ouvre, entrez un nom pour la connexion (par exemple local), sélectionnez "SonarQube" et ajoutez l'URL de votre instance SonarQube (http://localhost:9000)
3. Entrez enfin votre token d'authentification et cliquez sur "Next", encore "Next" et enfin "Create"
4. Retourner dans Settings → Tools → SonarLint → Project Settings et sélectionnez la connexion que vous venez de créer
5. Cliquez sur "Search in the list" et sélectionnez le projet "workshop-dette-technique". 
6. Cliquer sur "OK".

La configuration est maintenant terminée, si jamais vous ne voyez pas des problèmes que SonarQube devrait relever dans l'IDE, redémarrez IntelliJ. 

### Score au niveau de la sécurité

#### Retirer OpenRewrite

Pour les problèmes de sécurité dans les dépendances, toutes les failles proviennent du plugin `openrewrite-gradle-plugin` qui est utilisé pour la migration.

Maintenant que nous avons fait la migration, nous pouvons retirer ce plugin du projet en utilisant l'assistant GitHub Copilot pour lui demander de supprimer toute référence à ce projet.

Pour cela, ouvrez le chat de Github Copilot et demandez-lui par exemple:

```
Dans le fichier build.gradle supprime toute référence à rewrite
```

La réponse retournée devrait vous avoir retiré toute référence à `openrewrite` soit les lignes suivantes ne devraient plus être là: 

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

Nous allons cibler maintenant un élément que copilot peut nous aider à améliorer.

#### Utilisation de 'instanceof'

Dans la classe `GraphQLCustomizeExceptionHandler.java`, vous devriez voir un code smell sur la ligne 42.

Pour ce code smell, il n'y a pas de correction rapide disponible, pour l'exercice nous allons utiliser Github Copilot.

Pour cela, ouvrez le chat de Github Copilot et demandez lui un fix pour la description du code smell, par exemple:

```
/fix Replace this instanceof check and cast with 'instanceof ConstraintViolationException constraintviolationexception' Only generate the snippet
```

Il vous proposera alors un code de correction que vous pourrez copier coller dans votre IDE, en validant que la solution proposée est correcte.


#### Fixer les problèmes de toList 
Nous allons remplacer l'utilisation de `Stream.collect(Collectors.toList())` par `Stream.toList()` dans l'ensemble du projet.

Pour cela, recherchez et remplacez dans l'ensemble du projet (Ctrl+Shift+R):
1. `collect(Collectors.toList())` et remplacez les par `toList()`.
2. `collect(toList())` et remplacez les par `toList()`.

## Félicitation !

Les problèmes qui empêchait le gating de passer sont maintenant corrigé. Si vous êtes limités par le temps, exécuter pour admirer votre travail:

```bash
./gradlew test dependencyCheckAnalyze sonar 
```

sinon on peut passer à l'étape suivante, ou nous allons faire des excercices pour améliorer encore plus la qualité du code: [Mise en place de la méthode "Clean as you code"](COMPLEXITY.md)

## Félicitation !

La dette technique et la qualité sont améliorées, vous voila ainsi prêt pour passer à la dernière étape: [Mise en place de la méthode "Clean as you code"](COMPLEXITY.md)
