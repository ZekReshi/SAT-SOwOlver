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

    public void imply(Lit lit, Clause clause) {
        List<Node> reason = new ArrayList<>();
        Node node = new Node(lit, true, clause);
        decisions.push(node);
        nodes.push(node);
    }

    public List<Var> conflict() {
        List<Var> vars = new ArrayList<>();
        Node node;
        do {
            node = nodes.pop();
            vars.add(node.lit.var);
        } while (node.implication);
        decisions.pop();
        return vars;
    }

}
