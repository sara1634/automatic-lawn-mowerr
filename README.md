# automatic-lawn-mowerr

***Automatic-lawn-mowerr***
Simulation de tondeuses écrite en Scala

***OBJECTIF ET CONTRAINTES***

-Programme : en Scala 2.12
-Le style fonctionnel (types, immutabilité, fonctions totales, minimiser les effets de bord, gérer les cas d’erreur avec les structures adaptées,..)
-Test : build.sbt 

***Environnement Technique ***
- Scala version 2.12.8
- SBT(Scala build tool) version 0.13
- Git 

***Installation***
1. Installez JDK8 sur votre machine
2. Changez le Variable système JAVA_HOME pointant vers JDK8
3. $ JAVA_HOME \ bin est présent dans votre chemin système
4. Instalez SBT (sinon, consultez: http://www.scala-sbt.org/download.html)
5. SBT_installation_path \ bin est présent dans la variable de chemin système.
6. Installez GIT sur votre machine

***Exécution***
Il vous faut entrer la commande sbt run. Il y a ensuite deux cas de figure:
- L'utilisateur peut fournir le chemin d'un fichier décrivant le jardin ainsi que les tondeuses.
- Si aucun argument n'est fourni par l'utilisateur, le fichier mowers.txt situé dans le répertoire resources sera utilisé.
- Le fichier Result.txt situé dans le répertoire resources sera utilisé indiquant la situation finale du tondeuze.

***Choix d'implémentation***

1. Model 
Dans ce package, nous allons définir les différents objets suivantes : 

*Coordinate : Cette classe nous permettra de représenter des coordonnées ( la position du tondeuze + la taille du jardin )
*Direction : cette classe représente une direction cardinale oriontale ( North, East, South, West)
*Mower : cette classe représentate une tondeuse , qui se compose par un objet Coordinate correspondant à la position de la tondeuse ainsi un objet Direction indiquant la direction cardinale à laquelle la tondeuse fait face.
*Garden : cette classe nous permet de représenter un jardin, elle compte deux champs :( un object Coordinate correspondant aux coordonnées du coin supérieur droit du jardin + une liste de tondeuses)

2.Parser 
Dans ce package, nous allons définir les différents objets suivantes : 

*Parser : cette classe permettant de parser des listes de chaînes de caractères afin de créer des jardins ou des tondeuses et d'effectuer des actions sur nos tondeuses.

*PrintParser : elle  fourni une méthode générique print , qui prend en paramètre une preuve implicite de l'implantion du PrintParser pour renvoyer la chaîne de caractères correspondante.

***Test***
Pour les tests unitaires ,  j 'ai utilisé les librairies Scalatest et Scalacheck.
