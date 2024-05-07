# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Qualité du code avec GitHub Copilot

Dans cet atelier, nous allons voir comment utiliser Github Copilot pour améliorer la qualité du code de notre application.

Le résultat de la nouvelle analyse affiche un niveau de qualité pouvant être améliorer au niveau de la sécurité, de la fiabilité et de la maintenabilité.

### Lier le plugin SonarLint à son instance SonarQube

Nous allons commencer par lier le plugin SonarLint à l'instance SonarQube pour synchroniser les règles de qualité de code ainsi que les problèmes détectés.\
Avant de commencer, copiez quelque part en dehors d'IntelliJ votre token d'authentification SonarQube, que vous avez déjà copié dans le fichier build.gradle.\
Pour cela, allez dans Settings → Tools → SonarLint et cliquez sur le bouton "+". Dans la fenêtre qui s'ouvre, entrez un nom pour la connexion (par exemple local), sélectionnez "SonarQube" et ajoutez l'URL de votre instance SonarQube (http://localhost:9000).\
Entrez enfin votre token d'authentification et cliquez sur "Next", encore "Next" et enfin "Create".

Ensuite, allez dans Settings → Tools → SonarLint → Project Settings et sélectionnez la connexion que vous venez de créer.\
Ensuite, cliquez sur "Search in the list" et sélectionnez le projet "workshop-dette-technique". Cliquer sur "OK".

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

Nous allons cibler certains éléments de maintenabilité et utiliser github Copilot afin de les corriger

### Maintenabilité dans le fichier profileApi 

Nous allons régler plusieurs problèmes de maintenabilité dans le fichier `ProfileApi.java`. 

Nous allons tout d'abord régler le problème de la méthode profileResponse à la ligne 70. Sélectionner le message d'erreur de SonarLint comme sur la capture d'écran suivante: 

![Alt text](profileApi.png)

Dans le chat de Github Copilot demandez lui par exemple:

```
/fix Use another way to initialize this instance.
```

Nous allons faire la même chose pour le problème sur Response Entity. Parcontre, cette erreur se retrouve à plusieurs endroits dans le fichier, nous allons donc sélectionner de la ligne 28 jusqu'à la ligne 74, puis dans le chat de Github Copilot demandez lui par exemple:

```
/fix Raw use of parameterized class 'ResponseEntity' 
Provide the parametrized type for this generic.
```

<details>
    <summary>Solution</summary>
    
```java
    @GetMapping
    public ResponseEntity<HashMap<String, ProfileData>> getProfile(
        @PathVariable String username, @AuthenticationPrincipal User user) {
      return profileQueryService
          .findByUsername(username, user)
          .map(this::profileResponse)
          .orElseThrow(ResourceNotFoundException::new);
    }
    
    @PostMapping(path = "follow")
    public ResponseEntity<HashMap<String, ProfileData>> follow(
        @PathVariable String username, @AuthenticationPrincipal User user) {
      return userRepository
          .findByUsername(username)
          .map(
              target -> {
                FollowRelation followRelation = new FollowRelation(user.getId(), target.getId());
                userRepository.saveRelation(followRelation);
                return profileResponse(profileQueryService.findByUsername(username, user).get());
              })
          .orElseThrow(ResourceNotFoundException::new);
    }
    
    @DeleteMapping(path = "follow")
    public ResponseEntity<HashMap<String, ProfileData>> unfollow(
        @PathVariable String username, @AuthenticationPrincipal User user) {
      Optional<User> userOptional = userRepository.findByUsername(username);
      if (userOptional.isPresent()) {
        User target = userOptional.get();
        return userRepository
            .findRelation(user.getId(), target.getId())
            .map(
                relation -> {
                  userRepository.removeRelation(relation);
                  return profileResponse(profileQueryService.findByUsername(username, user).get());
                })
            .orElseThrow(ResourceNotFoundException::new);
      } else {
        throw new ResourceNotFoundException();
      }
    }
    
    private ResponseEntity<HashMap<String, ProfileData>> profileResponse(ProfileData profile) {
      HashMap<String, ProfileData> map = new HashMap<>();
      map.put("profile", profile);
    
      return ResponseEntity.ok(map);
    }
```
</details>

