
Résolution d'un casse-tête : Rush HourTM
1. Introduction
On s'intéresse dans ce projet à la résolution d'un casse-tête intitulé Rush
Hour. Il est possible d'y jouer en ligne, par exemple via le lien :
http://www.mah-jongg.ch/rushhour/rushhour.swf
Le but du jeu est de faire sortir une voiture rouge (unique) d'un embouteillage
sur une grille 6  6. Sur cette grille sont positionnés des voitures
(deux cases de long) et des camions (trois cases de long) horizontalement
ou verticalement. Les véhicules ne peuvent pas tourner, autrement dit ils
se déplacent uniquement sur leur rangée de départ horizontale ou verticale.
Une conguration de départ du jeu est décrite par une carte comme celle
représentée sur la gure de gauche ci-dessous. On positionne alors les véhicules
(15 au maximum) sur la grille comme indiqué sur la carte. La voiture
rouge (pointée par la èche) est toujours positionnée horizontalement sur la
troisième ligne, la sortie étant située à l'extrémité droite de cette ligne.
Une solution pour cette conguration de départ consiste à déplacer la
voiture verte d'une case vers la droite, le camion violet d'une case vers le
haut, la voiture orange d'une case vers le haut, le camion vert de deux cases
vers la gauche, la voiture turquoise de trois cases vers la gauche, le camion
bleu de deux cases vers le bas, le camion jaune de trois cases vers le bas, et
enn la voiture rouge de trois cases vers la droite. On considère le jeu résolu
dès que la voiture rouge est positionnée sur les deux cases devant la sortie.
Toute conguration où la voiture rouge est ainsi positionnée est appelée
conguration-but. Le but du jeu est de passer d'une conguration de départ
à une conguration-but par une séquence de déplacements de véhicules.
Deux fonctions objectifs seront considérées :
(a) minimiser le nombre de mouvements : un mouvement consiste à dé-
placer un véhicule d'un nombre quelconque de cases ; par exemple, la
solution décrite ci-dessus comporte 8 mouvements ;
(b) minimiser le nombre total de cases de l'ensemble des mouvements : c'est
la version pondérée de la fonction objectif précédente, puisque chaque
mouvement coûte le nombre de cases de déplacement ; par exemple, il
y a 16 cases de déplacement au total dans la solution décrite plus haut.
Pour l'objectif (a) on parlera du problème RHM (Rush Hour Mouvements),
et pour l'objectif (b) du problème RHC (Rush Hour Cases).
Question 1. Montrer que, pour une même conguration de départ, une solution
optimale pour RHC n'est pas nécessairement optimale pour RHM. Pour
ce faire, on pourra utiliser une conguration de départ où la voiture rouge
est positionnée comme dans l'exemple de l'introduction, et qui comporte en
tout trois voitures (en comptant la rouge) et deux camions.
Les problèmes RHM et RHC sont diciles d'un point de vue computationnel.
Le projet consiste à développer une ou deux méthodes de résolution,
l'une fondée sur la programmation linéaire en variables binaires, l'autre sur
les graphes, et à développer une interface graphique (qui peut être minimaliste)
pour décrire les solutions trouvées. An de faciliter le débogage, on
commencera par développer l'interface graphique.
2. Congurations de départ et achage
Il y a 40 congurations de départ de diculté croissante fournies avec la
version physique du jeu. Elles sont stockées sous la forme de chiers textes
à télécharger sur le site web de MOGPL :
http://www-poleia.lip6.fr/~perny/ANDROIDE1617/MOGPL/
Les chiers sont classés en quatre niveaux : débutant (jam1.txt à jam10.txt),
intermédiaire (jam11.txt à jam20.txt), avancé (jam21.txt à jam30.txt)
et expert (jam31.txt à jam40.txt). Le chier jam1.txt par exemple se
présente sous la forme suivante :
6 6
c1 c1 0 0 0 t1
t2 0 0 t3 0 t1
t2 g g t3 0 t1
t2 0 0 t3 0 0
c2 0 0 0 c3 c3
c2 0 t4 t4 t4 0
La première ligne du chier indique le nombre de lignes et de colonnes
de la grille. Précisons que toutes les grilles considérées dans ce projet seront
de taille 66. Néanmoins, la possibilité de changer la taille de la grille peut
aider au débogage du code. Chaque ligne suivante dans le chier correspond
à une ligne de la grille. Le caractère 0 signie une case libre, tandis qu'une
case occupée comporte le code du véhicule qui l'occupe. Le caractère g indique
les cases occupées par la voiture rouge. Les codes des autres voitures
débutent par le caractère c (pour car), et les codes des camions débutent
par le caractère t (pour truck). Par exemple, dans jam1.txt, la voiture c1
occupe les deux premières cases à gauche de la première ligne, et le camion
t1 occupe les trois premières cases en haut de la dernière colonne.
Remarque : pour aider au débogage, trois congurations tests particulièrement
simples sont également fournies (test1.txt à test3.txt).
Question 2. Coder une fonction de lecture d'un chier d'entrée, qui génère
une structure de données représentant la conguration de départ. La structure
choisie devra permettre d'accéder en (1) à n'importe quelle case de la
grille (ainsi qu'à l'identiant du véhicule qui l'occupe si la case est occupée).
Question 3. Coder une fonction d'achage qui prend en entrée la structure
de données représentant une conguration quelconque et qui ache la grille.
L'achage peut se faire dans la console tant qu'il reste lisible.
Les parties 3 et 4 peuvent être traitées indépendamment. Il
n'est pas obligatoire d'aborder les deux parties, mais il faut en
traiter au moins une. Toutefois pour pouvoir dépasser la note de 15 il est
nécessaire d'avoir traité les deux parties an de comparer les deux méthodes.
3. Résolution par programmation linéaire en variables binaires
Cette section est consacrée à une méthode de résolution de Rush Hour fond
ée sur la programmation linéaire en variables binaires. An d'identier la
position d'un véhicule, on considérera la position de son marqueur, qui correspond
au numéro de la case la plus en haut à gauche occupée par ce véhicule.
La numérotation des cases est indiquée sur la grille ci-dessous. Par exemple,
si une voiture (resp. un camion) occupe les cases 14 et 15 (resp. 16, 22 et
28), alors son marqueur est positionné en 14 (resp. 16).
1 2 3 4 5 6
7 8 9 10 11 12
13 14 15 16 17 18 sortie
19 20 21 22 23 24
25 26 27 28 29 30
31 32 33 34 35 36
Pour pouvoir formuler les problèmes RHC et RHM comme des programmes
mathématiques, il est nécessaire de s'imposer un nombre maximum
autorisé N de mouvements de véhicules (pour avoir un nombre ni de variables)
pour résoudre le casse-tête. Ce nombre N est xé par l'utilisateur
avant de lancer la résolution. Les valeurs de N à utiliser pour les expérimentations
numériques sont indiquées dans la section 5.
Les variables gurant dans le programme mathématique comportent toutes
au moins trois indices : l'identiant i du véhicule concerné (il peut être pratique
de coder l'identiant comme une chaîne de caractères, correspondant
au nom du véhicule), l'indice j 2 f1; : : : ; 36g de la position concernée dans
la grille, et enn l'indice k 2 f0; : : : ;Ng du tour de jeu (les variables indicées
par k = 0 correspondent à la conguration de départ du jeu). Il y a trois
types de variables (toutes binaires) :
xi;j;k = 1 si le marqueur du véhicule i est en position j au terme du keme
mouvement, 0 sinon ;
zi;j;k = 1 si le véhicule i occupe la position j (notons qu'une voiture occupe
deux positions, et un camion trois positions) au terme du keme mouvement,
0 sinon ;
yi;j;l;k = 1 si le marqueur du véhicule i est passé de la position j à la position
l lors du keme mouvement, 0 sinon.
An d'exprimer les diérentes contraintes du problème, on fera appel aux
notations suivantes :
vi : longueur (en nombre de cases) du véhicule i ;
mi;j : ensemble des positions occupées par le véhicule i quand le marqueur
i est en position j ;
pj;l : ensemble des positions comprises entre les positions j et l.
Les contraintes permettant de caractériser les positions occupées par les
diérents véhicules sont les suivantes :
vixi;j;k 
X
m2mi;j
zi;m;k 8i; j; k (1)
X
i
zi;j;k  1 8j; k (2)
X
j
zi;j;k = vi 8i; k (3)
yi;j;l;k  1 􀀀
X
i06=i
zi0;p;k􀀀1 8i; j; l; k 8p 2 pj;l (4)
En s'appuyant sur la position j du marqueur du véhicule i, la contrainte (1)
assure que zi;m;k = 1 pour toute case m occupée par i au terme du keme
mouvement. La contrainte (2) assure qu'il n'y ait pas plusieurs véhicules
occupant une même case j au terme du keme mouvement, et la contrainte (3)
renforce la formulation en établissant que seules vi cases sont occupées par
le véhicule i dans sa rangée. Enn, la contrainte (4) assure qu'un véhicule
i ne peut se déplacer d'une case j à une case l lors du keme mouvement
(yi;j;l;k = 1) que si les cases comprises entre j et l sont libres.
Question 4. Donner l'expression de la fonction objectif :
a. si l'on souhaite résoudre RHM (minimiser le nombre de mouvements) ;
b. si l'on souhaite résoudre RHC (minimiser le nombre total de cases
déplacement).
Question 5. Donner les contraintes, impliquant les variables xi;j;k et yi;j;l;k,
qui assurent que :
a. la voiture rouge est positionnée devant la sortie au terme du dernier
mouvement ;
b. au plus un véhicule est déplacé par tour ;
c. la position du marqueur d'un véhicule i est bien mise à jour si le véhicule
i se déplace.
Question 6. Expliquer pourquoi seul un sous-ensemble de toutes les variables
binaires potentielles doivent être introduites dans le modèle.
Question 7. Coder la fonction qui fait appel un solveur de programmation
linéaire en variables binaires (Gurobi) pour résoudre Rush Hour. La solution
devra être achée sous la forme d'une séquence de déplacements à réaliser
an de résoudre le casse-tête, et non pas sous la forme d'une liste de
variables binaires avec leurs valeurs (ce qui serait rapidement fastidieux, et
dicilement intelligible pour un non-initié !). L'achage en mode texte dans
la console est accepté.
4. Résolution par l'algorithme de Dijkstra
Une autre méthode de résolution de Rush Hour consiste à appliquer l'algorithme
de Dijkstra dans le graphe des congurations du jeu. Le graphe des
congurations est un graphe non-orienté où chaque sommet correspond à
une conguration réalisable (c'est-à-dire sans chevauchement de véhicules,
et atteignable depuis la conguration de départ) et où il y a une arête entre
deux congurations si il est possible de passer de l'une à l'autre en déplaçant
un véhicule d'une ou plusieurs cases.
A titre d'illustration, considérons la conguration de départ suivante (sur
une grille volontairement réduite an que la taille du graphe des congurations
reste contenue), où le rectangle hachuré représente la voiture rouge :
Pour cette conguration de départ, l'ensemble des 16 congurations réalisables
est listé ci-dessous (les deux conguration-buts sont marquées par
une astérisque) :
Le graphe des congurations est représenté ci-dessous. La valeur d'une
arête correspond au nombre de cases de déplacement pour passer d'une con-
guration à l'autre.
Si l'objectif est de minimiser le nombre total de cases de déplacement
(RHC), la résolution du casse-tête proposé consiste alors à appliquer l'algorithme
de Dijkstra pour trouver une plus courte chaîne de la conguration
de départ à une conguration-but. Sur l'exemple, il s'agit de trouver une
plus courte chaîne du sommet 0 vers un des sommets 2 ou 12.
Notons qu'en pratique le graphe n'est pas donné explicitement (on ne
dispose pas de la liste des congurations et des arêtes) mais qu'il est dé-
ni en compréhension : étant donnée une conguration, on est capable de
déterminer les congurations voisines.
An de résoudre RHC et RHM par l'algorithme de Dijkstra, il sera donc
nécessaire d'implémenter une fonction déterminant les congurations voisines
d'une conguration (en recensant les déplacements possibles de vé-
hicules). Pour chaque nouveau sommet du graphe des congurations ainsi
découvert, on sera amené à réaliser une copie de la grille de jeu et à la modi-
er pour simuler le déplacement d'un véhicule (d'une ou plusieurs cases vers
la gauche, la droite, le haut ou le bas).
Question 8. Coder une méthode de résolution de RHC fondée sur l'utilisation
de l'algorithme de Dijkstra dans le graphe des congurations. Quelle
est la condition d'arrêt de l'algorithme ?
Question 9. Coder une fonction qui permet d'acher la séquence de dé-
placements à réaliser an de résoudre le casse-tête depuis une conguration
de départ donnée en entrée (l'achage en mode texte dans la console est
accepté), ainsi que la valeur correspondante de la fonction objectif.
Question 10. Expliquer comment adapter la méthode de la question 8 si l'on
souhaite résoudre RHM (minimiser le nombre de mouvements). Implanter
cette fonctionnalité dans le code.
Question 11. Expliquer comment adapter la méthode la question 8 si l'on
souhaite compter le nombre de congurations réalisables (en un nombre quelconque
de mouvements de véhicules) depuis la conguration de départ. Implanter
cette fonctionnalité dans le code.
5. Expérimentations numériques
Tester la (les) méthode(s) de résolution que vous avez codée(s) sur les 40
congurations de départ fournies sur le site web du module. Chaque exécution
devra tourner au plus deux minutes, au terme desquelles sera retourn
ée la meilleure solution trouvée jusqu'alors si une solution a été trouvée,
et aucune solution dans le cas contraire. Pour la méthode fondée sur la
programmation linéaire en variables binaires, on posera N = 14 pour les
congurations de niveau débutant, N = 25 pour les congurations de niveau
intermédiaire, N = 31 pour les congurations de niveau avancé, et N = 50
pour les congurations de niveau expert.
Question 12. Fournir les temps de résolution obtenus pour chaque méthode
implantée et chaque instance ou groupe d'instances, sous la forme d'un tableau
ou d'une courbe. Pour la méthode fondée sur l'algorithme de Dijkstra,
il pourra être intéressant également d'étudier le comportement en fonction
du nombre de congurations réalisables depuis la conguration de départ.
