package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.value.IAny;

/**
 * @author Stan
 */
public class CastingAnyBool implements ICastingRule {

    public static final CastingAnyBool INSTANCE = new CastingAnyBool();

    private CastingAnyBool() {
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().invokeInterface(IAny.class, "asBool", boolean.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.BOOL;
    }
}
