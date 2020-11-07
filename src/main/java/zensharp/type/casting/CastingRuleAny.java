package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.value.IAny;

/**
 * @author Stan
 */
public class CastingRuleAny implements ICastingRule {

    private final ZenType toType;

    public CastingRuleAny(ZenType toType) {
        this.toType = toType;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().constant(toType);
        method.getOutput().invokeInterface(IAny.class, "as", Class.class, Object.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return toType;
    }
}
