package zensharp.parser.expression;

import zensharp.annotations.OperatorType;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionBinary extends ParsedExpression {

    private final ParsedExpression left;
    private final ParsedExpression right;
    private final OperatorType operator;

    public ParsedExpressionBinary(ZenPosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
        super(position);

        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        // TODO: make better predictions
        Expression cLeft = left.compile(environment, predictedType).eval(environment);
        Expression cRight = right.compile(environment, predictedType).eval(environment);
        return cLeft.getType().binary(getPosition(), environment, cLeft, cRight, operator);
    }
}
