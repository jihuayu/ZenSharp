package zensharp.type.casting;

import org.objectweb.asm.Type;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.value.IAny;

/**
 * @author Stan
 */
public class CastingRuleAnyAs implements ICastingRule {

    private final ZenType type;

    public CastingRuleAnyAs(ZenType type) {
        this.type = type;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().constant(Type.getType(type.toJavaClass()));
        method.getOutput().invokeInterface(IAny.class, "as", Class.class, Object.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return type;
    }
}
