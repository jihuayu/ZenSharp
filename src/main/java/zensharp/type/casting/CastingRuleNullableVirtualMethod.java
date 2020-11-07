package zensharp.type.casting;

import org.objectweb.asm.Label;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.natives.IJavaMethod;
import zensharp.util.MethodOutput;

/**
 * @author Stan
 */
public class CastingRuleNullableVirtualMethod implements ICastingRule {

    private final ZenType type;
    private final IJavaMethod method;

    public CastingRuleNullableVirtualMethod(ZenType type, IJavaMethod method) {
        this.type = type;
        this.method = method;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        MethodOutput output = method.getOutput();

        Label lblNotNull = new Label();
        Label lblAfter = new Label();

        output.dup();
        output.ifNonNull(lblNotNull);
        output.pop();
        output.aConstNull();
        output.goTo(lblAfter);

        output.label(lblNotNull);
        this.method.invokeVirtual(method.getOutput());

        output.label(lblAfter);
    }

    @Override
    public ZenType getInputType() {
        return type;
    }

    @Override
    public ZenType getResultingType() {
        return method.getReturnType();
    }
}
