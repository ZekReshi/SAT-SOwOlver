import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

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
            Node[] reason = new Node[clause.lits.size() - 1];
            int i = 0;
            for (Lit lit_it : clause.lits) {
                if (lit_it != lit) {
                    for (Node node : nodes) {
                        if (node.lit == lit_it) {
                            reason[i] = node;
                            i++;
                        }
                    }
                }
            }
            this.reason = reason;
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
        decisions.pop();
        return vars;
    }

    public Clause getDecisionClause(int n) {
        Clause clause = new Clause(n);
        for (Node node : decisions) {
            if (clause.lits.size() <= 1) {
                clause.watched[clause.lits.size()] = node.lit.neg();
                if (node.lit.positive) {
                    node.lit.var.watchedTrue.add(clause);
                }
                else {
                    node.lit.var.watchedFalse.add(clause);
                }
            }
            clause.lits.add(node.lit.neg());
        }
        return clause;
    }

}
