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
import zensharp.util.Pair;
import zensharp.util.ZenPosition;

import java.util.List;
import java.util.Map;

public class ZenTypeGeneric extends ZenType {

    private final ZenType type;
    private final String name;

    public ZenTypeGeneric(String name,ZenType type){
        this.type = type;
        this.name = name;
    }

    @Override
    public Expression unary(ZenPosition position, IEnvironmentGlobal environment, Expression value, OperatorType operator) {
        return type.unary(position, environment, value, operator);
    }

    @Override
    public Expression binary(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, OperatorType operator) {
        return type.binary(position, environment, left, right, operator);
    }

    @Override
    public Expression trinary(ZenPosition position, IEnvironmentGlobal environment, Expression first, Expression second, Expression third, OperatorType operator) {
        return type.trinary(position, environment, first, second, third, operator);
    }

    @Override
    public Expression compare(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, CompareType type) {
        return this.type.compare(position, environment, left, right, type);
    }

    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name) {
            return type.getMember(position, environment, value, name);
    }

    @Override
    public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        return type.getStaticMember(position, environment, name);
    }

    @Override
    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
        return type.call(position, environment, receiver, arguments);
    }

    @Override
    public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {
        type.constructCastingRules(environment, rules, followCasters);
    }

    @Override
    public IZenIterator makeIterator(int numValues, IEnvironmentMethod methodOutput) {
        return null;
    }

    @Override
    public Class toJavaClass() {
        return type.toJavaClass();
    }

    @Override
    public Type toASMType() {
        return type.toASMType();
    }

    @Override
    public int getNumberType() {
        return type.getNumberType();
    }

    @Override
    public String getSignature() {
        return "T"+name+";";
    }

    @Override
    public boolean isPointer() {
        return type.isPointer();
    }

    @Override
    public String getAnyClassName(IEnvironmentGlobal environment) {
        return type.getAnyClassName(environment);
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public Expression defaultValue(ZenPosition position) {
        return new ExpressionNull(position);
    }
}
