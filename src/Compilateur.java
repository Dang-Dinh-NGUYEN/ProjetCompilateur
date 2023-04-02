import java.io.*;
import java.nio.charset.StandardCharsets;

public class Compilateur {
    public static final int LONG_MAX_IDENT = 20;
    public static final int LONG_MAX_CHAINE = 50;
    public static final int NB_MOTS_RESERVES = 12;
    public static final int MAXINT = 32767;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    String SOURCE;
    int CARLU;
    int NOMBRE;
    String CHAINE;
    int NUM_LIGNE;
    String[] TABLE_MOTS_RESERVES = new String[NB_MOTS_RESERVES];
    String MESSAGE_ERREUR;

    BufferedReader bf;

    AnalyseurLexical analyseurLexical;
    AnalyseurSyntaxique analyseurSyntaxique;
    Interpreteur interpreteur;

    public void ERREUR(int n) throws Exception {
        switch (n) {
            case 1:
                System.out.println("fin de fichier atteinte _ ligne n°" + NUM_LIGNE);
                break;
            case 2:
                System.out.println("dépassé la valeur maximale _ ligne n°" + NUM_LIGNE);
                System.exit(0);
                break;
            case 3:
                System.out.println("dépassé la longueur maximale _ ligne n°" + NUM_LIGNE);
                System.exit(0);
                break;
            case 4:
                System.out.println("invalide identificateur _ ligne n°" + NUM_LIGNE);
                System.exit(0);
                break;
            case 5:
                System.out.println(ANSI_RED + "\nERREUR SYNTAXIQUE _ ligne n°" + NUM_LIGNE + ":\n" + MESSAGE_ERREUR + ANSI_RESET);
                System.exit(0);
                break;
            case 6:
                System.out.println(ANSI_RED + MESSAGE_ERREUR + ANSI_RESET);
                break;
        }
    }

    public void INITIALISER() throws IOException {
        NUM_LIGNE = 1;
        SOURCE = "C:\\Users\\Dang Dinh NGUYEN\\Documents\\L3_INFO\\S6\\ProjetCompilateur\\src\\TEST1.MP";
        try {
            bf = new BufferedReader(new FileReader(SOURCE));
            analyseurLexical = new AnalyseurLexical(this);
            analyseurSyntaxique = new AnalyseurSyntaxique(this);
            interpreteur = new Interpreteur(this);
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier source n'a pas été trouvé");
            System.exit(0);
        }
        TABLE_MOTS_RESERVES = new String[]{"PROGRAMME", "DEBUT", "FIN", "CONST",
                "VAR", "ECRIRE", "LIRE", "SI", "ALORS", "SINON", "TANTQUE", "FAIRE"};
    }

    public void TERMINER() throws Exception {
        bf.close();
    }

    public static void main(String[] args) throws Exception {
        Compilateur compilateur = new Compilateur();
        compilateur.INITIALISER();
        compilateur.analyseurLexical.LIRE_CAR();
        /*
        while (compilateur.CARLU!=-1) {
            T_UNILEX token = compilateur.analyseurLexical.ANALEX();
            compilateur.analyseurLexical.AFFICHER(token);
        }
         */
        compilateur.analyseurSyntaxique.ANASYNT();
        compilateur.TERMINER();
        compilateur.interpreteur.CREER_FICHIER_CODE(compilateur.SOURCE);
        compilateur.interpreteur.INTERPRETER();

    }
}

