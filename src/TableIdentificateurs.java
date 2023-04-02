import java.util.ArrayList;
import java.util.HashMap;

class TableIdentificateurs {
    private static final int NB_IDENT_MAX = 100;
    private static HashMap<String, T_ENREG_IDENT> identTab = new HashMap<>(NB_IDENT_MAX);

    public boolean CHERCHER(String nom) {
        return identTab.containsKey(nom);
    }

    public void INSERER(String nom, T_ENREG_IDENT gerne) {
        identTab.put(nom,gerne);
    }

    public T_ENREG_IDENT getIdentificateur(String nom){
        return identTab.get(nom);
    }

    public HashMap<String, T_ENREG_IDENT> getIdentTab() {
        return identTab;
    }

    void AFFICHE_TABLE_IDENT() {
        System.out.println("Table des identificateurs :");
        System.out.println("Nom | Genre | typ");
        for (String i : identTab.keySet()) {
            System.out.print(i + " | " + identTab.get(i).toString() + " | ");
            if(identTab.get(i) instanceof Variable) {
                System.out.print(((Variable) identTab.get(i)).getTypv() + " Adresse: ");
                System.out.println(((Variable) identTab.get(i)).getAdrv());
            }
            if(identTab.get(i) instanceof Constante)
                System.out.println(((Constante) identTab.get(i)).getTypc());
        }
        System.out.println();
    }
}