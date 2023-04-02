import java.io.FileNotFoundException;

public class AnalyseurLexical {
    private final Compilateur compilateur;
    public AnalyseurLexical(Compilateur compilateur) throws FileNotFoundException {
        this.compilateur = compilateur;
    }

    public void LIRE_CAR() throws Exception {
        compilateur.CARLU = compilateur.bf.read();
        if (compilateur.CARLU == -1) {
            compilateur.ERREUR(1);
            return;
        }
        if (compilateur.CARLU == '\n') {
            compilateur.NUM_LIGNE++;
        }
    }

    public void SAUTER_SEPARATEURS() throws Exception {
        while(compilateur.CARLU == ' ' || compilateur.CARLU == '\t' || compilateur.CARLU == '\n' || compilateur.CARLU == '\r') {
            LIRE_CAR();
        }

        if (compilateur.CARLU == '{') {
            //commentaires imbriqués
            int count = 1;
            while (count > 0) {
                LIRE_CAR();
                if (compilateur.CARLU == '{')
                    count++;
                if (compilateur.CARLU == '}')
                    count--;
            }
            LIRE_CAR();
            SAUTER_SEPARATEURS();
        }
    }

    public T_UNILEX RECO_ENTIER() throws Exception {
        int n = 0;
        while (Character.isDigit((char) compilateur.CARLU)) {
            n = n * 10 + ((char)compilateur.CARLU - '0');
            if (n >Compilateur. MAXINT) {
                compilateur.ERREUR(2);
            }
            LIRE_CAR();
        }
        if(Character.isAlphabetic((char) compilateur.CARLU))
            compilateur.ERREUR(4);
        compilateur.NOMBRE = n;

        //System.out.print(compilateur.NOMBRE + " >> ");
        return T_UNILEX.ent;
    }

    public T_UNILEX RECO_CHAINE() throws Exception {
        //chaines imbriquées
        LIRE_CAR();
        while (compilateur.CARLU != '\'') {
            compilateur.CHAINE += (char)compilateur.CARLU;
            LIRE_CAR();
        }
        LIRE_CAR();
        if(compilateur.CARLU == '\'') {
            compilateur.CHAINE += (char)compilateur.CARLU;
            return RECO_CHAINE();
        }

        if(compilateur.CHAINE.length() > Compilateur.LONG_MAX_CHAINE)
            compilateur.ERREUR(3);

        //System.out.print(compilateur.CHAINE + " >> ");
        return T_UNILEX.ch;
    }

    public T_UNILEX RECO_IDENT_OU_MOT_RESERVE() throws Exception {
        compilateur.CHAINE = "";
        while (Character.isLetterOrDigit((char) compilateur.CARLU) || (char) compilateur.CARLU == '_') {
            if (compilateur.CHAINE.length() < Compilateur.LONG_MAX_IDENT) {
                compilateur.CHAINE += (char)compilateur.CARLU;
            }
            LIRE_CAR();
        }
        compilateur.CHAINE = compilateur.CHAINE.toUpperCase();

        //System.out.print(compilateur.CHAINE + " >> ");
        return EST_UN_MOT_RESERVE(compilateur.CHAINE)? T_UNILEX.motcle : T_UNILEX.ident;
    }

    public boolean EST_UN_MOT_RESERVE(String chaine) {
        for (String motCle : compilateur.TABLE_MOTS_RESERVES) {
            if (motCle.equals(chaine))
                return true;
        }
        return false;
    }

    public T_UNILEX RECO_SYMB() throws Exception {
        switch (compilateur.CARLU) {
            case ';':
                //System.out.print((char) compilateur.CARLU + " >> " + T_UNILEX.ptvirg);
                return T_UNILEX.ptvirg;
            case ',':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.virg;
            case '.':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.point;
            case '(':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.parouv;
            case ')':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.parfer;
            case '<':
                //System.out.print((char) compilateur.CARLU + " >> ");
                LIRE_CAR();
                if (compilateur.CARLU == '=') {
                    //System.out.print((char) compilateur.CARLU + " >> ");
                    return T_UNILEX.infe;
                }
                else if (compilateur.CARLU == '>'){
                    //System.out.print((char) compilateur.CARLU + " >> ");
                    return T_UNILEX.diff;
                }
                else {
                    //System.out.print(" >> ");
                    return T_UNILEX.inf;
                }
            case '>':
                //System.out.print((char) compilateur.CARLU + " >> ");
                LIRE_CAR();
                if (compilateur.CARLU == '=') {
                    //System.out.print((char) compilateur.CARLU + " >> ");
                    return T_UNILEX.supe;
                }
                //System.out.print(" >> ");
                return T_UNILEX.sup;
            case '=':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.eg;
            case '+':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.plus;
            case '-':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.moins;
            case '*':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.mult;
            case '/':
                //System.out.print((char) compilateur.CARLU + " >> ");
                return T_UNILEX.divi;
            case ':':
                //System.out.print((char) compilateur.CARLU + " >> ");
                LIRE_CAR();
                if (compilateur.CARLU == '=') {
                    //System.out.print((char) compilateur.CARLU + " >> ");
                    return T_UNILEX.aff;
                }
                return T_UNILEX.deuxspts;
        }
        return null;
    }

    public void AFFICHER(T_UNILEX t_unilex){
        switch (t_unilex){
            case ent:
                System.out.println(compilateur.NOMBRE + " >> " + t_unilex);
                break;
            case ch:
            case ident:
            case motcle:
                System.out.println(compilateur.CHAINE + " >> " + t_unilex);
                break;
            case virg:
                System.out.println(", >> " + t_unilex);
                break;
            case ptvirg:
                System.out.println("; >> " + t_unilex);
                break;
            case point:
                System.out.println(". >> " + t_unilex);
                break;
            case parouv:
                System.out.println("( >> " + t_unilex);
                break;
            case parfer:
                System.out.println(") >> " + t_unilex);
                break;
            case inf:
                System.out.println("< >> " + t_unilex);
                break;
            case infe:
                System.out.println("<= >> " + t_unilex);
                break;
            case diff:
                System.out.println("<> >> " + t_unilex);
                break;
            case sup:
                System.out.println("> >> " + t_unilex);
                break;
            case supe:
                System.out.println(">= >> " + t_unilex);
                break;
            case plus:
                System.out.println("+ >> " + t_unilex);
                break;
            case moins:
                System.out.println("- >> " + t_unilex);
                break;
            case mult:
                System.out.println("* >> " + t_unilex);
                break;
            case divi:
                System.out.println("/ >> " + t_unilex);
                break;
            case eg:
                System.out.println("= >> " + t_unilex);
                break;
            case aff:
                System.out.println(":= >> " + t_unilex);
                break;
            case deuxspts:
                System.out.println(": >> " + t_unilex);
                break;
            default:
                System.out.println(" >> " + t_unilex);
                break;
        }
    }

    public T_UNILEX ANALEX() throws Exception {
        compilateur.CHAINE = "";
        // saute les séparateurs et commentaires
        SAUTER_SEPARATEURS();

        // essaie de reconnaître une unité lexicale
        if (Character.isDigit((char) compilateur.CARLU))
            return RECO_ENTIER();
        if (compilateur.CARLU == '\'')
            return RECO_CHAINE();
        if (Character.isLetter((char) compilateur.CARLU))
            return RECO_IDENT_OU_MOT_RESERVE();

        T_UNILEX t_unilex =  RECO_SYMB();
        LIRE_CAR();
        return t_unilex;
    }

}
