import expressions.Expression;
import expressions.Implication;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class MathLogicHW1 {
    private static String s = "good5.in";
    private static Parser parser = new Parser();
    private static PrintWriter out;
    private static HashMap<String, String> helper = new HashMap<>();

    private static ArrayList<Expression> axioms = new ArrayList<>();
    private static HashMap<String, Integer> assumptions = new HashMap<>();
    private static HashMap<String, Integer> prooved = new HashMap<>();
    private static HashMap<String, ArrayList<MyPair>> MPHelper = new HashMap<>();

    private static void putAxioms() {
        axioms.add(parser.parse("A->B->A"));
        axioms.add(parser.parse("(A->B)->(A->B->C)->(A->C)"));
        axioms.add(parser.parse("A->B->A&B)"));
        axioms.add(parser.parse("A&B->A"));
        axioms.add(parser.parse("A&B->B"));
        axioms.add(parser.parse("A->A|B"));
        axioms.add(parser.parse("B->A|B"));
        axioms.add(parser.parse("(A->C)->(B->C)->(A|B->C)"));
        axioms.add(parser.parse("(A->B)->(A->!B)->!A"));
        axioms.add(parser.parse("!!A->A"));
    }

    private static void getAssumptions(String header) {
        out.println(header);
        String[] twoParts = header.split("\\|-");
        if (twoParts.length > 0) {
            String[] data = twoParts[0].split(",");
            for (int i = 0; i < data.length; i++) {
                assumptions.put(parser.parse(data[i]).toString(), i);
            }
        }
    }

    private static boolean isAssumption(String exp) {
        if (assumptions.containsKey(exp)) {
            out.println(" (Предп. " + (assumptions.get(exp) + 1) + ")");
            return true;
        }
        return false;
    }

    private static boolean isAxiom(Expression exp) {
        for (int i = 0; i < axioms.size(); i++) {
            helper.clear();
            if (axioms.get(i).looksLike(exp, helper)) {
                out.println(" (Сх. акс. " + (i + 1) + ")");
                return true;
            }
        }
        return false;
    }

    private static boolean isMP(Expression exp) {
        String expString = exp.toString();
        if (MPHelper.containsKey(expString)) {
            ArrayList<MyPair> data = MPHelper.get(expString);
            for (MyPair d : data) {
                if (prooved.containsKey(d.first)) {
                    Integer first = prooved.get(d.first), second = d.second;
                    out.println(" (M.P. " + first + ", " + second + ")");
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(s));
        out = new PrintWriter(new File("output.txt"));
        String buf;
        Expression exp;
        putAxioms();
        getAssumptions(in.readLine());
        int sch = 0;
        while (true) {
            sch++;
            buf = in.readLine();
            if (buf == null) break;
            exp = parser.parse(buf);
            out.print("(" + sch + ") " + buf);
            if (isAxiom(exp) || isAssumption(exp.toString()) || isMP(exp)) {
                prooved.put(exp.toString(), sch);
                if (exp.getClass() == Implication.class) {
                    Implication impExp = (Implication) exp;
                    String impExpString = impExp.secondExpression.toString();
                    if (!prooved.containsKey(impExpString)) {
                        ArrayList<MyPair> data;
                        if (MPHelper.containsKey(impExpString)) {
                            data = MPHelper.get(impExpString);
                        } else {
                            data = new ArrayList<>();
                        }
                        data.add(new MyPair(impExp.firstExpression.toString(), sch));
                        MPHelper.put(impExpString, data);
                    }
                }
            } else {
                out.println(" (Не доказано)");
            }
        }
        out.close();
    }
}
