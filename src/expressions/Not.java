package expressions;

import java.util.HashMap;

public class Not implements Expression {
    private Expression expression;
    public Not(Expression expression) {this.expression = expression;}

    public String toString() {
        return "!" + expression.toString();
    }

    public Boolean looksLike(Expression exp, HashMap<String, String> vars) {
        return exp.getClass() == getClass() && (expression.looksLike(((Not) exp).expression, vars));
    }
}
