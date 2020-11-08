package zensharp.type;

import org.objectweb.asm.Type;


import zensharp.annotations.CompareType;
import zensharp.annotations.OperatorType;
import zensharp.definitions.zenclasses.ParsedZenClass;

import zensharp.expression.partial.IPartialExpression;
import zensharp.parser.Token;
import zensharp.type.casting.ICastingRuleDelegate;
import zensharp.util.Pair;
import zensharp.util.ZenPosition;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionNull;

import java.util.*;

public class ZenTypeZenClass extends ZenType {

    public final ParsedZenClass zenClass;

    public final List<Pair<String, ZenType>> generic;

    public ZenTypeZenClass(ParsedZenClass zenClass) {
        this(zenClass,new LinkedList<>());
    }

    public ZenTypeZenClass(ParsedZenClass zenClass, List<Pair<String, ZenType>> generic) {
        this.generic = generic;
        this.zenClass = zenClass;
    }

    @Override
    public Expression unary(ZenPosition position, IEnvironmentGlobal environment, Expression value, OperatorType operator) {
        throw new UnsupportedOperationException("Cannot use unary operators for ZenClasses");
    }

    @Override
    public Expression binary(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, OperatorType operator) {
        throw new UnsupportedOperationException("Classes have no binary operators");
    }

    @Override
    public Expression trinary(ZenPosition position, IEnvironmentGlobal environment, Expression first, Expression second, Expression third, OperatorType operator) {
        throw new UnsupportedOperationException("Classes don't have ternary operators");
    }

    @Override
    public Expression compare(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, CompareType type) {
        throw new UnsupportedOperationException("Why would you compare friggin classes?");
    }

    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name) {
        return zenClass.getMember(position, environment, value, name, false);
    }

    @Override
    public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        return zenClass.getMember(position, environment, null, name, true);
    }

    @Override
    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
        return zenClass.call(position, environment, arguments);
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
        return zenClass.thisClass;
    }

    @Override
    public Type toASMType() {
        return toJavaClass() != null ? Type.getType(toJavaClass()) : Type.getType(getSignature());
    }

    @Override
    public int getNumberType() {
        return 0;
    }

    @Override
    public String getSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append("L");
        sb.append(zenClass.className);
        for (Pair<String, ZenType> i : generic){
            sb.append("<");
            sb.append(i.getValue().getSignature());
            sb.append(">");
        }
        sb.append(";");

        return sb.toString();
    }

    @Override
    public boolean isPointer() {
        return false;
    }

    @Override
    public String getAnyClassName(IEnvironmentGlobal environment) {
        return zenClass.className;
    }

    @Override
    public String getName() {
        return zenClass.className;
    }

    @Override
    public Expression defaultValue(ZenPosition position) {
        return new ExpressionNull(position);
    }

    @Override
    public ZenType[] predictCallTypes(int numArguments) {
        return zenClass.predictCallTypes(numArguments);
    }

}
