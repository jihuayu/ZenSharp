package zensharp.type.casting;

import org.objectweb.asm.Label;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.MethodOutput;

/**
 * @author Stan
 */
public class CastingNotNull implements ICastingRule {

    public ZenType fromType;

    public CastingNotNull(ZenType fromType) {
        this.fromType = fromType;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        MethodOutput output = method.getOutput();
        Label labelElse = new Label();
        Label labelAfter = new Label();

        output.ifNull(labelElse);
        output.iConst1();
        output.goTo(labelAfter);
        output.label(labelElse);
        output.iConst0();
        output.label(labelAfter);
    }

    @Override
    public ZenType getInputType() {
        return fromType;
    }

    @Override
    public ZenType getResultingType() {
        return ZenType.BOOL;
    }
}
