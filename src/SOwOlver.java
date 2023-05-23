import java.util.ArrayList;
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
        clauses[0] = new Clause(1);
        curClause = clauses[0];
    }

    public void add(Lit lit) {
        if (curClause == null) {
            System.out.println("Clauses full, literal ignored");
            return;
        }
        if (lit == null) {
            if (curClause.lits.size() == 1) {
                assQueue.add(curClause.lits.get(0));
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
        Var decision = dlis();
        if (decision == null) {
            return true;
        }
        ig.decide(decision.negativeLit);
        assQueue.add(decision.negativeLit);
        if (dpll()) {
            return true;
        }
        ig.decide(decision.positiveLit);
        assQueue.add(decision.positiveLit);
        return dpll();
    }

    private boolean bcp() {
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
                        clause.watched[0] = lit_it;
                        System.out.println("Relinking in " + clause.n + ": " + lit + " -> " + lit_it);

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
                        System.out.println("Unit clause " + clause.n + ": Queueing " + clause.watched[1]);
                        ig.imply(clause.watched[1], clause);
                        assQueue.add(clause.watched[1]);
                    }
                }
            }
            for (Clause clause : toRemove) {
                clauses.remove(clause);
            }
        }
        return true;
    }

    private Var dlis() {
        for (Var var : vars) {
            if (var.ass == Var.Ass.Unass) {
                return var;
            }
        }
        return null;
    }

}
