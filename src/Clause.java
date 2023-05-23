import java.util.ArrayList;
import java.util.List;

public class Clause {

    List<Lit> lits;
    Lit[] watched;
    int n;

    public Clause(int n) {
        lits = new ArrayList<>();
        watched = new Lit[2];
        this.n = n;
    }

}
