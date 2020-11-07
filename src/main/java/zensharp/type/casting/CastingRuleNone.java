package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleNone implements ICastingRule {

    private final ZenType fromType;
    private final ZenType toType;

    public CastingRuleNone(ZenType fromType, ZenType toType) {
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        // nothing to do
    }

    @Override
    public ZenType getInputType() {
        return fromType;
    }

    @Override
    public ZenType getResultingType() {
        return toType;
    }
}
