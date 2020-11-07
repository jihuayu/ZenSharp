package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ExpressionNothing extends Expression {
    
    
    public ExpressionNothing(ZenPosition position) {
        super(position);
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
    
    }
    
    @Override
    public ZenType getType() {
        return ZenType.NULL;
    }
}
