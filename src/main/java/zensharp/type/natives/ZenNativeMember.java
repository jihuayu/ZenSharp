package zensharp.type.natives;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;

import zensharp.expression.partial.IPartialExpression;
import zensharp.symbols.IZenSymbol;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeFunctionCallable;
import zensharp.util.ZenPosition;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionCallStatic;
import zensharp.expression.ExpressionCallVirtual;
import zensharp.expression.ExpressionInvalid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static zensharp.util.StringUtil.methodMatchingError;

/**
 * @author Stan
 */
public class ZenNativeMember {
    
    private final List<IJavaMethod> methods = new ArrayList<>();
    private IJavaMethod getter;
    private IJavaMethod setter;
    private final boolean isParent;

    public ZenNativeMember(){
        this(false);
    }
    public ZenNativeMember(boolean isParent){
        this.isParent=isParent;
    }

    public boolean isParent() {
        return isParent;
    }

    public IJavaMethod getGetter() {
        return getter;
    }

    public void setGetter(IJavaMethod getter) {
        if(this.getter == null) {
            this.getter = getter;
        } else {
            throw new RuntimeException("already has a getter");
        }
    }
    
    public IJavaMethod getSetter() {
        return setter;
    }
    
    public void setSetter(IJavaMethod setter) {
        if(this.setter == null) {
            this.setter = setter;
        } else {
            throw new RuntimeException("already has a setter");
        }
    }
    
    public IPartialExpression instance(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value) {
        return new InstanceGetValue(position, value);
    }
    
    public IPartialExpression instance(ZenPosition position, IEnvironmentGlobal environment) {
        return new StaticGetValue(position);
    }
    
    public void addMethod(IJavaMethod method) {
        methods.add(method);
    }
    
    public List<IJavaMethod> getMethods() {
        return methods;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(methods.toArray());
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
            return new ExpressionCallVirtual(position, environment, getter, value.eval(environment));
        }
        
        @Override
        public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
            return new ExpressionCallVirtual(position, environment, setter, value.eval(environment), other);
        }
        
        @Override
        public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
            final ZenType type = getType();
            if(type == null){
                environment.error(position, "No such member: " + name);
                return new ExpressionInvalid(position);
            }
            return type.getMember(position, environment, this, name);
        }
        
        @Override
        public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
            IJavaMethod method = JavaMethod.select(false, methods, environment, values);
            if(method == null) {
                if(getter != null && getType() instanceof ZenTypeFunctionCallable) {
                    return eval(environment).call(position, environment, values);
                }
                environment.error(position, methodMatchingError(methods, values));
                return new ExpressionInvalid(position);
            } else {
                return new ExpressionCallVirtual(position, environment, method, value.eval(environment), values);
            }
        }
        
        @Override
        public ZenType[] predictCallTypes(int numArguments) {
            return JavaMethod.predict(methods, numArguments);
        }
        
        @Override
        public IZenSymbol toSymbol() {
            return null;
        }
        
        @Override
        public ZenType getType() {
            return getter != null ? getter.getReturnType() : setter != null ? setter.getParameterTypes()[0] : null;
        }
        
        @Override
        public ZenType toType(IEnvironmentGlobal environment) {
            environment.error(position, "not a valid type");
            return ZenType.ANY;
        }
    }
    
    public class StaticGetValue implements IPartialExpression {
        
        private final ZenPosition position;
        
        private StaticGetValue(ZenPosition position) {
            this.position = position;
        }
        
        @Override
        public Expression eval(IEnvironmentGlobal environment) {
            if(getter == null)
                throw new RuntimeException("No Getter found!");
            return new ExpressionCallStatic(position, environment, getter);
        }
        
        @Override
        public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
            if(setter == null)
                throw new RuntimeException("No Setter found!");
            return new ExpressionCallStatic(position, environment, setter, other);
        }
        
        @Override
        public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
            final ZenType type = getType();
            if(type == null){
                environment.error(position, "No such member: " + name);
                return new ExpressionInvalid(position);
            }
            return type.getMember(position, environment, this, name);
        }
        
        @Override
        public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
            IJavaMethod method = JavaMethod.select(true, methods, environment, values);
            if(method == null) {
                if(getter != null && getType() instanceof ZenTypeFunctionCallable) {
                    return eval(environment).call(position, environment, values);
                }
                environment.error(position, methodMatchingError(methods, values));
                return new ExpressionInvalid(position);
            } else {
                return new ExpressionCallStatic(position, environment, method, values);
            }
        }
        
        @Override
        public ZenType[] predictCallTypes(int numArguments) {
            return JavaMethod.predict(methods, numArguments);
        }
        
        @Override
        public IZenSymbol toSymbol() {
            return new StaticSymbol();
        }
        
        @Override
        public ZenType getType() {
            return getter != null ? getter.getReturnType() : setter != null ? setter.getParameterTypes()[0] : null;
        }
        
        @Override
        public ZenType toType(IEnvironmentGlobal environment) {
            environment.error(position, "not a valid type");
            return ZenType.ANY;
        }
    }
    
    private class StaticSymbol implements IZenSymbol {
        
        @Override
        public IPartialExpression instance(ZenPosition position) {
            return new StaticGetValue(position);
        }
    }
}
