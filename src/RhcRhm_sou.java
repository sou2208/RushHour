/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rhc.rhm_sou;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author didi
 */
public class RhcRhm_sou {
//--------------------Variables----------------------------------------------------------------------------------------------
public static int nbr_lignes_Matrice=6;
public static int nbr_colonne_Matrice=6;
public static String[][] Configuration_Initiale=new String[6][6];
public static int position_goal=17;
public static String marquer_voiture_rouge="g";
public static List<Transition_de_Configuration> solution_courte=new ArrayList<Transition_de_Configuration>();
public static List<Transition_de_Configuration> liste_transitions_precedente=new ArrayList<Transition_de_Configuration>();
public static List<Transition_de_Configuration> suite_courante=new ArrayList<Transition_de_Configuration>();
public static List<Transition_de_Configuration> nouvellesuite;
public static Vector chemin_parcouru=new Vector();       
public static Transition_de_Configuration trans_courante,nouvelletrans;
public static String[][]   transprecedente;
public static String chemin;
//------------Fonction de lecture de la matrice initiale--------------------------------------------------------------------
public static void Lire_configuration_Initiale(String chemin){
    Ouvrir_FichierMatrice ouv=new Ouvrir_FichierMatrice(chemin);
    Configuration_Initiale=ouv.recuperer_Contenue_Texte_Fichier();
    nbr_lignes_Matrice=ouv.recuperer_nbr_ligne();
    nbr_colonne_Matrice=ouv.recuperer_nbr_colonne();
    AfficherConfig(Configuration_Initiale);
}
//-----------Fonction d'affichage d'une  matrice-------------------------------------------------------------------------------
static void AfficherConfig(String[][] conf){    
    for(int i=0;i<nbr_lignes_Matrice;i++){
        for(int j=0;j<nbr_colonne_Matrice;j++){
            System.out.print(conf[i][j]+" ");
        }
        System.out.println();
    }    
}     

//----------------------Fonction qui retourne l'oriontation du véhicule (N=zero,H=horizontale,V=verticale)------------------
public static char obtenir_obj_par_position(String [][] conf,int li,int col){
//conf=config courante
if( conf[li][col].compareTo("0")==0) return 'N';
if(col!=0){
    if(conf[li][col-1].compareTo(conf[li][col])==0) {
        return 'N';
    }
}
if(li!=0){
    if(conf[li-1][col].compareTo(conf[li][col])==0) {
        return 'N';
    }
}
if(col+1<nbr_colonne_Matrice){
    if(conf[li][col].compareTo(conf[li][col+1])==0){
        return 'H';
    }
}    
return 'V';  
}
//-----Fontion qui retourne la longeur d'un véhicule a condition que sa position soit H ou V---------------------------------
public static int obtenir_longueur(String [][] conf,int li,int col){
int taille=0;
char orientation=obtenir_obj_par_position(conf,li,col);
if(orientation!='N'){
    if(orientation=='H'){  
        for(int i=col;i<nbr_colonne_Matrice&&(conf[li][col].compareTo(conf[li][i])==0);i++){
            taille++;
        }
    }
    else {
        for(int i=li;i<nbr_lignes_Matrice&&(conf[li][col].compareTo(conf[i][col])==0);i++){
            taille++;
        }
    }
}
return taille;
}


//----------Fonction qui retourne le nombre de case sur lesquelles on peu se déplacer-----------------------------------------
public static int nbr_deplacements_possibles(String [][] conf,int li,int col,int dirrection){
int cases_vides=0;
char orientation=obtenir_obj_par_position(conf,li,col);
if(orientation!='N'){
    if(dirrection>0){//marcher en avant
        int longueur=obtenir_longueur(conf,li,col);
        if(orientation=='H') {//si on est positioné horizontalement
            for(int i=col+longueur;i<nbr_colonne_Matrice&&(conf[li][i].compareTo("0")==0);i++){
                //si la case d'acoté =0 alrs on incrémente le nbr de sauts qu'on peut faire
                cases_vides++;
            }
        }
        else{//si on est positioné verticalement
            for(int i=li+longueur;i<nbr_lignes_Matrice&&(conf[i][col].compareTo("0")==0);i++)
            cases_vides++;      
        }
    }
    else {//marcher en arriére
        if(orientation=='H') {
        for(int i=col-1;i>=0&&(conf[li][i].compareTo("0")==0);i--){
            cases_vides++;
        }}
        else{
            for(int i=li-1;i>=0&&(conf[i][col].compareTo("0")==0);i--){
            cases_vides++;  }    
        }
    }
}
return cases_vides;
}


//--------Fonction copie matrice------------------------------------------------------------------------------------
public static String[][] copyMatrice(String[][] mat){
if(mat==null){
    return null;
}
String[][] newconfig=new String[nbr_lignes_Matrice][nbr_colonne_Matrice];
for(int i=0;i<nbr_lignes_Matrice;i++)
for(int j=0;j<nbr_colonne_Matrice;j++){
newconfig[i][j]=mat[i][j];
}
return newconfig;
}
//car si on met directe mat=newcofig si on modifi lune lotr sera modifiée osi


//----Fonction qui retourne la matrice apré déplacement-------------------------------------------------

public static String[][]  deplacement(String [][] conf,int li,int col,int dirrection, int nbr_pas){
    String[][] newconfig=new String[nbr_lignes_Matrice][nbr_colonne_Matrice];
    newconfig=copyMatrice(conf);
    int longueur=obtenir_longueur(conf,li,col);
    char orientation=obtenir_obj_par_position(conf,li,col);
    int i;
    if(nbr_deplacements_possibles(conf,li,col,dirrection)==0){
        return newconfig;
    }// Déplacement impossible dans la dirrection indiqée
    if(orientation!='N'){
        if(dirrection>0){
            if(orientation=='H')  for(i=col;i<col+nbr_pas;i++){
                newconfig[li][i]="0";
                newconfig[li][i+longueur]=newconfig[li][i+1];
            }
            else for(i=li;i<li+nbr_pas;i++){
                newconfig[i][col]="0";
                newconfig[i+longueur][col]=newconfig[i+1][col];
            }    
        } 
        else {
        if(orientation=='H')  
        for(i=col-nbr_pas;i<col;i++){
            newconfig[li][i]=newconfig[li][i+1];
            newconfig[li][i+longueur]="0";
        }
        else 
            for(i=li-nbr_pas;i<li;i++){
                newconfig[i][col]=newconfig[i+1][col];
                newconfig[i+longueur][col]="0";
            }     
        }
    }    
    return newconfig;
}
//-----------Fonction qui vérifie si la config a déja été parcouru----------------------------------------------------

public static boolean Configuration_deja_existante(Transition_de_Configuration transition, int longueur,Vector chemins){
int i,j,k,n=liste_transitions_precedente.size(),m;
//on parcour la liste liste_transitions_precedente tous en comparent avec la transi donnée pour  voir si elle existe ou pa
for(i=0;i<n;i++)
for(j=0;j<nbr_lignes_Matrice;j++)
for(k=0;k<nbr_colonne_Matrice;k++) 
if(liste_transitions_precedente.get(i).compare(transition)){//si la transi existe:
if(liste_transitions_precedente.get(i).obtenir_nbrtransition()>longueur){//si la longeur du chemin ds la liste est plu long ke celui de notre transition alr:
liste_transitions_precedente.remove(i);//on suprime cette transition de ntre liste
n=chemins.size();
for(i=0;i<n;i++)
if(((List<Transition_de_Configuration>) chemins.elementAt(i))!=null){//si le vecteur n'est pa vide
m=((List<Transition_de_Configuration>) chemins.elementAt(i)).size();
for(j=0;j<m;j++)
if(((List<Transition_de_Configuration>) chemins.elementAt(i)).get(j).compare(transition)){
chemins.setElementAt(null,i);
return false;}
}
}
//si la  n'existe :
return true;
}
               
//si la transi n'existe pas:
return false;
}



//--------Fonction qui retourne si l'objectif est atteint ou pas---------------------------------------------------------
public static boolean est_Goal(String[][] configuration_courante){
    if(configuration_courante[position_goal/nbr_colonne_Matrice][position_goal%nbr_colonne_Matrice].compareTo(marquer_voiture_rouge)==0) return true;
    //config_courante[2][5].compareTo("g")==0 alr wi nous avons atteint notre objectif 
    return false;   
}
//---Fonction qui resoud le RHC------------------------------------------------------------------------------------------------
static void Resoudre_RHC(){
    //on donne le chemin du fichier q'on veut analyser:
    chemin=javax.swing.JOptionPane.showInputDialog("Chemin du fichier de configuration initiale:");
    Lire_configuration_Initiale(chemin);
    //on met notre matrice initiale dans la liste suite_courante dont la description="matrice initiale":
    suite_courante.add(new Transition_de_Configuration(Configuration_Initiale, 0,0,""));
    //on met notre matrice initiale dans la liste liste_transitions_precedente dont la description="matrice initiale":
    liste_transitions_precedente.add(new Transition_de_Configuration( Configuration_Initiale, 0,0,""));
    //ont met la liste suite_courante qui ne contien que la mat initiale dans le vecteur chemin_parcouru:
    chemin_parcouru.add(suite_courante);
    int nombre_conf_possibles=resoudre_RHC(chemin_parcouru);
    affichage_sol(solution_courte);
    System.out.println("Nombre configurations possibles="+nombre_conf_possibles);
}
//-----------------------------------------------------------------------------------------------------------------------
public static int resoudre_RHC(Vector vecteur){
    int position,i,n=vecteur.size(),nombre_chemins_parcouru=liste_transitions_precedente.size();
    char orientation;
    for(i=n-1;i>=0;i--)
    if(((List<Transition_de_Configuration>) vecteur.elementAt(i))!=null){
        //si la taille de la liste solution_courte=0 ou bien taille liste solution_courte>liste(chem1parcouru[i]  :      
        if(solution_courte.size()==0 || solution_courte.size()>((List<Transition_de_Configuration>)vecteur.elementAt(i)).size()){
        //on récupére la premiére case de la premiére liste du vecteur ex pr iter2: vecteur.elem(0).get(vecteur.elem(0).size)-1=0)   
        trans_courante= ((List<Transition_de_Configuration>) vecteur.elementAt(i)).get(((List<Transition_de_Configuration>)vecteur.elementAt(i)).size()-1);
        //on fait une copie de la matrice courante et on la mé ds transprecedente
        transprecedente=trans_courante.conf_suivante();

        for(position=0;position<36;position++)
        //si notre vecteur n'est pas vide ce qui est le cas alr:
        if(((List<Transition_de_Configuration>)vecteur.elementAt(i))!=null){
            //on calcule l'oriontation de chaque vehicule:
            orientation=RhcRhm_sou.obtenir_obj_par_position(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice);
            //si la case ne contient pas un 0 et donc contient un véhicule:
            if(orientation!='N'){
                nouvellesuite=new ArrayList<Transition_de_Configuration>();
                //on ajoute vecteur[i] a la liste nouvelle suite:
                nouvellesuite.addAll(((List<Transition_de_Configuration>)vecteur.elementAt(i)));
                //si on peut se deplacer en avant alrs:
                if(nbr_deplacements_possibles(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,1)!=0){
                    nouvelletrans=new Transition_de_Configuration(deplacement(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,1,1),1,((List<Transition_de_Configuration>) vecteur.elementAt(i)).size()+1,"Déplacement = ("+transprecedente[position/nbr_colonne_Matrice][position%nbr_colonne_Matrice]+" , "+1+")");   
                    //si la config actuelle na pas deja été parcouru
                    if(!Configuration_deja_existante(nouvelletrans,((List<Transition_de_Configuration>)vecteur.elementAt(i)).size()+1,vecteur)){
                        liste_transitions_precedente.add(nouvelletrans);//on ajoute cette transi ds notre liste
                        nouvellesuite.add(nouvelletrans);//pareil
                        //si la voiture g se trouve ds la case[2][5] et que la taille de notre solution> a celle de la new suite
                        if(est_Goal(nouvelletrans.conf_suivante()) && (solution_courte.size()==0 || solution_courte.size()>nouvellesuite.size())){         
                            solution_courte=new ArrayList<Transition_de_Configuration>();
                            solution_courte.addAll(nouvellesuite);//on ajoute la nouvelle suite ds notre liste
                        } 
                        else vecteur.add(nouvellesuite);
                    }
                }
                nouvellesuite=new ArrayList<Transition_de_Configuration>();
                nouvellesuite.addAll(((List<Transition_de_Configuration>) vecteur.elementAt(i)));
                //si on peut se deplacer en arriére alrs:
                if(RhcRhm_sou.nbr_deplacements_possibles(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,-1)!=0){
                    nouvelletrans=new Transition_de_Configuration(RhcRhm_sou.deplacement(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,-1,1),1,((List<Transition_de_Configuration>)vecteur.elementAt(i)).size()+1,"Déplacement = ("+transprecedente[position/nbr_colonne_Matrice][position%nbr_colonne_Matrice]+" , "+(-1)+")");   
                    if(!Configuration_deja_existante(nouvelletrans,((List<Transition_de_Configuration>)vecteur.elementAt(i)).size()+1,vecteur)){
                        liste_transitions_precedente.add(nouvelletrans);    
                        nouvellesuite.add(nouvelletrans);
                        if(est_Goal(nouvelletrans.conf_suivante()) && (solution_courte.size()==0 || solution_courte.size()>nouvellesuite.size())){
                            solution_courte=new ArrayList<Transition_de_Configuration>();
                            solution_courte.addAll(nouvellesuite);
                        } 
                        else vecteur.add(nouvellesuite);   
                    }
                }
            }        
            if(orientation=='H')position++;
            }
            }
            vecteur.remove(i);
    }//fin premier if
    n=vecteur.size();
    for(i=n-1;i>=0;i--)
    if(((List<Transition_de_Configuration>) vecteur.elementAt(i))==null)
    vecteur.remove(i);
    //si toutes les suites de config ont été parcouru et qu'au- une transi est découverte alrs on fait appel a la récurssivité
    if(nombre_chemins_parcouru!=liste_transitions_precedente.size()) resoudre_RHC(vecteur);
    // sinon, l'algorithm sort car ttes les config possibles ont été parcourus 
    return liste_transitions_precedente.size();// on récupère le nombre de configurations possibles
}

//---Fonction qui resoud le RHM------------------------------------------------------------------------------------------------
static void Resoudre_RHM(){
  
        Lire_configuration_Initiale(chemin);
        List<Transition_de_Configuration> suite_courante=new ArrayList<Transition_de_Configuration>();
        
        
        solution_courte=new ArrayList<Transition_de_Configuration>();
        liste_transitions_precedente=new ArrayList<Transition_de_Configuration>();

        
        
        suite_courante.add(new Transition_de_Configuration(Configuration_Initiale, 0,0,""));
        liste_transitions_precedente.add(new Transition_de_Configuration( Configuration_Initiale, 0,0,""));
        Vector chemin_parcouru=new Vector();
        chemin_parcouru.add(suite_courante);
        
        resoudre_RHM(chemin_parcouru);
        affichage_sol(solution_courte);
        
}
//----------------------------------------------------------------------------------------------------------------------------
public static void resoudre_RHM(Vector Liste_chemin){
int i,n=Liste_chemin.size(),j,nombre_chemins_parcouru=liste_transitions_precedente.size();
Transition_de_Configuration trans_courante,nouvelletrans;
String[][]   transprecedente;
List<Transition_de_Configuration> nouvellesuite;
int position;
char orientation;
int nombre_mvt_possibles;
for(i=n-1;i>=0;i--)
if(((List<Transition_de_Configuration>) Liste_chemin.elementAt(i))!=null){//si le vecteur né pa vide
    if(solution_courte.size()==0 || solution_courte.size()>((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).size()){
        trans_courante= ((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).get(((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).size()-1);
        transprecedente=trans_courante.conf_suivante();
        for(position=0;position<36 ;position++)
        if(((List<Transition_de_Configuration>) Liste_chemin.elementAt(i))!=null){
            orientation=RhcRhm_sou.obtenir_obj_par_position(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice);
            if(orientation!='N'){
                nombre_mvt_possibles=nbr_deplacements_possibles(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,1);
                for(j=1;j<=nombre_mvt_possibles;j++){ // on parcour tou les déplacement possibles dans la résolution RHM au lieu de déplacer avec une seul case
                    nouvellesuite=new ArrayList<Transition_de_Configuration>();
                    nouvellesuite.addAll(((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)));
                    nouvelletrans=new Transition_de_Configuration(deplacement(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,1,j),j,((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).size()+1,"Déplacement = ("+transprecedente[position/nbr_colonne_Matrice][position%nbr_colonne_Matrice]+" , "+j+")");   
                    if(!Configuration_deja_existante(nouvelletrans,((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).size()+1,Liste_chemin)){
                        liste_transitions_precedente.add(nouvelletrans);
                        nouvellesuite.add(nouvelletrans);
                        if(est_Goal(nouvelletrans.conf_suivante()) && (solution_courte.size()==0 || solution_courte.size()>nouvellesuite.size())){
                            solution_courte=new ArrayList<Transition_de_Configuration>();
                            solution_courte.addAll(nouvellesuite);
                        }
                        else Liste_chemin.add(nouvellesuite);   
                    }
                }
                nombre_mvt_possibles=nbr_deplacements_possibles(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,-1);
                for(j=1;j<=nombre_mvt_possibles;j++){
                    nouvellesuite=new ArrayList<Transition_de_Configuration>();
                    nouvellesuite.addAll(((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)));
                    nouvelletrans=new Transition_de_Configuration(deplacement(transprecedente,position/nbr_colonne_Matrice,position%nbr_colonne_Matrice,-1,j),j,((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).size()+1,"Déplacement = ("+transprecedente[position/nbr_colonne_Matrice][position%nbr_colonne_Matrice]+" , "+(-j)+")");   
                    if(!Configuration_deja_existante(nouvelletrans,((List<Transition_de_Configuration>) Liste_chemin.elementAt(i)).size()+1,Liste_chemin)){
                        liste_transitions_precedente.add(nouvelletrans);    
                        nouvellesuite.add(nouvelletrans);
                        if(est_Goal(nouvelletrans.conf_suivante()) && (solution_courte.size()==0 || solution_courte.size()>nouvellesuite.size())){
                            solution_courte=new ArrayList<Transition_de_Configuration>();
                            solution_courte.addAll(nouvellesuite);
                        } 
                        else Liste_chemin.add(nouvellesuite);  
                    }
                }
            }        
            if(orientation=='H')position++;
        }
    }
    Liste_chemin.remove(i);
    }
    n=Liste_chemin.size();
    for(i=n-1;i>=0;i--)if(((List<Transition_de_Configuration>) Liste_chemin.elementAt(i))==null)Liste_chemin.remove(i);
    if(nombre_chemins_parcouru!=liste_transitions_precedente.size()) resoudre_RHM(Liste_chemin);
    
}
//------------------------FOCNTION QUI AFFICHE LA solution finale-------------------------------------------------------------



public static void affichage_sol(List<Transition_de_Configuration> solution){
    //Si pas de solutions trouvées:
    if(solution.size()==0){
        System.out.println("\nPas de solution");
        return;
    }
    //si on trouve une solution:
    int i,n=solution.size(),nbr_deplacement_total=0;
    System.out.println("\n\nSolution =");
    for(i=0;i<n;i++) {//on parcoure la Liste (solution)
        nbr_deplacement_total+=solution.get(i).obtenir_deplacement();
        System.out.println(solution.get(i).obtenir_descrption());
        System.out.println("_______________________");
        AfficherConfig(solution.get(i).conf_suivante());//on affiche la matrice i
        System.out.println("_______________________");
    }
    if((solution.size()-1)==nbr_deplacement_total)
        System.out.println("\n\nLongueur solution RHC="+(solution.size()-1));
    else 
        System.out.println("\n\nLongueur solution RHM="+(solution.size()-1));

}
//----Main-----------------------------------------------------------------------------------------------------------------------
public static void main(String [] args){
    Resoudre_RHC();
    Resoudre_RHM();
}
}
