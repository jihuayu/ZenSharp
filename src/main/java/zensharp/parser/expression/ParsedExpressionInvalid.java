package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.ExpressionInvalid;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionInvalid extends ParsedExpression {

    public ParsedExpressionInvalid(ZenPosition position) {
        super(position);
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return new ExpressionInvalid(getPosition(), predictedType == null ? ZenType.ANY : predictedType);
    }
}
