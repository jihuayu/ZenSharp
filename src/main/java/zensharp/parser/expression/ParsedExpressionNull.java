package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.ExpressionNull;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionNull extends ParsedExpression {

    public ParsedExpressionNull(ZenPosition position) {
        super(position);
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return new ExpressionNull(getPosition());
    }
}
