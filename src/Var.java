import java.util.ArrayList;
import java.util.List;

public class Var {

    public enum Ass {
        False, True, Unass;

        public static Ass from(boolean bool) {
            return bool ? True : False;
        }

        public static Ass neg(Ass ass) {
            return switch (ass) {
                case False -> True;
                case True -> False;
                default -> Unass;
            };
        }
    }

    Ass ass;
    List<Clause> watchedTrue, watchedFalse;
    int n;
    Lit positiveLit;
    Lit negativeLit;

    public Var(int n) {
        ass = Ass.Unass;
        watchedTrue = new ArrayList<>();
        watchedFalse = new ArrayList<>();
        this.n = n;
        positiveLit = new Lit(this, true);
        negativeLit = new Lit(this, false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(": ");
        sb.append(ass);
        sb.append(" + ");
        for (Clause clause : watchedTrue) {
            sb.append(clause.n);
            sb.append(' ');
        }
        sb.append("- ");
        for (Clause clause : watchedFalse) {
            sb.append(clause.n);
            sb.append(' ');
        }
        return sb.toString();
    }

}
