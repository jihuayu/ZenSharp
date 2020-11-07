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
public class ParsedExpressionUnary extends ParsedExpression {

    private final ParsedExpression value;
    private final OperatorType operator;

    public ParsedExpressionUnary(ZenPosition position, ParsedExpression value, OperatorType operator) {
        super(position);

        this.value = value;
        this.operator = operator;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        // TODO: improve type predictions?
        Expression cValue = value.compile(environment, predictedType).eval(environment);
        return cValue.getType().unary(getPosition(), environment, cValue, operator);
    }
}
