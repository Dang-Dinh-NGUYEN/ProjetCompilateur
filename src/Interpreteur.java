import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Interpreteur {
    public static final int TAILLE_MAX_MEM = 10000;
    //MOT_MEM[] MEM = new MOT_MEM[TAILLE_MAX_MEM]; //la mémoire centrale

    Integer[] MEM_VAR = new Integer[TAILLE_MAX_MEM]; //la zone mémoire réservée aux variables globales
    Integer[] P_CODE = new Integer[TAILLE_MAX_MEM]; //la zone mémoire réservée au code machine
    Integer[] PILEX = new Integer[TAILLE_MAX_MEM]; //la zone mémoire réservée à la pile d'exécution
    int SOM_PILEX = 0; //la sommet de la pile d'exécution

    Integer[] PILOP = new Integer[TAILLE_MAX_MEM];
    int SOM_PILOP = -1;
    int CO = 0;

    private Compilateur compilateur;
    private AnalyseurSemantique analyseurSemantique;
    private StringBuilder stringBuilder = new StringBuilder();

    public Interpreteur(Compilateur compilateur) {
        this.compilateur = compilateur;
        AnalyseurSyntaxique analyseurSyntaxique = compilateur.analyseurSyntaxique;
        this.analyseurSemantique = analyseurSyntaxique.getAnalyseurSemantique();
    }

    public void GENCODE_AFFECTATION_IDENT(String ch){
        P_CODE[CO] = MOT_MEM.EMPI.ordinal();
        P_CODE[CO + 1] = ((Variable) analyseurSemantique.tableIdentificateurs.getIdentificateur(ch)).getAdrv();
        stringBuilder.append(MOT_MEM.EMPI.name()).append(" ").append(P_CODE[CO + 1]).append('\n');
        CO = CO + 2;

    }

    public void GENCODE_AFFECTATION_AFFE(){
        P_CODE[CO] = MOT_MEM.AFFE.ordinal();
        stringBuilder.append(MOT_MEM.AFFE.name()).append('\n');
        CO = CO + 1;

    }

    public void GENCODE_LECTURE(String ch){
        P_CODE[CO] = MOT_MEM.EMPI.ordinal();
        P_CODE[CO + 1] = ((Variable) analyseurSemantique.tableIdentificateurs.getIdentificateur(ch)).getAdrv();
        stringBuilder.append(MOT_MEM.EMPI.name()).append(" ").append(P_CODE[CO + 1]).append('\n');
        P_CODE[CO + 2] = MOT_MEM.LIRE.ordinal();
        stringBuilder.append(MOT_MEM.LIRE.name()).append('\n');
        CO = CO + 3;

    }

    public void GENCODE_ECRL(){
        P_CODE[CO] = MOT_MEM.ECRL.ordinal();
        stringBuilder.append(MOT_MEM.ECRL.name()).append('\n');
        CO = CO + 1;

    }

    public void GENCODE_ECRC(String ch){
        P_CODE[CO] = MOT_MEM.ECRC.ordinal();
        stringBuilder.append(MOT_MEM.ECRC.name());
        CO++;
        for(int i = 0; i < ch.length(); i++){
            P_CODE[CO + i] = (int) ch.charAt(i);
        }
        stringBuilder.append(" ").append(ch).append(" ");
        P_CODE[CO + ch.length()] = MOT_MEM.FINC.ordinal();
        stringBuilder.append(MOT_MEM.FINC.name()).append('\n');
        CO = CO + ch.length() + 1;

    }

    public void GENCODE_ECR_EXP(){
        P_CODE[CO] = MOT_MEM.ECRE.ordinal();
        stringBuilder.append(MOT_MEM.ECRE.name()).append('\n');
        CO = CO + 1;

    }

    public void GENCODE_EXP_FIN(){
        P_CODE[CO] = PILOP[SOM_PILOP];
        stringBuilder.append(MOT_MEM.values()[PILOP[SOM_PILOP]]).append('\n');
        SOM_PILOP = SOM_PILOP - 1;
        CO = CO +1;

    }

    public void GENCODE_OP_BIN(T_UNILEX t_unilex){
        SOM_PILOP = SOM_PILOP + 1;

        switch (t_unilex){
            case plus:
                PILOP[SOM_PILOP] = MOT_MEM.ADDI.ordinal();
                break;
            case moins:
                PILOP[SOM_PILOP] = MOT_MEM.SOUS.ordinal();
                break;
            case mult:
                PILOP[SOM_PILOP] = MOT_MEM.MULT.ordinal();
                break;
            case divi:
                PILOP[SOM_PILOP] = MOT_MEM.DIVI.ordinal();
                break;
        }
    }

    public void GENCODE_TERME_ENT(){
        P_CODE[CO] = MOT_MEM.EMPI.ordinal();
        P_CODE[CO + 1] = compilateur.NOMBRE;
        stringBuilder.append(MOT_MEM.EMPI).append(" ").append(P_CODE[CO + 1]).append('\n');
        CO = CO + 2;

    }

    public void GENCODE_TERME_IDENT(String ch){
        P_CODE[CO] = MOT_MEM.EMPI.ordinal();
        P_CODE[CO + 1] = ((Variable)analyseurSemantique.tableIdentificateurs.getIdentificateur(ch)).getAdrv();
        stringBuilder.append(MOT_MEM.EMPI.name()).append(" ").append(P_CODE[CO + 1]).append('\n');
        P_CODE[CO + 2] = MOT_MEM.CONT.ordinal();
        stringBuilder.append(MOT_MEM.CONT.name()).append('\n');
        CO = CO + 3;

    }

    public void GENCODE_TERME_MOIN(){
        P_CODE[CO] = MOT_MEM.MOIN.ordinal();
        stringBuilder.append(MOT_MEM.MOIN.name()).append('\n');
        CO = CO + 1;

    }

    public void GENCODE_STOP(){
        P_CODE[CO] = MOT_MEM.STOP.ordinal();
        stringBuilder.append(MOT_MEM.STOP.name());
        CO = CO + 1;

    }

    public void AFFICHER_CODE_GEN(){
        System.out.println(Arrays.toString(MEM_VAR));
        System.out.println("PILOP: ");
        System.out.println(Arrays.toString(PILOP));
        System.out.println("P_CODE: ");
        System.out.println(Arrays.toString(P_CODE));
        System.out.println();
        //System.out.println(stringBuilder.toString());

    }

    public void INTERPRETER() throws Exception {
        CO = 0;
        while (P_CODE[CO] != MOT_MEM.STOP.ordinal()){
            switch (P_CODE[CO]) {
                case 0: //ADDI
                    //System.out.println(Compilateur.ANSI_YELLOW + "ADDI");
                    PILEX[SOM_PILEX - 1] = PILEX[SOM_PILEX - 1] + PILEX[SOM_PILEX];
                    SOM_PILEX = SOM_PILEX - 1;
                    CO = CO + 1;
                    break;
                case 1: //SOUS
                    //System.out.println(Compilateur.ANSI_YELLOW + "SOUS");
                    PILEX[SOM_PILEX - 1] = PILEX[SOM_PILEX - 1] - PILEX[SOM_PILEX];
                    SOM_PILEX = SOM_PILEX - 1;
                    CO = CO + 1;
                    break;
                case 2: //MULT
                    //System.out.println(Compilateur.ANSI_YELLOW + "MULT");
                    PILEX[SOM_PILEX - 1] = PILEX[SOM_PILEX - 1] * PILEX[SOM_PILEX];
                    SOM_PILEX = SOM_PILEX - 1;
                    CO = CO + 1;
                    break;
                case 3: //DIVI
                    //System.out.println(Compilateur.ANSI_YELLOW + "DIVI");
                    if (PILEX[SOM_PILEX] == 0) {
                        compilateur.MESSAGE_ERREUR = "ERREUR D'EXECUTION: DIVISION PAR ZERO";
                        compilateur.ERREUR(6);
                    }
                    PILEX[SOM_PILEX - 1] = PILEX[SOM_PILEX - 1] / PILEX[SOM_PILEX];
                    SOM_PILEX = SOM_PILEX - 1;
                    CO = CO + 1;
                    break;
                case 4: //MOIN
                    //System.out.println(Compilateur.ANSI_YELLOW + "MOIN");
                    PILEX[SOM_PILEX] = -PILEX[SOM_PILEX];
                    CO = CO + 1;
                    break;
                case 5: //AFFE
                    //System.out.println(Compilateur.ANSI_YELLOW + "AFFE");
                    MEM_VAR[PILEX[SOM_PILEX - 1]] = PILEX[SOM_PILEX];
                    SOM_PILEX = SOM_PILEX - 2;
                    CO = CO + 1;
                    break;
                case 6: //LIRE
                    //System.out.println(Compilateur.ANSI_YELLOW + "LIRE");
                    Scanner scanner = new Scanner(System.in);
                    MEM_VAR[PILEX[SOM_PILEX]] = Integer.valueOf(scanner.nextLine());
                    SOM_PILEX = SOM_PILEX - 1;
                    CO = CO + 1;
                    break;
                case 7: //ECRL
                    //System.out.println(Compilateur.ANSI_YELLOW + "ECRL");
                    System.out.println();
                    CO = CO + 1;
                    break;
                case 8: //ECRE
                    //System.out.println(Compilateur.ANSI_YELLOW + "ECRE");
                    System.out.println(PILEX[SOM_PILEX]);
                    SOM_PILEX = SOM_PILEX - 1;
                    CO = CO + 1;
                    break;
                case 9: //ECRC
                    //System.out.println(Compilateur.ANSI_YELLOW + "ECRC");
                    int i = 1;
                    char ch = (char) P_CODE[CO + i].intValue();
                    while (P_CODE[CO + i] != MOT_MEM.FINC.ordinal()) {
                        System.out.print(ch);
                        i = i + 1;
                        ch = (char) P_CODE[CO + i].intValue();
                    }
                    //System.out.println(Compilateur.ANSI_YELLOW + "FINC");
                    CO = CO + i + 1;
                    break;
                case 11: //EMPI
                    //System.out.println(Compilateur.ANSI_YELLOW + "EMPI");
                    SOM_PILEX = SOM_PILEX + 1;
                    PILEX[SOM_PILEX] = P_CODE[CO + 1];
                    CO = CO + 2;
                    break;
                case 12: //CONT
                    //System.out.println(Compilateur.ANSI_YELLOW + "CONT");
                    PILEX[SOM_PILEX] = MEM_VAR[PILEX[SOM_PILEX]];
                    CO = CO + 1;
                    break;
                case 13:
                    break;
            }
        }
    }


    public void CREER_FICHIER_CODE(String inputFileName) throws IOException {
        // Get the base name of the input file (i.e., without the ".mp" extension)
        String baseName = inputFileName.substring(0, inputFileName.lastIndexOf("."));

        // Create the output file name by adding the ".COD4" extension
        String outputFileName = baseName + ".COD";

        try{
            File outputFile = new File(outputFileName);
            if(outputFile.createNewFile()){
                FileWriter myWriter = new FileWriter(outputFileName);
                myWriter.write(Arrays.stream(MEM_VAR)
                        .filter(Objects::nonNull)
                        .count()
                + " mot(s) réservé(s) pour les variables globales\n");
                myWriter.write(stringBuilder.toString());
                myWriter.close();
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
