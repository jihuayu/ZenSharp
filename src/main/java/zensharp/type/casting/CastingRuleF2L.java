package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleF2L extends BaseCastingRule {

    public CastingRuleF2L(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().f2l();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.FLOAT;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.LONG;
    }
}
