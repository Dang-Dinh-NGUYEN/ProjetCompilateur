public class Constante extends T_ENREG_IDENT{
    private int typc; //soit ent(0), soit ch(1)
    private int val; //valent si typc == 0 et valch si typc == 0

    public Constante(String nom, int typc, int val){
        super(nom);
        this.typc = typc;
        this.val = val;
    }

    public int getTypc() {
        return typc;
    }

    public int getVal() {
        return val;
    }

    public String toString(){
        return "Constante";
    }
}
