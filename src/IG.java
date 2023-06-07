import java.util.*;

public class IG {

    private class Node {
        Lit lit;
        int level;
        boolean implication;
        Node[] reason;

        public Node(Lit lit, boolean implication) {
            this.lit = lit;
            this.level = decisions.size() + 1;
            this.implication = implication;
            this.reason = null;
        }

        public Node(Lit lit, boolean implication, Clause clause) {
            this.lit = lit;
            this.level = decisions.size() + 1;
            this.implication = implication;
            reason = new Node[clause.lits.size() - 1];
            int i = 0;
            for (Lit lit_it : clause.lits) {
                if (lit_it != lit) {
                    for (Node node : nodes) {
                        if (node.lit == lit_it.neg()) {
                            reason[i] = node;
                            i++;
                        }
                    }
                }
            }
        }
    }

    Stack<Node> nodes;
    Stack<Node> decisions;

    public IG() {
        this.nodes = new Stack<>();
        this.decisions = new Stack<>();
    }

    public void decide(Lit lit) {
        Node node = new Node(lit,  false);
        decisions.push(node);
        nodes.push(node);
    }

    public boolean imply(Lit lit, Clause clause) {
        for (Node n : nodes) {
            if (n.lit.var == lit.var) {
                return n.lit == lit;
            }
        }
        Node node = clause == null ? new Node(lit, true) : new Node(lit, true, clause);
        nodes.push(node);
        return true;
    }

    public List<Var> conflict() {
        List<Var> vars = new ArrayList<>();
        Node node;
        do {
            node = nodes.pop();
            vars.add(node.lit.var);
        } while (node.implication && !nodes.isEmpty());
        if (!decisions.empty()) {
            decisions.pop();
        }
        return vars;
    }

    public Clause getDecisionClause(int n) {
        Clause clause = new Clause(n);
        HashSet<Node> reason = new HashSet<>(Arrays.asList(nodes.peek().reason));
        HashSet<Node> newReason;
        boolean done = false;
        while (!done) {
            newReason = new HashSet<>();
            done = true;
            for (Node node : reason) {
                if (node.implication) {
                    newReason.addAll(Arrays.asList(node.reason));
                    done = false;
                }
                else {
                    newReason.add(node);
                }
            }
            reason = newReason;
        }
        for (Node node : reason) {
            if (clause.lits.size() <= 1) {
                clause.watched[clause.lits.size()] = node.lit.neg();
                if (node.lit.positive) {
                    node.lit.var.watchedFalse.add(clause);
                }
                else {
                    node.lit.var.watchedTrue.add(clause);
                }
            }
            clause.lits.add(node.lit.neg());
        }
        return clause;
    }

}
