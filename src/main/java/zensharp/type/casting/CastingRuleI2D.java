package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleI2D extends BaseCastingRule {

    public CastingRuleI2D(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().i2d();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.INT;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.DOUBLE;
    }
}
