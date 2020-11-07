package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleF2D extends BaseCastingRule {

    public CastingRuleF2D(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().f2d();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.FLOAT;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.DOUBLE;
    }
}
