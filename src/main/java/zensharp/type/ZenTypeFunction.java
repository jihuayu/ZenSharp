package zensharp.type;

import org.objectweb.asm.Type;


import zensharp.annotations.CompareType;
import zensharp.annotations.OperatorType;
import zensharp.definitions.ParsedFunctionArgument;

import zensharp.expression.partial.IPartialExpression;


import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionInvalid;
import zensharp.expression.ExpressionNull;
import zensharp.type.casting.CastingRuleMatchedFunction;
import zensharp.type.casting.ICastingRule;
import zensharp.type.casting.ICastingRuleDelegate;
import zensharp.util.ZenPosition;
import zensharp.util.ZenTypeUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Stanneke
 */
public class ZenTypeFunction extends ZenType {
    
    protected final ZenType returnType;
    protected final ZenType[] argumentTypes;
    private final String name;
    protected final Map<ZenType, CastingRuleMatchedFunction> implementedInterfaces = new HashMap<>();
    
    public ZenTypeFunction(ZenType returnType, List<ParsedFunctionArgument> arguments) {
        this.returnType = returnType;
        argumentTypes = new ZenType[arguments.size()];
        for(int i = 0; i < argumentTypes.length; i++) {
            argumentTypes[i] = arguments.get(i).getType();
        }
        
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append("function(");
        boolean first = true;
        for(ZenType type : argumentTypes) {
            if(first) {
                first = false;
            } else {
                nameBuilder.append(',');
            }
            nameBuilder.append(type.getName());
        }
        nameBuilder.append(returnType.getName());
        name = nameBuilder.toString();
    }
    
    public ZenTypeFunction(ZenType returnType, ZenType[] argumentTypes) {
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
        
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append("function(");
        boolean first = true;
        for(ZenType type : argumentTypes) {
            if(first) {
                first = false;
            } else {
                nameBuilder.append(',');
            }
            nameBuilder.append(type.getName());
        }
        nameBuilder.append(')');
        nameBuilder.append(returnType.getName());
        name = nameBuilder.toString();
    }
    
    @Override
    public String getAnyClassName(IEnvironmentGlobal global) {
        throw new UnsupportedOperationException("functions cannot yet be used as any value");
    }
    
    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name) {
        environment.error(position, "Functions have no members");
        return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
    }
    
    @Override
    public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        environment.error(position, "Functions have no static members");
        return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
    }
    
    @Override
    public IZenIterator makeIterator(int numValues, IEnvironmentMethod methodOutput) {
        return null;
    }
    
    @Override
    public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {
        if(followCasters) {
            constructExpansionCastingRules(environment, rules);
        }
    }
    
    @Override
    public ICastingRule getCastingRule(ZenType type, IEnvironmentGlobal environment) {
        if(implementedInterfaces.containsKey(type)) {
            return implementedInterfaces.get(type);
        }
        
        Class<?> cls = type.toJavaClass();
        
        System.out.println("Can cast this function to " + cls.getName() + "?");
    
        final Method method = ZenTypeUtil.findFunctionalInterfaceMethod(cls);
        if(method != null) {
            // this is a functional interface
            // do the method signatures match?
            ZenType methodReturnType = environment.getType(method.getGenericReturnType());
            ICastingRule returnCastingRule = null;
            if(!returnType.equals(methodReturnType)) {
                returnCastingRule = returnType.getCastingRule(environment.getType(method.getGenericReturnType()), environment);
                if(returnCastingRule == null) {
                    System.out.println("Return types don't match");
                    return null;
                }
            }
            
            java.lang.reflect.Type[] methodParameters = method.getGenericParameterTypes();
            if(methodParameters.length < argumentTypes.length) {
                System.out.println("Argument count doesn't match");
                return null;
            }
            
            ICastingRule[] argumentCastingRules = new ICastingRule[argumentTypes.length];
            for(int i = 0; i < argumentCastingRules.length; i++) {
                ZenType argumentType = environment.getType(methodParameters[i]);
                if(!argumentType.equals(argumentTypes[i])) {
                    argumentCastingRules[i] = argumentType.getCastingRule(argumentTypes[i], environment);
                    if(argumentCastingRules[i] == null) {
                        System.out.println("Argument " + i + " doesn't match");
                        System.out.println("Cannot cast " + argumentType.getName() + " to " + argumentTypes[i].getName());
                        return null;
                    }
                }
            }
            
            CastingRuleMatchedFunction castingRule = new CastingRuleMatchedFunction(this, type, returnCastingRule, argumentCastingRules);
            implementedInterfaces.put(type, castingRule);
            System.out.println("Can cast this function");
            return castingRule;
        }
        
        return null;
    }
    
    @Override
    public Type toASMType() {
        return Type.getType(Object.class); // TODO: NEXT: expand
    }
    
    @Override
    public int getNumberType() {
        return 0;
    }
    
    @Override
    public String getSignature() {
        return "Ljava/lang/Object;"; // TODO: NEXT: expand
    }
    
    @Override
    public boolean isPointer() {
        return true;
    }
    
    @Override
    public Expression unary(ZenPosition position, IEnvironmentGlobal environment, Expression value, OperatorType operator) {
        environment.error(position, "cannot apply operators on a function");
        return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
    }
    
    @Override
    public Expression binary(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, OperatorType operator) {
        environment.error(position, "cannot apply operators on a function");
        return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
    }
    
    @Override
    public Expression trinary(ZenPosition position, IEnvironmentGlobal environment, Expression first, Expression second, Expression third, OperatorType operator) {
        environment.error(position, "cannot apply operators on a function");
        return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
    }
    
    @Override
    public Expression compare(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, CompareType type) {
        environment.error(position, "cannot apply operators on a function");
        return new ExpressionInvalid(position, ZenTypeAny.INSTANCE);
    }
    
    @Override
    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
        return null; // TODO: complete
    }
    
    @Override
    public ZenType[] predictCallTypes(int numArguments) {
        return Arrays.copyOf(argumentTypes, numArguments);
    }
    
    @Override
    public Class toJavaClass() {
        // TODO: complete
        return null;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Expression defaultValue(ZenPosition position) {
        return new ExpressionNull(position);
    }
    
    public ZenType getReturnType() {
        return returnType;
    }
    
    public ZenType[] getArgumentTypes() {
        return argumentTypes;
    }
    
    @Override
    public String getNameForInterfaceSignature() {
        return ZenTypeFunctionCallable.makeInterfaceName(returnType, argumentTypes);
    }
}