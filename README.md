# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé : cas concret avec SonarQube, GitHub Copilot et Open Rewrite
## Setup de l'environnement

### Création d'une image à partir d'un Dockerfile et démarrage du conteneur

Pour créer une image à partir d'un Dockerfile et démarrer un conteneur, suivez les étapes ci-dessous :

1. Assurez-vous d'avoir Docker installé sur votre machine. Si ce n'est pas le cas, vous pouvez le télécharger et l'installer à partir du site officiel de Docker.

2. Placez-vous dans le répertoire /sonar qui contient le Dockerfile que vous souhaitez utiliser pour créer l'image.

3. Ouvrez une fenêtre de terminal ou une invite de commande et exécutez la commande suivante pour construire l'image :

    ```shell
    docker build --pull --rm -t workshopsonar:latest "."
    ```

4. Une fois l'image construite, exécutez la commande suivante pour démarrer un conteneur à partir de l'image:

    ```shell
    docker run -d --name workshopsonar -p 9000:9000 workshopsonar:latest
    ```

5. Une fois le conteneur démarré, ouvrez votre navigateur web et rendez-vous à l'adresse [http://localhost:9000](http://localhost:9000).

6. Connectez-vous en utilisant les identifiants suivants :
   - Nom d'utilisateur : admin
   - Mot de passe : admin

7. Après vous être connecté, vous serez invité à changer votre mot de passe. Suivez les instructions à l'écran pour le modifier.

8. Une fois connecté et votre mot de passe modifié, choisissez de créer un projet local en cliquant sur l'option correspondante.

9. Nommez le projet "Workshop-dette-technique" et définissez la branche principale comme étant "master".

10. Appuyez sur "Next" et choisissez "Use global settings" dans le nouvel écran, puis cliquez sur "Create project".

11. Ensuite, pour la méthode d'analyse, choisissez "Locally".

12. Laissez le nom du token par défaut et choisissez "Generate".

13. Copiez le token généré et collez-le dans la propriété "sonar.token" du fichier "build.gradle" du projet.

À ce stade, nous avons fini de configurer notre serveur SonarQube et allons pouvoir lancer notre première analyse.

### Lancer une analyse sonar

Avant de lancer l'analyse, assurez vous que vous avec bien copié votre token sonar dans le fichier "build.gradle".
Ensuite exécutez la commande suivante dans un terminal ouvert au niveau du projet java:

    ./gradlew test dependencyCheckAnalyze sonar


L'analyse devrait prendre quelques minutes tout au plus à s'exécuter.
Une fois faites, vous pouvez retourner sur la page "overview" de notre serveur sonar, et les données de l'analyse devraient être disponibles.
Vous devriez voir une page qui ressemble à celle-ci:

![Analyse Sonar](images/analyse-sonar.png)

## Migration de l'application vers Springboot 3

### Build du projet

#### Avec le plugin gradle pour IntelliJ IDEA

Pour build le projet, assurez-vous déjà que votre projet gradle utilise la version 11 de java. Pour vérifier ce paramètre dans IntelliJ IDEA, allez dans Settings > Build, Execution, Deployment > Build Tools > Gradle et assurez-vous que le JDK utilisé est le JDK 11:
![JDK 11](images/jdk-11.png)

Vous pouvez alors build le projet en commençant par aller dans Tasks > other > spotlessJavaApply pour formater le code, puis Tasks > build > build pour build le projet.

#### Avec le wrapper gradle

Si vous préférez utiliser le wrapper gradle en command line, assurez vous en premier que votre wrapper utilise la version 11 de java, en utilisant la commande:

    ./gradlew -v

Si la version de la JVM n'est pas la version 11, vous pouvez la changer en modifiant soit votre JAVA_HOME dans les variables d'environnement, soit en utilisant l'argument -Dorg.gradle.java.home=/path/to/jdk11 lors de l'exécution de la commande gradlew:

    ./gradlew -Dorg.gradle.java.home=/path/to/jdk11 -v

