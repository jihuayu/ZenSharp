package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.value.IAny;

/**
 * @author Stan
 */
public class CastingAnyDouble implements ICastingRule {

    public static final CastingAnyDouble INSTANCE = new CastingAnyDouble();

    private CastingAnyDouble() {
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().invokeInterface(IAny.class, "asDouble", double.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.DOUBLE;
    }
}
