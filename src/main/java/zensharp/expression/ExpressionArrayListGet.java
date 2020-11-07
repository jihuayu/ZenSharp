package zensharp.expression;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeArrayList;
import zensharp.util.ZenPosition;
import zensharp.util.ZenTypeUtil;

import java.util.List;

public class ExpressionArrayListGet extends Expression {

    private final Expression array;
    private final Expression index;
    private final ZenType type;
    private final ZenPosition position;

    public ExpressionArrayListGet(ZenPosition position, Expression array, Expression index) {
        super(position);
        this.array = array;
        this.index = index;
        this.type = ((ZenTypeArrayList) array.getType()).getBaseType();
        this.position = position;
    }

    @Override
    public ZenType getType() {
        return type;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if (result) {
            array.compile(result, environment);
            index.compile(result, environment);
            environment.getOutput().invokeInterface(List.class, "get", Object.class, int.class);


            environment.getOutput().checkCast(ZenTypeUtil.checkPrimitive(type).toASMType().getInternalName());
            if (ZenTypeUtil.isPrimitive(type)) {
                ZenTypeUtil.checkPrimitive(type).getCastingRule(type, environment).compile(environment);
            }
        }

    }

    @Override
    public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
        return new ExpressionArrayListSet(position, array, index, other);
    }

}
