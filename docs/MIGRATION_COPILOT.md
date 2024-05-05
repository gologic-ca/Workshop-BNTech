# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Fixer la migration avec GitHub Copilot

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

