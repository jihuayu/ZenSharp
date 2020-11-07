package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

import static zensharp.util.ZenTypeUtil.internal;

public class ExpressionInstanceOf extends Expression {
    
    private final Expression expression;
    private final ZenType type;
    
    public ExpressionInstanceOf(ZenPosition position, Expression expression, ZenType type) {
        super(position);
        this.expression = expression;
        this.type = type;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(!result)
            return;
        expression.compile(result, environment);
        environment.getOutput().instanceOf(internal(type.toJavaClass()));
    }
    
    @Override
    public ZenType getType() {
        return ZenType.BOOL;
    }
}
