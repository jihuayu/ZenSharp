package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeAssociative;

/**
 * @author Stan
 */
public class CastingRuleMap implements ICastingRule {

    private final ICastingRule keyRule;
    private final ICastingRule valueRule;
    private final ZenTypeAssociative fromType;
    private final ZenTypeAssociative toType;

    public CastingRuleMap(ICastingRule keyRule, ICastingRule valueRule, ZenTypeAssociative fromType, ZenTypeAssociative toType) {
        this.keyRule = keyRule;
        this.valueRule = valueRule;
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        throw new UnsupportedOperationException("Not supported yet.");
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
