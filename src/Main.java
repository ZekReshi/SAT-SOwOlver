public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SOwOlver sOwOlver = new SOwOlver(7, 3);
        sOwOlver.add(sOwOlver.vars[0].positiveLit);
        sOwOlver.add(sOwOlver.vars[1].positiveLit);
        sOwOlver.add(sOwOlver.vars[2].positiveLit);
        sOwOlver.add(null);
        sOwOlver.add(sOwOlver.vars[0].positiveLit);
        sOwOlver.add(sOwOlver.vars[1].negativeLit);
        sOwOlver.add(null);
        sOwOlver.add(sOwOlver.vars[0].positiveLit);
        sOwOlver.add(sOwOlver.vars[2].negativeLit);
        sOwOlver.add(null);
        sOwOlver.add(sOwOlver.vars[1].positiveLit);
        sOwOlver.add(sOwOlver.vars[2].negativeLit);
        sOwOlver.add(null);
        sOwOlver.add(sOwOlver.vars[0].negativeLit);
        sOwOlver.add(sOwOlver.vars[1].negativeLit);
        sOwOlver.add(sOwOlver.vars[2].positiveLit);
        sOwOlver.add(null);
        sOwOlver.add(sOwOlver.vars[0].negativeLit);
        sOwOlver.add(sOwOlver.vars[1].negativeLit);
        sOwOlver.add(sOwOlver.vars[2].negativeLit);
        sOwOlver.add(null);
        sOwOlver.add(sOwOlver.vars[0].negativeLit);
        sOwOlver.add(sOwOlver.vars[1].positiveLit);
        sOwOlver.add(sOwOlver.vars[2].positiveLit);
        sOwOlver.add(null);
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