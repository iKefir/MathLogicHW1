package expressions;

import java.util.HashMap;

public interface Expression {
    String toString();

    Boolean looksLike(Expression exp, HashMap<String, String> vars);
}
