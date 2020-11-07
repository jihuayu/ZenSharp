package zensharp.type.expand;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionCallStatic;
import zensharp.type.ZenType;
import zensharp.type.casting.CastingRuleDelegateStaticMethod;
import zensharp.type.casting.CastingRuleStaticMethod;
import zensharp.type.casting.ICastingRuleDelegate;
import zensharp.type.natives.IJavaMethod;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ZenExpandCaster {

    private final IJavaMethod method;

    public ZenExpandCaster(IJavaMethod method) {
        this.method = method;
    }

    public ZenType getTarget() {
        return method.getReturnType();
    }

    public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules) {
        ZenType type = method.getReturnType();
        rules.registerCastingRule(type, new CastingRuleStaticMethod(method));

        type.constructCastingRules(environment, new CastingRuleDelegateStaticMethod(rules, method), false);
    }

    public Expression cast(ZenPosition position, IEnvironmentGlobal environment, Expression expression) {
        return new ExpressionCallStatic(position, environment, method, expression);
    }

    public void compile(MethodOutput output) {
        method.invokeStatic(output);
    }
}
