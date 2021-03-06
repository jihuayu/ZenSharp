package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.value.IAny;

/**
 * @author Stan
 */
public class CastingAnyByte implements ICastingRule {

    public static final CastingAnyByte INSTANCE = new CastingAnyByte();

    private CastingAnyByte() {
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().invokeInterface(IAny.class, "asByte", byte.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.BYTE;
    }
}
