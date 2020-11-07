package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleD2I extends BaseCastingRule {

    public CastingRuleD2I(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().d2i();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.DOUBLE;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.INT;
    }
}
