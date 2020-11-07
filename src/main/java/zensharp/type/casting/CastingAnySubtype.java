package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenTypeUtil;

public class CastingAnySubtype implements ICastingRule {

    private final ZenType fromType;
    private final ZenType toType;

    public CastingAnySubtype(ZenType fromType, ZenType toType) {
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().checkCast(ZenTypeUtil.internal(toType.toJavaClass()));
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
