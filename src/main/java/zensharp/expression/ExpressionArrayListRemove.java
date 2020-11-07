package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;
import zensharp.util.ZenTypeUtil;

import java.util.Collection;

public class ExpressionArrayListRemove extends Expression {

    private final Expression list;
    private final Expression val;

    public ExpressionArrayListRemove(ZenPosition position, Expression list, Expression val) {
        super(position);
        this.list = list;
        this.val = val;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        list.compile(true, environment);
        val.cast(getPosition(), environment, ZenTypeUtil.checkPrimitive(val.getType())).compile(true, environment);


        environment.getOutput().invokeInterface(Collection.class, "remove", boolean.class, Object.class);
        if (!result)
            environment.getOutput().pop();
    }

    @Override
    public ZenType getType() {
        return ZenType.BOOL;
    }
}
