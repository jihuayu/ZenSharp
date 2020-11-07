package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleD2L extends BaseCastingRule {

    public CastingRuleD2L(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().d2l();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.DOUBLE;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.LONG;
    }
}
