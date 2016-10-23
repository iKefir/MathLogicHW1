package expressions;

import java.util.HashMap;

public class Variable implements Expression {
    private String value;

    public Variable(String value) {this.value = value;}

    public String toString() {
        return value;
    }

    public Boolean looksLike(Expression exp, HashMap<String, String> vars) {
        if (vars.containsKey(value)) {
            if (exp.toString().equals(vars.get(value))) {
                return true;
            }
        } else {
            vars.put(value, exp.toString());
            return true;
        }
        return false;
    }
}
