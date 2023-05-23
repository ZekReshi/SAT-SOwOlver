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

}
