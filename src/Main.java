import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid argument count!");
            return;
        }
        SOwOlver sOwOlver;
        try {
            sOwOlver = getSolver(args[0]);
            boolean sat = sOwOlver.solve();
            if (sat) {
                System.out.println("SAT, assignment follows");
                for (Var var : sOwOlver.vars) {
                    System.out.print(var.ass == Var.Ass.True ? var.n : var.ass == Var.Ass.False ? -var.n : "#" + var.n);
                    System.out.print(", ");
                }
                System.exit(10);
            }
            else {
                System.out.println("UNSAT");
                System.exit(20);
            }
        } catch (IOException e) {
            System.out.println("File could not be read");
        }        
    }

    public static SOwOlver getSolver(String filePath) throws IOException{
        
        List<String> fileLines = Files.readAllLines(Path.of(filePath));
        int clauseCount = getClauseCount(fileLines.get(0));
        int varCount = getVarCount(fileLines.get(0));
        SOwOlver solver = new SOwOlver(clauseCount, varCount);
        for (String line : fileLines.subList(1, fileLines.size())) {
            String[] lits = line.split(" ");
            for (String lit : lits) {
                solver.add(Integer.parseInt(lit));
            }
        }

        return solver;
    }

    private static int getVarCount(String line) {
        return Integer.parseInt(line.split(" ")[2]);
    }

    private static int getClauseCount(String line) {
        return Integer.parseInt(line.split(" ")[3]);
    }
}
