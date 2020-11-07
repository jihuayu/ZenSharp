package zensharp.parser.expression;

import zensharp.annotations.CompareType;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionCompare extends ParsedExpression {

    private final ParsedExpression left;
    private final ParsedExpression right;
    private final CompareType type;

    public ParsedExpressionCompare(ZenPosition position, ParsedExpression left, ParsedExpression right, CompareType type) {
        super(position);

        this.left = left;
        this.right = right;
        this.type = type;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        Expression cLeft = left.compile(environment, null).eval(environment);
        Expression cRight = right.compile(environment, null).eval(environment);
        return cLeft.getType().compare(getPosition(), environment, cLeft, cRight, type);
    }
}
