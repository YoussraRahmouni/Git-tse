# info4

# Contributors:
Antoine MALIFARGE
Alban THOMÉ
Xavier PALIX
Valentin LEROY
Youssra RAHMOUNI


# Dépendances : 

Cette application recquiert JAVA SE 8, elle utilise les libairies java :
-java.awt
-java.io
-java.util
-javax.swing
-org.gitlab4j.api (à ajouter dans le pom.xml si besoin)
-java.nio
-com.github.cliftonlabs.json_simple (à ajouter dans le pom.xml si besoin)
-org.JSON (à ajouter dans le pom.xml si besoin)
-java.net
-java.beans

# Javadoc :

Pour accéder à la javadoc, il faut ouvrir l'index.html dans le dossier javadoc

# Utilisation : 

## Page de connexion :

Après avoir lancé l'application, vous arrivez sur la page de connexion.
Rentrez nom.prénom et votre mot de passe Gitlab telecom dans les champs respectifs, 
puis appuyez sur entrer ou cliquer sur le bouton comfirmer pour vous connecter.
Vous pouvez afficher votre mot de passe en cochant la case blanche.

## L'arbre :

Après connexion, vous arrivez sur la page d'accueil. Sur la gauche de cette page se trouve un arbre représentant les groupes et projets dont vous faites parti ainsi que leurs membres. Vous pouvez les sélectionner en cliquant dessus. Vous pouvez en séléctionner plusieurs à la fois.

## Page d'accueil :

Sur la page d'accueil, vous avez 5 boutons. Le bouton "?" en haut à droite ouvre une fenêtre d'aide expliquant le fonctionnement de l'applicatiion.
Le bouton ajouter vous permet de créer un nouveau groupe ou projet ou aussi un nouveau groupe avec un nouveau projet, il faut renseigner les champs correspondants et cliquer sur le bouton créer. Vous pouvez annuler l'action en cliquant sur la croix rouge ou sur le bouton annuler.
Le bouton supprimer suprime les élémets sélectionnés dans l'arbre. Un pop-up de confirmation vous demande de confirmer votre choix.
Le bouton archiver archive les élémets sélectionnés dans l'arbre. Un pop-up de confirmation vous demande de confirmer votre choix.
Le bouton importcsv vous permet de créer des groupes et projets avec un fichier csv de format : nom.prenom; groupe; projet.
Si vous avez un profil bob, vous avez accès à un un autre bouton avec un champ à renseigner. En entrant nom.prenom d'un autre utilisateur gitlab et cliquant sur ajouter au milieu de l'écran vous donnez un profil bob à l'utilisateur renseigné. La liste des utilisateurs avec le profil bob est sauvegardée localement. 

## Onglet projets archives :

Dans cet onglet, la liste de vos projets archivés et de leur branches principales est affichée.

## Onglet statistiques :

Dans cet onglet, différents statistiques et information sur le projet séléctionné dans l'arbre sont affichés. Un message d'erreur est affiché si l'objet sélectionné dans l'arbre n'est pas un projet ou que plusieurs sont séléctionnés. Seul les projets dont vous êtes Owner ont l'affichage des statistiques. Le bouton + en haut à gauche ouvre une fenêtre avec une vision avancée des statistiques.

## Onglet groupes : 

Dans cet onglet, les membres du groupe sélectionné dans l'arbre sont affichés. Un message d'erreur est affiché si l'objet séléctionné dans l'arbre n'est pas un groupe ou que plusieurs sont sélectionnés. Seul les groupes dont vous êtes Owner ont l'affichage. Le bouton gérer les groupes en haut à gauche sert à ajouter ou supprimer un membre du groupe en renseignant le champ avec le format suivant : nom1.prenom1,nom2.prenom2 et en cliquant sur ajouter ou supprimer.Le bouton voir fichier sert à ouvrir un fichier local de sauvegarde des groupes gitlab crée par l'application.

## Onglet module :

Si vous avez un profil bob, vous avez accès à un onglet module.
Pour créer un module, il faut remplir le champs Nom du module et cliquer sur créer.
Pour supprimer un module, il faut sélectionner le module à supprimer dans le menu déroulant en haut à droite et cliquer sur supprimer.
Pour gérer les projets et groupes dans un module, il faut sélectionner dans l'arbre les éléments à ajouter/supprimer, sélectionner dans le menu déroulant le module auquel vous voulez ajouter/supprimer les éléments et cliquer sur le bouton correspondant. 
