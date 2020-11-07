package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ExpressionThis extends Expression {

    private final ZenType type;

    public ExpressionThis(ZenPosition position, ZenType type) {
        super(position);
        this.type = type;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(result)
            environment.getOutput().loadObject(0);
    }

    @Override
    public ZenType getType() {
        return type;
    }
}
