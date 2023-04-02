public class Variable extends T_ENREG_IDENT{
    private int typv;
    private int adrv;

    public Variable(String nom, int typv, int adrv){
        super(nom);
        this.typv = typv;
        this.adrv = adrv;
    }

    public int getAdrv() {
        return adrv;
    }

    public int getTypv() {
        return typv;
    }

    public String toString(){
        return "Variable";
    }
}