Ensuite, pour build le projet, exécutez la commande suivante:

    ./gradlew -Dorg.gradle.java.home=/path/to/jdk11 spotlessJavaApply build

### Lancer la migration

Notre environnement est enfin prêt pour lancer la migration de notre application vers Springboot 3. Pour cela, nous allons utiliser OpenRewrite, un outil de refactoring automatisé qui nous permettra de migrer notre application en quelques étapes simples.
La configuration de l'outil est déjà faite dans le projet, il ne reste plus qu'à lancer la migration, pour cela exécutez la commande suivante:

    ./gradlew rewriteRun

### Build du projet après la migration

Lorsque la migration a été complétée avec succès, nous allons essayer de build le projet en java 17.
Pour cela il changer la version java de gradle en allant dans Settings > Build, Execution, Deployment > Build Tools > Gradle et en choisissant la version 17 de java.

Si vous utilisez gradle en CLI, vérifier avec l'option '-v' que la version de la JVM est bien la version 17.

Ensuite, exécutez spotlessJavaApply puis build depuis le plugin gradle ou utilisez la commande suivante:

    ./gradlew spotlessJavaApply build

Vous devriez avoir des erreurs de compilation, car la recette de migration ne peut pas corriger tous les problèmes.
Pour corriger ces erreurs, nos essais avec Github Copilot n'ont pas été concluants, principalement parce que le modèle utilisé en arrière a été entrainé avant que Springboot 3 ne soit sorti.
Nous allons donc devoir corriger ces erreurs manuellement.

#### CustomizeExceptionHandler.java

Dans le fichier `CustomizeExceptionHandler.java`, nous avons une erreur de compilation à la ligne 62. Pour corriger cette erreur, remplacez la ligne 62 par la ligne suivante:

```java
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
```
N'oubliez pas d'importer `org.springframework.http.HttpStatusCode;`

#### WebSecurityConfig.java

Dans le fichier `WebSecurityConfig.java`, nous avons une erreur de compilation à la ligne 39. Pour corriger cette erreur, remplacez la ligne 39 par la ligne suivante:

```java
  http.csrf(AbstractHttpConfigurer::disable)
```

Il faudra également importer `org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer`\
Et retirer une parenthèse à la ligne 60:

```java
  .authenticated());
```

#### GraphQLCustomizeExceptionHandler.java

Dans le fichier `GraphQLCustomizeExceptionHandler.java`, nous avons une erreur de compilation à la ligne 31. Pour corriger cette erreur, remplacez la ligne 31 par la ligne suivante:

```java
  public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
```

Importez les classe manquantes `java.util.concurrent.CompletableFuture`.\
Il nous faudra aussi modifier tous les retours de méthode de `DataFetcherExceptionHandler` pour retourner un `CompletableFuture<DataFetcherExceptionHandlerResult>`:

 ```java
    return CompletableFuture.completedFuture(DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build());
 ```
Pour les lignes 41 et 65 et:

```java
    return CompletableFuture.completedFuture(defaultHandler.onException(handlerParameters));
```

Pour la ligne 67.

#### Mise à jour de spotless

Enfin nous devons mettre à jour le plugin spotless pour fonctionner avec java 17. Changer la version du plugin pour celle-ci:

```groovy
id "com.diffplug.spotless" version "6.25.0"
```

#### Correction des tests

Si vous lancez un build à ce stade vous devriez avoir des erreurs lors de l'exécution des tests. Nous allons devoir mettre plusieurs dépendances à jour:

 ```groovy
 implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3'
 testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.3'
testImplementation 'io.rest-assured:rest-assured:5.4.0'
testImplementation 'io.rest-assured:json-path:5.4.0'
testImplementation 'io.rest-assured:xml-path:5.4.0'
testImplementation 'io.rest-assured:spring-mock-mvc:5.4.0'
```

Avec ces changements vous devriez pouvoir build le projet sans erreurs.

### Analyse Sonar post migration

Pour lancer une nouvelle analyse Sonar après la migration, exécutez la commande suivante:

    ./gradlew test dependencyCheckAnalyze sonar

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
