package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionOrOr;

/**
 * @author Stanneke
 */
public class ParsedExpressionOrOr extends ParsedExpression {

    private final ParsedExpression left;
    private final ParsedExpression right;

    public ParsedExpressionOrOr(ZenPosition position, ParsedExpression left, ParsedExpression right) {
        super(position);

        this.left = left;
        this.right = right;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        Expression cLeft = left.compile(environment, predictedType).eval(environment);
        Expression cRight = right.compile(environment, predictedType).eval(environment);

        ZenType type;
        if(cRight.getType().canCastImplicit(cLeft.getType(), environment)) {
            type = cLeft.getType();
        } else if(cLeft.getType().canCastImplicit(cRight.getType(), environment)) {
            type = cRight.getType();
        } else {
            environment.error(getPosition(), "These types could not be unified: " + cLeft.getType() + " and " + cRight.getType());
            type = ZenType.ANY;
        }

        return new ExpressionOrOr(getPosition(), cLeft.cast(getPosition(), environment, type), cRight.cast(getPosition(), environment, type));
    }
}
