package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionCast extends ParsedExpression {

    private final ParsedExpression value;
    private final ZenType type;

    public ParsedExpressionCast(ZenPosition position, ParsedExpression value, ZenType type) {
        super(position);

        this.value = value;
        this.type = type;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return value.compile(environment, type).eval(environment).cast(getPosition(), environment, type);
    }
}
