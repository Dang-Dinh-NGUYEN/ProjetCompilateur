public class AnalyseurSemantique {
    private Compilateur compilateur;
    private AnalyseurLexical analyseurLexical;
    private AnalyseurSyntaxique analyseurSyntaxique;
    TableIdentificateurs tableIdentificateurs = new TableIdentificateurs();

    public AnalyseurSemantique(Compilateur compilateur) {
        this.compilateur = compilateur;
        this.analyseurLexical = compilateur.analyseurLexical;
        this.analyseurSyntaxique = compilateur.analyseurSyntaxique;
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
            compilateur.NB_CONST_CHAINE = compilateur.NB_CONST_CHAINE + 1;
            compilateur.VAL_DE_CONST_CHAINE[compilateur.NB_CONST_CHAINE] = compilateur.CHAINE;
            enreg = new Constante(nom, 1, compilateur.NB_CONST_CHAINE);
        }
        tableIdentificateurs.INSERER(nom, enreg);
        return true;
    }
}
