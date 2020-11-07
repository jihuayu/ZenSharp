package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.ExpressionBool;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionBool extends ParsedExpression {

    private final boolean value;

    public ParsedExpressionBool(ZenPosition position, boolean value) {
        super(position);

        this.value = value;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return new ExpressionBool(getPosition(), value);
    }
}
