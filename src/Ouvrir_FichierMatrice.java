package rhc.rhm_sou;


import java.io.*;
class Ouvrir_FichierMatrice{
private String[][] Contenue_Text_Fichier;
private int nbr_li;
private int nbr_col;
BufferedReader br=null;
String[] infoMat=new String[2];//tab qui contient les id des des vehicules de la ligne lu
int i=0;
String Ligne_lu="";
//--------------------------Fonction pour ouvrir un fichier txt et le mettre dans une matrice--------------------------------
Ouvrir_FichierMatrice(String Nom_Fichier){
try{
br=new BufferedReader( new FileReader(Nom_Fichier));//on lit le fichier
}
catch(Exception e){System.out.println("Echèque de lecture du fichier "+Nom_Fichier);}
try{
Ligne_lu=br.readLine();//on récupére le contenu de la ligne du fichier
infoMat=Ligne_lu.split(" ");//on enregistre le contenu récupéré précédement dans ce tableau
nbr_li=Integer.parseInt(infoMat[0]);//le nbr de ligne correspond a la case1 du tab infomat
nbr_col=Integer.parseInt(infoMat[1]);//le nbr de ligne correspond a la case2 du tab infomat
Contenue_Text_Fichier=new String[nbr_li][nbr_col];//création d'une matrice (nbr_li X nbr_col)
while((Ligne_lu=br.readLine())!=null){ // tanque la ligne lu du fichier n'est pas null, on continue de lire le fichier tous en remplissant le tab a 2dimensions
Contenue_Text_Fichier[i]=Ligne_lu.split(" ");
i++;
}
}
catch(Exception e){}
try{
br.close(); // il est important de fermer le fichier à la fin 
}
catch(Exception e){System.out.println("Erreur de fermeture");}
}
//----------------------------Fonction pour récupérer le contenu de la matrice qu'on a rempli apartir du fichier txt------------
public String[][] recuperer_Contenue_Texte_Fichier(){
	return Contenue_Text_Fichier;
}
//---------------Fonction pour récupérer le nbr de lignes de la matrice--------------------------------------------------------
public int recuperer_nbr_ligne(){
    return this.nbr_li;
}
//---------------Fonction pour récupérer le nbr de collones de la matrice--------------------------------------------------------
public int recuperer_nbr_colonne(){
    return this.nbr_col;
}
}

