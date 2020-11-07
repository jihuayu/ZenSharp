package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionValue extends ParsedExpression {

    private final IPartialExpression value;

    public ParsedExpressionValue(ZenPosition position, IPartialExpression value) {
        super(position);

        if(value == null)
            throw new IllegalArgumentException("value cannot be null");

        this.value = value;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return value;
    }
}
