package zensharp.type.casting;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

/**
 * @author Stan
 */
public interface ICastingRule {

    void compile(IEnvironmentMethod method);

    ZenType getInputType();

    ZenType getResultingType();
}
