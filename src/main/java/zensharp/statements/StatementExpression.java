package zensharp.statements;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.util.ZenPosition;
import zensharp.parser.expression.ParsedExpression;
import zensharp.parser.expression.ParsedExpressionIndexSet;

public class StatementExpression extends Statement {

    private final ParsedExpression expression;

    public StatementExpression(ZenPosition position, ParsedExpression expression) {
        super(position);

        this.expression = expression;
    }

    @Override
    public void compile(IEnvironmentMethod environment) {
        compile(environment, false);
    }
    
    @Override
    public void compile(IEnvironmentMethod environment, boolean forced) {
        environment.getOutput().position(getPosition());
        //boolean shouldCompile = expression.getClass().getName().equals("stanhebben.zenscript.parser.expression.ParsedExpressionIndexSet");
        boolean shouldCompile = ParsedExpressionIndexSet.class.isInstance(expression);
        expression.compile(environment, null).eval(environment).compile(shouldCompile || forced, environment);
    }
}
