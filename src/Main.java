public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SOwOlver sOwOlver = new SOwOlver(6, 3);
        sOwOlver.add(1);
        sOwOlver.add(2);
        sOwOlver.add(3);
        sOwOlver.add(0);
        sOwOlver.add(1);
        sOwOlver.add(-2);
        sOwOlver.add(0);
        sOwOlver.add(1);
        sOwOlver.add(-3);
        sOwOlver.add(0);
        sOwOlver.add(2);
        sOwOlver.add(-3);
        sOwOlver.add(0);
        sOwOlver.add(-1);
        sOwOlver.add(-2);
        sOwOlver.add(3);
        sOwOlver.add(0);
        sOwOlver.add(-1);
        sOwOlver.add(-2);
        sOwOlver.add(-3);
        sOwOlver.add(0);
        for (Var var : sOwOlver.vars) {
            System.out.println(var);
        }
        for (Clause clause : sOwOlver.clauses) {
            System.out.println(clause);
        }
        System.out.println(sOwOlver.solve());
        for (Var var : sOwOlver.vars) {
            System.out.println(var);
        }
        for (Clause clause : sOwOlver.clauses) {
            System.out.println(clause);
        }
    }
}