[!WARNING]
Il est important de toujours contre-vérifier les modifications proposées par Github Copilot pour s'assurer qu'elles sont correctes. Particulièrement pour des changements sur plusieurs méthodes.



#### Utilisation de 'instanceof'

Dans la classe `GraphQLCustomizeExceptionHandler.java`, vous devriez voir un code smell sur la ligne 42.

Pour ce code smell, il n'y a pas de correction rapide disponible, pour l'exercice nous allons utiliser Github Copilot.

Pour cela, ouvrez le chat de Github Copilot et demandez lui un fix pour la description du code smell, par exemple:

```
/fix Replace this instanceof check and cast with 'instanceof ConstraintViolationException constraintviolationexception' Only generate the snippet
```

Il vous proposera alors un code de correction que vous pourrez copier coller dans votre IDE, en validant que la solution proposée est correcte.


#### Utilisation des références de méthode dans les lambdas

Si vous allez dans la classe `ArticleFavoriteApi.java`, plusieurs enjeux de maintenabilité sont présent. Sélectionner le message d'erreur de SonarLint comme sur la capture d'écran suivante: 

![Alt text](ArticleFavoriteApi.png)

Dans le chat de Github Copilot demandez lui par exemple:

```
/fix Call "Optional#isPresent()" or "!Optional#isEmpty()" before accessing the value.
```

Vous devriez avoir un résultat qui ressemble à ceci:

```java
  @PostMapping
  public ResponseEntity favoriteArticle(
      @PathVariable String slug, @AuthenticationPrincipal User user) {
    Article article =
        articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    ArticleFavorite articleFavorite = new ArticleFavorite(article.getId(), user.getId());
    articleFavoriteRepository.save(articleFavorite);
      Optional<ArticleData> optionalArticleData = articleQueryService.findBySlug(slug, user);
      if (optionalArticleData.isEmpty()) {
          throw new ResourceNotFoundException();
      }
      return responseArticleData(optionalArticleData.get());
  }
```

Vous pouvez par la suite sélectionner la méthode unfavoriteArticle, puis dans le chat peser sur la flèche du haut pour réutiliser le dernier message

le résultat devrait ressembler à ceci:

```java
    @DeleteMapping
    public ResponseEntity unfavoriteArticle(
        @PathVariable String slug, @AuthenticationPrincipal User user) {
      Article article =
          articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
      articleFavoriteRepository
          .find(article.getId(), user.getId())
          .ifPresent(
              favorite -> {
                articleFavoriteRepository.remove(favorite);
              });
      Optional<ArticleData> optionalArticleData = articleQueryService.findBySlug(slug, user);
      if (optionalArticleData.isEmpty()) {
          throw new ResourceNotFoundException();
      }
      return responseArticleData(optionalArticleData.get());
    }
```

#### Mauvais initialisation de HashMap avec duplication

Dans le fichier ArticleDataFetcher.java, nous allons corriger les trois mauvaises initialisations de HashMap qui sont dupliquées.
Pour ce faire sélectionner la ligne 300 à 357, puis dans le chat de Github Copilot demandez lui par exemple:

```
move initialization of hashmap in method
```

Il devrait vous proposer une nouvelle méthode ainsi que les modifications aux trois méthodes existantes.

[!TIP]
Lorsque Copilot vous propose des changements, mais qu'il n'inclut pas complètement le code( Il met un commentaire `//...` par exemple), vous pouvez peser sur la flèche du haut dans le chat pour que votre dernier message soit réutilisé, puis ajouter dans votre message generate all the code


#### Nettoyage des imports non-utilisés

Utilisez l'inspection du code de IntelliJ pour vous aider à supprimer toute référence à des imports inutiles.

Sur le projet, sélectionnez `Analyze->Inspect Code`, dans le résultat naviguez dans `Inspection Results -> Java -> Imports -> Unused import` et avec le bouton droit choisir `Delete unnecessary imports` pour nettoyer l'ensemble du projet.

## Félicitation !

La dette technique et la qualité sont améliorées, vous voila ainsi prêt pour passer à la dernière étape: [Mise en place de la méthode "Clean as you code"](COMPLEXITY.md)
