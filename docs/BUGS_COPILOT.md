# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Qualité du code avec GitHub Copilot
...

Si vous allez voir le résultat de la nouvelle analyse, vous devriez voir qu'elle a échouée sur le nouveau code à cause de nombreux problèmes de sécurité et d'une couple de code smells.\
Pour les problèmes de sécurité dans les dépendances, on observe qu'elles viennent toute du plugin 'openrewrite-gradle-plugin' qui est utilisé pour la migration.\
Maintenant que nous avons fait la migration, nous pouvons retirer ce plugin du projet.\
Pour cela, ouvrez le fichier `build.gradle` et retirez la ligne suivante:

```groovy
    id "org.openrewrite.rewrite" version "7.8.0"
```
Ainsi que toutes les recettes actives, et dans les dépendances retirez:

```groovy  
    rewrite("org.openrewrite.recipe:rewrite-spring:5.8.0")
```

Il restera encore une vulnérabilité, liée à une dépendance de dépendance qui est jackson-databind.\
Pour corriger cette vulnérabilité, nous allons mettre à jour la version de jackson-databind dans le fichier `build.gradle`, dans la section dependencies:

```groovy
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.16.0'
```

Pour ce qui est des code smells, nous allons les corriger manuellement, ainsi qu'avec l'aide de Github Copilot.

##### Utilisation des collectors

Nous allons remplacer l'utilisation de `Stream.collect(Collectors.toList())` par `Stream.toList()` dans l'ensemble du projet.\
Pour cela, faites une recherche dans l'ensemble du projet (Ctrl+Shift+F) pour `collect(Collectors.toList())` et `collect(toList())` et remplacez les par `toList()`.
Si vous relancez une analyse Sonar, vous devriez voir que le code smell a été corrigé.

##### Utilisation des références de méthode dans les lambdas

Si vous allez dans la classe `ArticleQueryService.java` vous devriez voir un code smell sur la ligne 160.\
IntelliJ nous propose une correction rapide, vous pour l'appliquer en appuyant sur `Ctrl+Alt+Enter` ou en maintenant votre souris sur le code souligné et en choisissant la première option.

##### Utilisation de 'instanceof'

Dans la classe `GraphQLCustomizeExceptionHandler.java`, vous devriez voir un code smell sur la ligne 42.\
Pour ce code smell, il n'y a pas de correction rapide disponible, pour l'exercice nous allons utiliser Github Copilot.\
Pour cela, ouvrez le chat de Github Copilot et demandez lui un fix pour la description du code smell, par exemple:

    /fix Replace this instanceof check and cast with 'instanceof ConstraintViolationException constraintviolationexception' Only generate the snippet

Il vous proposera alors un code de correction que vous pourrez copier coller dans votre IDE, en validant que la solution proposée est correcte.
