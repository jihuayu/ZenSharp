package zensharp.expression;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeArrayBasic;
import zensharp.util.ArrayUtil;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

public class ExpressionArrayAdd extends Expression {

    private final Expression array, value;
    private final ZenTypeArrayBasic type;

    public ExpressionArrayAdd(ZenPosition position, IEnvironmentGlobal environment, Expression array, Expression val) {
        super(position);
        this.array = array;
        this.value = val;
        this.type = (ZenTypeArrayBasic) array.getType();
    }

    @Override
    public ZenType getType() {
        return type;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        MethodOutput output = environment.getOutput();

        array.compile(true, environment);
        value.cast(getPosition(), environment, type.getBaseType()).compile(true, environment);

        if (type.getBaseType().toJavaClass().isPrimitive()) {
            Class<?> arrayType = getType().toJavaClass();
            output.invokeStatic(ArrayUtil.class, "add", arrayType, arrayType, type.getBaseType().toJavaClass());
        } else {
            output.invokeStatic(ArrayUtil.class, "add", Object[].class, Object[].class, Object.class);
            output.checkCast(type.getSignature());
        }
    }

}
