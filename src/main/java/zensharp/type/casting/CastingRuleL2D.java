package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleL2D extends BaseCastingRule {

    public CastingRuleL2D(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().l2d();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.LONG;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.DOUBLE;
    }
}
