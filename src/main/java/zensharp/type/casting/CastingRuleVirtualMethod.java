package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.natives.IJavaMethod;

/**
 * @author Stan
 */
public class CastingRuleVirtualMethod implements ICastingRule {

    private final IJavaMethod method;

    public CastingRuleVirtualMethod(IJavaMethod method) {
        this.method = method;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        this.method.invokeVirtual(method.getOutput());
    }

    @Override
    public ZenType getInputType() {
        return method.getParameterTypes()[0];
    }

    @Override
    public ZenType getResultingType() {
        return method.getReturnType();
    }
}
