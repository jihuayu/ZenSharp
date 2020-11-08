package zensharp.type;

import org.objectweb.asm.Type;
import zensharp.annotations.CompareType;
import zensharp.annotations.OperatorType;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionNull;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.casting.ICastingRuleDelegate;
import zensharp.util.ZenPosition;
import zensharp.util.ZenTypeUtil;

public class ZenTypeObject extends ZenType {

    public static final ZenTypeObject INSTANCE = new ZenTypeObject();


    @Override
    public Expression unary(ZenPosition position, IEnvironmentGlobal environment, Expression value, OperatorType operator) {
        return null;
    }

    @Override
    public Expression binary(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, OperatorType operator) {
        return null;
    }

    @Override
    public Expression trinary(ZenPosition position, IEnvironmentGlobal environment, Expression first, Expression second, Expression third, OperatorType operator) {
        return null;
    }

    @Override
    public Expression compare(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, CompareType type) {
        return null;
    }

    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name) {
        return null;
    }

    @Override
    public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        return null;
    }

    @Override
    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
        return null;
    }

    @Override
    public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {

    }

    @Override
    public IZenIterator makeIterator(int numValues, IEnvironmentMethod methodOutput) {
        return null;
    }

    @Override
    public Class toJavaClass() {
        return Object.class;
    }

    @Override
    public Type toASMType() {
        return Type.getType(Object.class);
    }

    @Override
    public int getNumberType() {
        return 0;
    }

    @Override
    public String getSignature() {
        return ZenTypeUtil.signature(Object.class);
    }

    @Override
    public boolean isPointer() {
        return false;
    }

    @Override
    public String getAnyClassName(IEnvironmentGlobal environment) {
        return Object.class.getName();
    }

    @Override
    public String getName() {
        return "object";
    }

    @Override
    public Expression defaultValue(ZenPosition position) {
        return new ExpressionNull(position);
    }
}
