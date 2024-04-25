# Workshop: Libérez-vous de la dette technique grâce à l'IA et au refactoring automatisé : cas concret avec SonarQube, GitHub Copilot et Open Rewrite
## Setup de l'environnement

### Création d'une image à partir d'un Dockerfile et démarrage du conteneur

Pour créer une image à partir d'un Dockerfile et démarrer un conteneur, suivez les étapes ci-dessous :

1. Assurez-vous d'avoir Docker installé sur votre machine. Si ce n'est pas le cas, vous pouvez le télécharger et l'installer à partir du site officiel de Docker.

2. Placez-vous dans le répertoire contenant le Dockerfile que vous souhaitez utiliser pour créer l'image.

3. Ouvrez une fenêtre de terminal ou une invite de commande et exécutez la commande suivante pour construire l'image :

    ```shell
    docker build --pull --rm -f "dockerfile" -t workshopsonar:latest "."
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