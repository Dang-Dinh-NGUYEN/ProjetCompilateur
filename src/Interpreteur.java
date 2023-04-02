import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class Interpreteur {
    public static final int TAILLE_MAX_MEM = 10000;
    //MOT_MEM[] MEM = new MOT_MEM[TAILLE_MAX_MEM]; //la mémoire centrale

    Variable[] MEM_VAR = new Variable[TAILLE_MAX_MEM]; //la zone mémoire réservée aux variables globales
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
        /*
        for(MOT_MEM m : MOT_MEM.values())
            System.out.println(m + " " + m.ordinal());
        System.out.println();
        System.out.println("MEM_VAR de taille: " + Arrays.stream(MEM_VAR)
                .filter(Objects::nonNull)
                .count());
         */
        System.out.println(Arrays.toString(MEM_VAR));
        System.out.println("PILOP: ");
        System.out.println(Arrays.toString(PILOP));
        System.out.println("P_CODE: ");
        System.out.println(Arrays.toString(P_CODE));
        System.out.println();
        System.out.println(stringBuilder.toString());

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
