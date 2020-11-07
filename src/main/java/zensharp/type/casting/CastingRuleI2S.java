package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public class CastingRuleI2S extends BaseCastingRule {

    public CastingRuleI2S(ICastingRule baseRule) {
        super(baseRule);
    }

    @Override
    public void compileInner(IEnvironmentMethod method) {
        method.getOutput().i2s();
    }

    @Override
    public ZenType getInnerInputType() {
        return ZenType.INT;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.SHORT;
    }
}
