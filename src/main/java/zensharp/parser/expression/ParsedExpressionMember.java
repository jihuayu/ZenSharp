package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionMember extends ParsedExpression {

    private final ParsedExpression value;
    private final String member;

    public ParsedExpressionMember(ZenPosition position, ParsedExpression value, String member) {
        super(position);

        this.value = value;
        this.member = member;
    }

    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        return value.compile(environment, null).getMember(getPosition(), environment, member);
    }
}
