# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé

## Résoudre un cas de complexité cognitive

Dans cet atelier, nous allons voir comment utiliser Github Copilot pour nous aider à résoudre un cas de complexité cognitive dans notre projet.
Un cas de complexité cognitive apparaît lorsqu'une méthode ou une classe est trop complexe à comprendre. Cela peut être dû à une longue liste de paramètres, une longue liste d'instructions, des conditions imbriquées, etc.
À terme cela peut rendre le code difficile à maintenir et à faire évoluer.

Pour les besoins du workshop, nous avons modifier une méthode pour la rendre volontaire trop complexe et faire en sorte que SonarQube le relève.
Cette méthode est `findUserFeed` dans la classe `ArticleQueryService`.

### Générer de la documentation

Une bonne chose à faire pour commencer à résoudre le problème est de comprendre ce que fait la méthode. Si malheureusement il n'y a pas de documentation, nous pouvons utiliser Github Copilot pour nous aider à en générer.
Pour cela, sélectionnez l'ensemble de la méthode `findUserFeed`, faites un clic droit sur la sélection et choisissez "Generate Docs" dans le menu "GitHub Copilot".
Le chat devrait vous répondre le bloc de code que vous avez sélectionné, précédé d'un bloc de documentation au format Javadoc.
Prenez le temps de lire la documentation générée et de la comparer avec ce que fait réellement la méthode.

### Simplifier la méthode

Maintenant que nous avons une documentation, nous pouvons commencer à simplifier la méthode.
Le moyen le plus simple de diminuer la complexité cognitive d'une méthode est de la diviser en plusieurs méthodes plus petites, chacune ayant une responsabilité unique.
Pour cela, nous allons utiliser Github Copilot pour nous aider à extraire des méthodes.\
Sélectionnez la méthode `findUserFeed`, ouvrez le chat Copilot et demandez-lui par exemple:
```
Réduit la complexité de la méthode findUserFeed en la séparant en plusieurs méthodes.
```