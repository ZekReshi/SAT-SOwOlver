import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SOwOlver sOwOlver;
        try {
            sOwOlver = getSolver("formula.cnf");
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
