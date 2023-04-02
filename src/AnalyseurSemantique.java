public class AnalyseurSemantique {
    private Compilateur compilateur;
    private AnalyseurLexical analyseurLexical;
    private AnalyseurSyntaxique analyseurSyntaxique;
    int NB_CONST_CHAINE;
    String[] VAL_DE_CONST_CHAINE = new String[1000];
    int DERNIERE_ADRESSE_VAR_GLOB = -1;


    TableIdentificateurs tableIdentificateurs = new TableIdentificateurs();

    public AnalyseurSemantique(Compilateur compilateur, AnalyseurSyntaxique analyseurSyntaxique) {
        this.compilateur = compilateur;
        this.analyseurLexical = compilateur.analyseurLexical;
        this.analyseurSyntaxique = analyseurSyntaxique;
    }

    public boolean DEFINIR_CONSTANTE(String nom, T_UNILEX ul) throws Exception {
        T_ENREG_IDENT enreg;

        if (tableIdentificateurs.CHERCHER(nom)) {
            compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction de DEFINIR_CONSTANTE: identificateur existe déjà";
            compilateur.ERREUR(5);
        }
        if (ul == T_UNILEX.ent) {
            enreg = new Constante(nom, 0, compilateur.NOMBRE); //la constante de type entier(0)
        } else {
            NB_CONST_CHAINE = NB_CONST_CHAINE + 1;
            VAL_DE_CONST_CHAINE[NB_CONST_CHAINE] = compilateur.CHAINE;
            enreg = new Constante(nom, 1, NB_CONST_CHAINE);
        }
        tableIdentificateurs.INSERER(nom, enreg);
        return true;
    }

    public boolean DEFINIR_VAR (String nom) throws Exception {
        T_ENREG_IDENT enreg;

        if (tableIdentificateurs.CHERCHER(nom)) {
            compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction de DEFINIR_VAR: identificateur existe déjà";
            compilateur.ERREUR(5);
        }
        DERNIERE_ADRESSE_VAR_GLOB = DERNIERE_ADRESSE_VAR_GLOB + 1;

        enreg = new Variable(nom, 0, DERNIERE_ADRESSE_VAR_GLOB);
        tableIdentificateurs.INSERER(nom, enreg);
        //A verifier
        compilateur.interpreteur.MEM_VAR[DERNIERE_ADRESSE_VAR_GLOB] = 0;
        return true;
    }

    public boolean IDENT_EXIST() throws Exception {
        return tableIdentificateurs.CHERCHER(compilateur.CHAINE);
    }

    public void AFF_VERIFICATION() throws Exception {
        T_ENREG_IDENT id = tableIdentificateurs.getIdentificateur(compilateur.CHAINE);
        int id_val;
        if(id instanceof Constante) {
            id_val = ((Constante) id).getTypc();
            if(id_val != 0)
                compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction d'AFFECTATION: variable doit être de type entier";
            compilateur.ERREUR(5); //Constante de type chaine
        }
    }

    public void TERME_VERIFICATION() throws Exception {
        T_ENREG_IDENT id = tableIdentificateurs.getIdentificateur(compilateur.CHAINE);
        int id_val;
        if(id instanceof Constante) {
            id_val = ((Constante) id).getTypc();
            if (id_val != 0)
                compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction d'un TERME: Variable doit être de type entier";
            compilateur.ERREUR(5); //Constante de type chaine
        }
    }
}
