package zensharp.type.expand;



import zensharp.expression.partial.IPartialExpression;
import zensharp.symbols.IZenSymbol;
import zensharp.type.ZenType;

import zensharp.util.ZenPosition;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionCallStatic;
import zensharp.expression.ExpressionInvalid;
import zensharp.type.natives.IJavaMethod;
import zensharp.type.natives.JavaMethod;
import zensharp.util.StringUtil;

import java.util.*;

/**
 * @author Stan
 */
public class ZenExpandMember {

    private final String type;
    private final String name;
    private final List<IJavaMethod> methods = new ArrayList<>();
    private IJavaMethod getter;
    private IJavaMethod setter;

    public ZenExpandMember(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public IPartialExpression instance(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value) {
        return new InstanceGetValue(position, value);
    }

    public IPartialExpression instance(ZenPosition position, IEnvironmentGlobal environment) {
        return new StaticGetValue(position);
    }

    public void setGetter(IJavaMethod getter) {
        if(this.getter != null) {
            throw new RuntimeException(type + "." + name + " already has a getter");
        } else {
            this.getter = getter;
        }
    }

    public void setSetter(IJavaMethod setter) {
        if(this.setter != null) {
            throw new RuntimeException(type + "." + name + " already has a setter");
        } else {
            this.setter = setter;
        }
    }

    public void addMethod(IJavaMethod method) {
        methods.add(method);
    }

    private class InstanceGetValue implements IPartialExpression {

        private final ZenPosition position;
        private final IPartialExpression value;

        public InstanceGetValue(ZenPosition position, IPartialExpression value) {
            this.position = position;
            this.value = value;
        }

        @Override
        public Expression eval(IEnvironmentGlobal environment) {
            return new ExpressionCallStatic(position, environment, getter, value.eval(environment));
        }

        @Override
        public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
            return new ExpressionCallStatic(position, environment, setter, value.eval(environment), other);
        }

        @Override
        public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
            return getter.getReturnType().getMember(position, environment, this, name);
        }

        @Override
        public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
            Expression[] newValues = new Expression[values.length + 1];
            newValues[0] = value.eval(environment);
            System.arraycopy(values, 0, newValues, 1, values.length);
            IJavaMethod method = JavaMethod.select(true, methods, environment, newValues);
            if(method == null) {
                environment.error(position, StringUtil.methodMatchingError(methods, values));
                return new ExpressionInvalid(position);
            } else {
                return new ExpressionCallStatic(position, environment, method, newValues);
            }
        }

        @Override
        public IZenSymbol toSymbol() {
            return null;
        }

        @Override
        public ZenType getType() {
            return getter.getReturnType();
        }

        @Override
        public ZenType toType(IEnvironmentGlobal environment) {
            environment.error(position, "not a valid type");
            return ZenType.ANY;
        }

        @Override
        public ZenType[] predictCallTypes(int numArguments) {
            ZenType[] base = JavaMethod.predict(methods, numArguments + 1);
            ZenType[] result = new ZenType[base.length - 1];
            System.arraycopy(base, 1, result, 0, result.length);

            return result;
        }
    }

    public class StaticGetValue implements IPartialExpression {

        private final ZenPosition position;

        private StaticGetValue(ZenPosition position) {
            this.position = position;
        }

        @Override
        public Expression eval(IEnvironmentGlobal environment) {
            if (getter == null) {
                for(IJavaMethod method : methods) {
                    if (method.accepts(0))
                        return new ExpressionCallStatic(position, environment, method);
                }
                environment.error(position, "Could not evaluate ZenExpandMember " + name + " in " + type);
                return new ExpressionInvalid(position);
            }
            return new ExpressionCallStatic(position, environment, getter);
        }

        @Override
        public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
            return new ExpressionCallStatic(position, environment, setter, other);
        }

        @Override
        public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
            return getter.getReturnType().getMember(position, environment, this, name);
        }

        @Override
        public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
            IJavaMethod method = JavaMethod.select(true, methods, environment, values);
            if(method == null) {
                environment.error(position, StringUtil.methodMatchingError(methods, values));
                return new ExpressionInvalid(position);
            } else {
                return new ExpressionCallStatic(position, environment, method, values);
            }
        }

        @Override
        public IZenSymbol toSymbol() {
            return new StaticSymbol();
        }

        @Override
        public ZenType getType() {
            if (getter != null)
                return getter.getReturnType();
            if (methods.size() > 1)
                return methods.get(0).getReturnType();
            return ZenType.ANY;
        }

        @Override
        public ZenType toType(IEnvironmentGlobal environment) {
            environment.error(position, "not a valid type");
            return ZenType.ANY;
        }

        @Override
        public ZenType[] predictCallTypes(int numArguments) {
            return JavaMethod.predict(methods, numArguments);
        }
    }

    private class StaticSymbol implements IZenSymbol {

        @Override
        public IPartialExpression instance(ZenPosition position) {
            return new StaticGetValue(position);
        }
    }
}
