public class Lit {

    Var var;
    boolean positive;

    public Lit(Var var, boolean positive) {
        this.var = var;
        this.positive = positive;
    }

    public Var.Ass ass() {
        return positive ? var.ass : Var.Ass.neg(var.ass);
    }

}
