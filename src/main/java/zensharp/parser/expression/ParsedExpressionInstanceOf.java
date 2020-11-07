package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.ExpressionInstanceOf;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ParsedExpressionInstanceOf extends ParsedExpression {
    
    private final ParsedExpression base;
    private final ZenType type;
    
    public ParsedExpressionInstanceOf(ZenPosition position, ParsedExpression base, ZenType type) {
        super(position);
        this.base = base;
        this.type = type;
    }
    
    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        IPartialExpression ex = base.compile(environment, null);
        return new ExpressionInstanceOf(getPosition(), ex.eval(environment), type);
    }
    
}
