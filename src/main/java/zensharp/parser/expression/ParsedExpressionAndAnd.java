package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.ExpressionAndAnd;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionAndAnd extends ParsedExpression {

    private final ParsedExpression left;
    private final ParsedExpression right;

    public ParsedExpressionAndAnd(ZenPosition position, ParsedExpression left, ParsedExpression right) {
        super(position);

        this.left = left;
        this.right = right;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return new ExpressionAndAnd(getPosition(), left.compile(environment, predictedType).eval(environment), right.compile(environment, predictedType).eval(environment));
    }
}
