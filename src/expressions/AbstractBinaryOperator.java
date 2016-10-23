package expressions;

import expressions.Expression;

import java.util.HashMap;

public abstract class AbstractBinaryOperator implements Expression {

    public Expression firstExpression;
    public Expression secondExpression;
    private  String op;

    public AbstractBinaryOperator(Expression firstExpression, Expression secondExpression, String op) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.op = op;
    }

    public String toString() {
        return ("(" + firstExpression.toString() + op + secondExpression.toString() + ")");
    }

    public Boolean looksLike(Expression exp, HashMap<String, String> vars) {
        if (exp.getClass() != getClass()) {
            return false;
        }
        AbstractBinaryOperator binExp = (AbstractBinaryOperator) exp;
        return (op.equals(binExp.op) && firstExpression.looksLike(binExp.firstExpression, vars) && secondExpression.looksLike(binExp.secondExpression, vars));
    }
}