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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(": ");
        for (Lit lit : lits) {
            sb.append(lit);
            if (watched[0] == lit || watched[1] == lit) {
                sb.append('*');
            }
            sb.append(' ');
        }
        return sb.toString();
    }

}
