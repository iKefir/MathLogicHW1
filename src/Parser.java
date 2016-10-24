import expressions.And;
import expressions.Implication;
import expressions.Or;
import expressions.Not;
import expressions.Variable;
import expressions.Expression;


public class Parser {
    private String s;
    private int nom;
    private boolean sEnded;

    Expression parse(String args) {
        s = args;
        nom = 0;
        sEnded = false;
        return parseBinaryExpression(0);
    }

    private String parseToken() {
        while ((nom < s.length()) && ((s.charAt(nom) == ' ') || (s.charAt(nom) == '\t'))) ++nom;

        if (nom == s.length()) {
            sEnded = true;
            return "";
        }
        if (Character.isLetter(s.charAt(nom))) {
            StringBuilder build = new StringBuilder();
            while (nom < s.length() && Character.isLetterOrDigit(s.charAt(nom))) {
                build.append(s.charAt(nom));
                nom++;
            }
            return build.toString();
        }
        pair hpOps = new pair();
        hpOps.arr = new String[]{"->"};
        hpOps.l = new int[]{2};
        String[] ops = {"&", "|", "!", "(", ")"};
        String t;
        int tSdv = 0;
        if (nom + 2 <= s.length()) {
            t = s.substring(nom, nom + 2);
            for (int i = 0; i < hpOps.arr.length; i++) {
                if (t.equals(hpOps.arr[i])) tSdv = hpOps.l[i];
            }
        } else {
            t = s.substring(nom, nom + 1);
        }
        if (tSdv == 0) {
            t = t.substring(0, 1);
            for (String op : ops) {
                if (t.equals(op)) tSdv = 1;
            }
        }
        if (tSdv > 0) {
            t = s.substring(nom, nom + tSdv);
            nom += tSdv;
            return t;
        }

        return "";
    }

    private Expression parseSimpleExpression() {
        String token = parseToken();

        if (token.equals("(")) {
            Expression res = parseBinaryExpression(0);
            parseToken();
            return res;
        }

        Expression arg;
        if (token.equals("!")) {
            String nextArg = parseToken();
            nom -= nextArg.length();
            arg = parseSimpleExpression();
            return new Not(arg);
        }

        return new Variable(token);
    }

    private int expressionPower(String token) {
        int number = -1;
        if (token.equals("&")) {
            number = 3;
        }
        if (token.equals("|")) {
            number = 2;
        }
        if (token.equals("->")) {
            number = 1;
        }
        return number;
    }

    private Expression expressionCreate(String token, Expression l, Expression r) {
        if (token.equals("&")) {
            return new And(l, r);
        }
        if (token.equals("|")) {
            return new Or(l, r);
        }

        return new Implication(l, r);
    }

    private Expression parseBinaryExpression(int power) {
        Expression left = parseSimpleExpression();
        Expression right;
        while (true) {
            String token = parseToken();
            int tPower = expressionPower(token);
            if ((token.equals("->") && tPower < power) || (!token.equals("->") && tPower <= power)) {
//            if (tPower < power) {
                nom -= token.length();
                return left;
            }

            right = parseBinaryExpression(tPower);
            left = expressionCreate(token, left, right);
        }
    }

    private static class pair {
        String[] arr;
        int[] l;
        int nom;
        Expression fun;
    }
}
