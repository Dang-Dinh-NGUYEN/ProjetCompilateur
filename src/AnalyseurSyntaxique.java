public class AnalyseurSyntaxique {
    T_UNILEX UNILEX;

    private Compilateur compilateur;
    private AnalyseurLexical analyseurLexical;
    private AnalyseurSemantique analyseurSemantique;
    //private Interpreteur interpreteur;

    public AnalyseurSyntaxique(Compilateur compilateur){
        this.compilateur = compilateur;
        analyseurLexical = this.compilateur.analyseurLexical;
        analyseurSemantique = new AnalyseurSemantique(compilateur,this);
    }

    public AnalyseurSemantique getAnalyseurSemantique(){
        return this.analyseurSemantique;
    }

    public boolean PROG() throws Exception {
        System.out.println("ANALYSE PROG...");
        if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("PROGRAMME")) {
            UNILEX = analyseurLexical.ANALEX();
            if (UNILEX == T_UNILEX.ident) {
                UNILEX = analyseurLexical.ANALEX();
                if (UNILEX == T_UNILEX.ptvirg) {
                    UNILEX = analyseurLexical.ANALEX();
                    if (DECL_CONST())
                        UNILEX = analyseurLexical.ANALEX();
                    if (DECL_VAR())
                        UNILEX = analyseurLexical.ANALEX();
                    if (BLOC()) {
                        UNILEX = analyseurLexical.ANALEX();
                        if (UNILEX == T_UNILEX.point){
                            compilateur.interpreteur.GENCODE_STOP();
                            return true;
                        }
                        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de déclaration d'un PROG: '.' attendu";
                        compilateur.ERREUR(5);
                    }
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de déclaration d'un PROG: BLOC attendu";
                } else {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de déclaration d'un PROG: ';' attendu";
                }
            } else {
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de déclaration d'un PROG: identificateur attendu";
            }
            compilateur.ERREUR(5);
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de déclaration d'un PROG: mot-clé 'PROGRAMME' attendu";
        compilateur.ERREUR(5);
        return false;
    }

    public boolean DECL_CONST() throws Exception {
        System.out.println("ANALYSE DECL_CONST...");
        boolean non_fin;
        String nom_constante;
        if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("CONST")) {
            UNILEX = analyseurLexical.ANALEX();
            if (UNILEX == T_UNILEX.ident) {
                nom_constante = compilateur.CHAINE;
                UNILEX = analyseurLexical.ANALEX();
                if (UNILEX == T_UNILEX.eg) {
                    UNILEX = analyseurLexical.ANALEX();
                    if (UNILEX == T_UNILEX.ch || UNILEX == T_UNILEX.ent) {
                        if (analyseurSemantique.DEFINIR_CONSTANTE(nom_constante, UNILEX)) {
                            UNILEX = analyseurLexical.ANALEX();
                            non_fin = true;
                            while (non_fin) {
                                if (UNILEX == T_UNILEX.virg) {
                                    UNILEX = analyseurLexical.ANALEX();
                                    if (UNILEX == T_UNILEX.ident) {
                                        nom_constante = compilateur.CHAINE;
                                        UNILEX = analyseurLexical.ANALEX();
                                        if (UNILEX == T_UNILEX.eg) {
                                            UNILEX = analyseurLexical.ANALEX();
                                            if (UNILEX == T_UNILEX.ch || UNILEX == T_UNILEX.ent) {
                                                if (analyseurSemantique.DEFINIR_CONSTANTE(nom_constante, UNILEX)) {
                                                    UNILEX = analyseurLexical.ANALEX();
                                                    non_fin = true;
                                                } else {
                                                    non_fin = false;
                                                }
                                            } else {
                                                non_fin = false;
                                            }
                                        } else {
                                            non_fin = false;
                                        }
                                    } else {
                                        non_fin = false; //erreur syntaxique dans une instruction de déclaration d'une constante: identificateur attendu
                                    }
                                } else {
                                    non_fin = false;
                                }
                            }
                            if (UNILEX == T_UNILEX.ptvirg) {
                                System.out.println();
                                return true;
                            } else {
                                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_CONST: ';' attendu";
                                compilateur.ERREUR(5);
                            }
                        } else {
                            compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_CONST: type de constante attendu";
                            compilateur.ERREUR(5);
                        }
                    }
                } else {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_CONST: '=' attendu";
                    compilateur.ERREUR(5);
                }
            } else {
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_CONST: identificateur attendu";
                compilateur.ERREUR(5);
            }
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_CONST: mot-clé 'CONST' attendu";
        return false;
    }

    public boolean DECL_VAR() throws Exception {
        System.out.println("ANALYSE DECL_VAR...");
        boolean fin;
        String nom_variable;
        if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("VAR")) {
            UNILEX = analyseurLexical.ANALEX();
            if (UNILEX == T_UNILEX.ident) {
                nom_variable = compilateur.CHAINE;
                UNILEX = analyseurLexical.ANALEX();
                fin = false;
                if (analyseurSemantique.DEFINIR_VAR(nom_variable)) {
                    while (!fin) {
                        if (UNILEX == T_UNILEX.virg) {
                            UNILEX = analyseurLexical.ANALEX();
                            if (UNILEX == T_UNILEX.ident) {
                                nom_variable = compilateur.CHAINE;
                                if (analyseurSemantique.DEFINIR_VAR(nom_variable)) {
                                    UNILEX = analyseurLexical.ANALEX();
                                } else {
                                    fin = true;
                                }
                            } else {
                                fin = true;
                            }
                        } else {
                            fin = true;
                        }
                    }
                } else {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_VAR: identificateur attendu";
                    compilateur.ERREUR(5); //
                }
                if (UNILEX == T_UNILEX.ptvirg) {
                    System.out.println();
                    return true;
                } else {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_VAR: ';' attendu";
                    compilateur.ERREUR(5);
                }
            } else {
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_VAR: identificateur attendu";
                compilateur.ERREUR(5);
            }
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de DECL_VAR: mot-clé 'VAR' attendu";
        return false;
    }

    public boolean BLOC() throws Exception {
        System.out.println(Compilateur.ANSI_PURPLE + "ANALYSE BLOC..." + Compilateur.ANSI_RESET);
        boolean fin;
        if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("DEBUT")) {
            UNILEX = analyseurLexical.ANALEX();
            if (INSTRUCTION()) {
                fin = false;
                while (!fin) {
                    if (UNILEX == T_UNILEX.ptvirg) {
                        UNILEX = analyseurLexical.ANALEX();
                        if (!INSTRUCTION()) {
                            compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de BLOC: INSTRUCTION attendu";
                            fin = true;
                        }
                    } else {
                        fin = true;
                    }
                }
                if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("FIN")) {
                    return true;
                } else {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de BLOC: mot-clé 'FIN' attendu";
                    compilateur.ERREUR(5);
                }
            } else {
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de BLOC: INSTRUCTION attendu";
                compilateur.ERREUR(5);
            }
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de BLOC: mot-clé 'DEBUT' attendu";
        compilateur.ERREUR(5);
        return false;
    }

    public boolean INSTRUCTION() throws Exception {
        System.out.println(Compilateur.ANSI_BLUE + "ANALYSE D'INSTRUCTION..." + Compilateur.ANSI_RESET);
        return AFFECTATION() || LECTURE() || ECRITURE() || BLOC();
    }

    //public boolean INST_NON_COND() throws Exception {
        //System.out.println(Compilateur.ANSI_BLUE + "ANALYSE D'INSTRUCTION NON CONDITION..." + Compilateur.ANSI_RESET);
        //return AFFECTATION() || LECTURE() || ECRITURE() || BLOC() || INST_REPE();
    //}

    //public boolean INST_COND(){}

    //public boolean INST_REPE(){}

    public boolean AFFECTATION() throws Exception {
        System.out.println(Compilateur.ANSI_CYAN + "ANALYSE D'AFFECTATION..." + Compilateur.ANSI_RESET);
        if (UNILEX == T_UNILEX.ident) {
            //vérifier si la variable a été déclaré
            if (analyseurSemantique.IDENT_EXIST()) {
                analyseurSemantique.AFF_VERIFICATION();
                compilateur.interpreteur.GENCODE_AFFECTATION_IDENT(compilateur.CHAINE);
                UNILEX = analyseurLexical.ANALEX();
                if (UNILEX == T_UNILEX.aff) {
                    UNILEX = analyseurLexical.ANALEX();
                    if(EXP()){
                        compilateur.interpreteur.GENCODE_AFFECTATION_AFFE();
                        System.out.println();
                        return true;
                    }
                    else {
                        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'AFFECTATION: EXP attendu";
                        compilateur.ERREUR(5);
                    }
                } else {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'AFFECTATION: ':=' attendu";
                    compilateur.ERREUR(5);
                }
            } else {
                compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction d'AFFECTATION: identificateur n'est pas déclaré";
                compilateur.ERREUR(5);
            }
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'AFFECTATION: identificateur attendu";
        return false;
    }

    public boolean LECTURE() throws Exception {
        System.out.println(Compilateur.ANSI_CYAN + "ANALYSE LECTURE..." + Compilateur.ANSI_RESET);
        boolean fin;
        boolean erreur = false;
        if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("LIRE")) {
            UNILEX = analyseurLexical.ANALEX();
            if (UNILEX == T_UNILEX.parouv) {
                UNILEX = analyseurLexical.ANALEX();
                if (UNILEX == T_UNILEX.ident) {
                    if(analyseurSemantique.IDENT_EXIST()) {
                        compilateur.interpreteur.GENCODE_LECTURE(compilateur.CHAINE);
                        UNILEX = analyseurLexical.ANALEX();
                        fin = false;
                        erreur = false;
                        while (!fin) {
                            if (UNILEX == T_UNILEX.virg) {
                                UNILEX = analyseurLexical.ANALEX();
                                if (UNILEX == T_UNILEX.ident) {
                                    if(analyseurSemantique.IDENT_EXIST()) {
                                        compilateur.interpreteur.GENCODE_LECTURE(compilateur.CHAINE);
                                        UNILEX = analyseurLexical.ANALEX();
                                    } else {
                                        fin = true;
                                    }
                                } else {
                                    fin = true;
                                    erreur = true;
                                }
                            } else {
                                fin = true;
                            }
                        }
                    } else {
                        compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction de LECTURE: identificateur n'est pas déclaré";
                        compilateur.ERREUR(5);
                    }
                    if (erreur) {
                        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de LECTURE: identificateur attendu";
                        compilateur.ERREUR(5);
                    } else if (UNILEX == T_UNILEX.parfer) {
                        UNILEX = analyseurLexical.ANALEX();
                        System.out.println();
                        return true;
                    } else {
                        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de LECTURE: ')' attendu";
                        compilateur.ERREUR(5);
                    }
                }
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de LECTURE: identificateur attendu";
                compilateur.ERREUR(5);
            }
            compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de LECTURE: '(' attendu";
            compilateur.ERREUR(5);
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de LECTURE: mot-clé 'LIRE' attendu";
        return false;
    }

    public boolean ECRITURE() throws Exception {
        System.out.println(Compilateur.ANSI_CYAN + "ANALYSE ECRITURE..." + Compilateur.ANSI_RESET);
        boolean fin;
        boolean erreur;
        if (UNILEX == T_UNILEX.motcle && compilateur.CHAINE.equals("ECRIRE")) {
            UNILEX = analyseurLexical.ANALEX();
            if (UNILEX == T_UNILEX.parouv) {
                UNILEX = analyseurLexical.ANALEX();
                erreur = false;
                if (ECR_EXP()) {
                    UNILEX = analyseurLexical.ANALEX();
                    fin = false;
                    while (!fin) {
                        if (UNILEX == T_UNILEX.virg) {
                            UNILEX = analyseurLexical.ANALEX();
                            erreur = !(ECR_EXP());
                            if (erreur) fin = true;
                        } else {
                            fin = true;
                        }
                    }
                } else {
                    compilateur.interpreteur.GENCODE_ECRL();
                }
                if (erreur) {
                    compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'ECRITURE: ECR_EXP non-valable";
                    compilateur.ERREUR(5);
                }
                if (UNILEX == T_UNILEX.parfer) {
                    UNILEX = analyseurLexical.ANALEX();
                    System.out.println();
                    return true;
                }
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'ECRITURE: ')' attendu";
            } else {
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'ECRITURE: '(' attendu";
            }
            compilateur.ERREUR(5);
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'ECRITURE: mot-clé 'ECRIRE' attendu";
        return false;
    }

    public boolean ECR_EXP() throws Exception {
        System.out.println("ANALYSE ECR_EXP...");
        if (EXP()) {
            compilateur.interpreteur.GENCODE_ECR_EXP();
            return true;
        }
        if (UNILEX == T_UNILEX.ch){
            compilateur.interpreteur.GENCODE_ECRC(compilateur.CHAINE);
            return true;
        }
        return false;
    }

    public boolean EXP() throws Exception {
        System.out.println("ANALYSE EXP...");
        if (TERME()) {
            return SUITE_TERME();
        }
        return false;
    }

    public boolean SUITE_TERME() throws Exception {
        System.out.println("ANALYSE SUITE_TERME...");
        if (OP_BIN()) {
            if(EXP()) {
                compilateur.interpreteur.GENCODE_EXP_FIN();
                return true;
            }
            compilateur.ERREUR(5);
        }
        return true; //cas vide
    }

    public boolean TERME() throws Exception {
        System.out.println("ANALYSE TERME...");
        if (UNILEX == T_UNILEX.ent) {
            compilateur.interpreteur.GENCODE_TERME_ENT();
            UNILEX = analyseurLexical.ANALEX();
            return true;
        } else if (UNILEX == T_UNILEX.ident) {
            if (analyseurSemantique.IDENT_EXIST()) {
                analyseurSemantique.TERME_VERIFICATION();
                compilateur.interpreteur.GENCODE_TERME_IDENT(compilateur.CHAINE);
                UNILEX = analyseurLexical.ANALEX();
                return true;
            } else {
                compilateur.MESSAGE_ERREUR = "erreur sémantique dans une instruction d'un TERME: identificateur n'est pas déclaré";
                compilateur.ERREUR(5);
            }
        } else if (UNILEX == T_UNILEX.parouv) {
            UNILEX = analyseurLexical.ANALEX();
            if (!EXP()) {
                compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'un TERME: EXP attendu";
                compilateur.ERREUR(5);
            }
            UNILEX = analyseurLexical.ANALEX();
            if (UNILEX == T_UNILEX.parfer) {
                UNILEX = analyseurLexical.ANALEX();
                return true;
            }
            compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'un TERME: ')' attendu";
            compilateur.ERREUR(5);
        } else if (UNILEX == T_UNILEX.moins) {
            UNILEX = analyseurLexical.ANALEX();
            if(TERME()){
                compilateur.interpreteur.GENCODE_TERME_MOIN();
                return true;
            }else{
                return false;
            }
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction d'un TERME: TERME attendu";
        return false;
    }

    public boolean OP_BIN() throws Exception {
        System.out.println("ANALYSE OP_BIN...");
        if (UNILEX == T_UNILEX.plus || UNILEX == T_UNILEX.moins || UNILEX == T_UNILEX.mult || UNILEX == T_UNILEX.divi) {
            compilateur.interpreteur.GENCODE_OP_BIN(UNILEX);
            UNILEX = analyseurLexical.ANALEX();
            return true;
        }
        compilateur.MESSAGE_ERREUR = "erreur syntaxique dans une instruction de OP_BIN: '+','-','*','/' attendu";
        return false;
    }

    public void ANASYNT() throws Exception{
        UNILEX = analyseurLexical.ANALEX();
        if (PROG()) {
            System.out.println(Compilateur.ANSI_GREEN + "Le PROG source est syntaxiquement correcte\n" + Compilateur.ANSI_RESET);
            analyseurSemantique.tableIdentificateurs.AFFICHE_TABLE_IDENT();
        }else {
            compilateur.ERREUR(5);
        }
    }

}

