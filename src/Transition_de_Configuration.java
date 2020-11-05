
package rhc.rhm_sou;


public class Transition_de_Configuration {
    private String[][] configuration_Suivante;
    private int nbr_transitions;
    private int deplacement;
    private String description;
    Transition_de_Configuration(String[][] conf2,int dep,int nbr_tr,String desc){
        description=desc;
        nbr_transitions=nbr_tr;
        configuration_Suivante=RhcRhm_sou.copyMatrice(conf2);
        deplacement=dep;
        if(dep!=0)description=desc;
        else description="Configuration initiale";
    }
    
    boolean compare(Transition_de_Configuration conf1){


    int i,j,k;
        
        for(j=0;j<configuration_Suivante.length;j++)
            for(k=0;k<configuration_Suivante[0].length;k++) if(configuration_Suivante[j][k].compareTo(conf1.conf_suivante()[j][k])!=0)return false;
        return true;
    }
    

    
    public  String[][] conf_suivante(){
        return configuration_Suivante;
    }
    
    public int obtenir_deplacement(){
        return deplacement;
    }
    public String obtenir_descrption(){
        return description;
    }
public int obtenir_nbrtransition(){
    return nbr_transitions;
}    
}
