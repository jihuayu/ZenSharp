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
public class ParsedExpressionIndex extends ParsedExpression {

    private final ParsedExpression value;
    private final ParsedExpression index;

    public ParsedExpressionIndex(ZenPosition position, ParsedExpression value, ParsedExpression index) {
        super(position);

        this.value = value;
        this.index = index;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        // TODO: improve type prediction for this
        Expression cValue = value.compile(environment, null).eval(environment);
        Expression cIndex = index.compile(environment, null).eval(environment);
        return cValue.getType().binary(getPosition(), environment, cValue, cIndex, OperatorType.INDEXGET);
    }
}
