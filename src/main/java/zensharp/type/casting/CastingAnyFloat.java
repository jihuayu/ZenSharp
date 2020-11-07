package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.value.IAny;

/**
 * @author Stan
 */
public class CastingAnyFloat implements ICastingRule {

    public static final CastingAnyFloat INSTANCE = new CastingAnyFloat();

    private CastingAnyFloat() {
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().invokeInterface(IAny.class, "asFloat", float.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.FLOAT;
    }
}
