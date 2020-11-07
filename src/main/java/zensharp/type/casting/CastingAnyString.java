package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingAnyString implements ICastingRule {

    public static final CastingAnyString INSTANCE = new CastingAnyString();

    private CastingAnyString() {
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        //IAny is not and probably will not be implemented, so let's at least use a method that works!
        method.getOutput().invokeVirtual(Object.class, "toString", String.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.STRING;
    }
}
