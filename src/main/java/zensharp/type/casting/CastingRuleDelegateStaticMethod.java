package zensharp.type.casting;

import zensharp.type.ZenType;
import zensharp.type.natives.IJavaMethod;

/**
 * @author Stan
 */
public class CastingRuleDelegateStaticMethod implements ICastingRuleDelegate {

    private final ICastingRuleDelegate target;
    private final IJavaMethod method;

    public CastingRuleDelegateStaticMethod(ICastingRuleDelegate target, IJavaMethod method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void registerCastingRule(ZenType type, ICastingRule rule) {
        target.registerCastingRule(type, new CastingRuleStaticMethod(method, rule));
    }
}
