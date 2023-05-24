import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class SOwOlver {

    Var[] vars;
    Clause[] clauses;
    List<Clause> learnedClauses;
    Queue<Lit> assQueue;
    IG ig;
    Clause curClause;

    public SOwOlver(int numClauses, int numVars) {
        clauses = new Clause[numClauses];
        vars = new Var[numVars];
        for (int i = 0; i < numVars; i++) {
            vars[i] = new Var(i+1);
        }
        learnedClauses = new ArrayList<>();
        assQueue = new LinkedList<>();
        ig = new IG();
        if (numClauses > 0) {
            clauses[0] = new Clause(1);
            curClause = clauses[0];
        }
    }

    public void add(int n) {
        add(getLit(n));
    }

    public void add(Lit lit) {
        if (curClause == null) {
            System.out.println("Clauses full, literal ignored");
            return;
        }
        if (lit == null) {
            if (curClause.lits.size() == 1) {
                Lit toAssign = curClause.lits.get(0);
                System.out.println("Unit clause " + curClause.n + ": Queueing " + toAssign);
                assQueue.add(toAssign);
                ig.imply(toAssign, curClause);
            }
            int n = curClause.n;
            if (n < clauses.length) {
                clauses[n] = new Clause(n+1);
                curClause = clauses[n];
            }
            else {
                curClause = null;
            }
        }
        else {
            curClause.lits.add(lit);
            if (curClause.watched[0] == null) {
                curClause.watched[0] = lit;
                if (lit.positive) {
                    lit.var.watchedTrue.add(curClause);
                }
                else {
                    lit.var.watchedFalse.add(curClause);
                }
            }
            else if (curClause.watched[1] == null) {
                curClause.watched[1] = lit;
                if (lit.positive) {
                    lit.var.watchedTrue.add(curClause);
                }
                else {
                    lit.var.watchedFalse.add(curClause);
                }
            }
        }
    }

    public boolean solve() {
        return dpll();
    }

    private boolean dpll() {
        boolean result = bcp();
        if (!result) {
            List<Var> vars = ig.conflict();
            System.out.println("Conflict, unassigning " + vars);
            for (Var var : vars) {
                var.ass = Var.Ass.Unass;
            }
            assQueue.clear();
            return false;
        }
        Lit decision = dlis();
        System.out.println("DLIS returned: "+decision);
        if (decision == null) {
            return true;
        }
        ig.decide(decision);
        assQueue.add(decision);
        if (dpll()) {
            return true;
        }
        ig.decide(decision);
        assQueue.add(decision);
        return dpll();
    }

    private boolean bcp() {
        boolean conflict = false;
        outer:
        while (!assQueue.isEmpty()) {
            Lit lit = assQueue.remove();
            System.out.println("Assigning " + lit);
            if (lit.var.ass != Var.Ass.Unass) {
                if (lit.var.ass != Var.Ass.from(lit.positive)) {
                    return false;
                }
                continue;
            }
            lit.var.ass = Var.Ass.from(lit.positive);

            List<Clause> clauses = lit.positive ? lit.var.watchedFalse : lit.var.watchedTrue;
            List<Clause> toRemove = new ArrayList<>();
            for (Clause clause : clauses) {
                if (clause.watched[0].var != lit.var) {
                    if (clause.watched[1].var == lit.var) {
                        Lit swap = clause.watched[0];
                        clause.watched[0] = clause.watched[1];
                        clause.watched[1] = swap;
                    }
                    else {
                        continue;
                    }
                }
                boolean litFound = false;
                for (Lit lit_it : clause.lits) {
                    if (lit_it.var != lit.var &&  lit_it != clause.watched[1] &&
                            (lit_it.var.ass == Var.Ass.Unass || lit_it.var.ass == Var.Ass.from(lit_it.positive))) {
                        //System.out.println("Relinking in " + clause.n + ": " + clause.watched[0] + " -> " + lit_it);
                        clause.watched[0] = lit_it;

                        toRemove.add(clause);
                        if (lit_it.positive) {
                            lit_it.var.watchedTrue.add(clause);
                        }
                        else {
                            lit_it.var.watchedFalse.add(clause);
                        }
                        litFound = true;
                        break;
                    }
                }
                if (!litFound) {
                    if (clause.watched[1] == null) {
                        return false;
                    }
                    else if (clause.watched[1].ass() == Var.Ass.Unass) {
                        if (ig.imply(clause.watched[1], clause)) {
                            System.out.println("Unit clause " + clause.n + ": Queueing " + clause.watched[1]);
                            assQueue.add(clause.watched[1]);
                        }
                        else {
                            System.out.println("Unit clause " + clause.n + ": Conflict when queueing " + clause.watched[1]);
                            conflict = true;
                            break outer;
                        }
                    }
                }
            }
            for (Clause clause : toRemove) {
                clauses.remove(clause);
            }
        }
        return !conflict;
    }

    private Lit dlis() {
        int varCount = vars.length;
        int[] litImprovements = new int[varCount*2];
        for (Clause clause : clauses) {
            if(!clauseSAT(clause)){
                for (Lit lit :clause.lits) {
                    int index = lit.var.n -1; //zero based var nums
                    if(!lit.positive)
                        index += varCount;
                    litImprovements[index]++;
                }
            }
        }

        for (Clause clause : learnedClauses) {
            if(!clauseSAT(clause)){
                for (Lit lit :clause.lits) {
                    int index = lit.var.n -1; //zero based var nums
                    if(!lit.positive)
                        index += varCount;
                    litImprovements[index]++;
                }
            }
        }

        int maxAt = 0;
        for (int i = 0; i < litImprovements.length; i++) {
            maxAt = litImprovements[i] > litImprovements[maxAt] ? i : maxAt;
        }
        if(litImprovements[maxAt]==0) return null;
        int varNum =(maxAt % varCount)+1;
        Var decision = Arrays.stream(vars).filter(it-> it.n == varNum).findFirst().get();
        return (maxAt>=varCount) ?decision.negativeLit : decision.positiveLit; 
      
    }

    private boolean clauseSAT(Clause clause) {
        for(Lit lit : clause.lits){
            if(lit.ass() == Var.Ass.True){
                return true;
            }
        }
        return false;
    }

    public Lit getLit(int n) {
        if (n == 0) {
            return null;
        }
        boolean positive = n > 0;
        if (!positive) {
            n = -n;
        }
        if (n > vars.length) {
            return null;
        }
        return positive ? vars[n-1].positiveLit : vars[n-1].negativeLit;
    }

}
