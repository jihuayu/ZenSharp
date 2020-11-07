package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.definitions.ParsedFunctionArgument;

import zensharp.expression.partial.IPartialExpression;
import zensharp.statements.Statement;


import zensharp.expression.ExpressionFunction;
import zensharp.expression.ExpressionInvalid;
import zensharp.expression.ExpressionJavaLambda;
import zensharp.expression.ExpressionJavaLambdaSimpleGeneric;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeAny;
import zensharp.type.ZenTypeFunctionCallable;
import zensharp.type.ZenTypeNative;
import zensharp.util.ZenPosition;
import zensharp.util.ZenTypeUtil;

import java.lang.reflect.*;
import java.util.List;

/**
 * @author Stan
 */
public class ParsedExpressionFunction extends ParsedExpression {
    
    private final ZenType returnType;
    private final List<ParsedFunctionArgument> arguments;
    private final List<Statement> statements;
    
    public ParsedExpressionFunction(ZenPosition position, ZenType returnType, List<ParsedFunctionArgument> arguments, List<Statement> statements) {
        super(position);
        
        this.returnType = returnType;
        this.arguments = arguments;
        this.statements = statements;
    }
    
    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        if(predictedType != null && predictedType instanceof ZenTypeNative) {
            System.out.println("Known predicted function type: " + predictedType);
            
            ZenTypeNative nativeType = (ZenTypeNative) predictedType;
            Class<?> nativeClass = nativeType.getNativeClass();
            Method method = ZenTypeUtil.findFunctionalInterfaceMethod(nativeClass);
            if(method != null) {
                // functional interface
                if(returnType != ZenTypeAny.INSTANCE && !returnType.canCastImplicit(environment.getType(method.getGenericReturnType()), environment)) {
                    environment.error(getPosition(), "return type not compatible");
                    return new ExpressionInvalid(getPosition());
                }
                if(arguments.size() != method.getParameterTypes().length) {
                    environment.error(getPosition(), String.format("Expected %s arguments, received %s arguments", method.getParameterTypes().length, arguments.size()));
                    return new ExpressionInvalid(getPosition());
                }
                for(int i = 0; i < arguments.size(); i++) {
                    ZenType argumentType = environment.getType(method.getParameterTypes()[i]);
                    if(arguments.get(i).getType() != ZenTypeAny.INSTANCE && !arguments.get(i).getType().canCastImplicit(argumentType, environment)) {
                        environment.error(getPosition(), "argument " + i + " doesn't match");
                        return new ExpressionInvalid(getPosition());
                    }
                }
                return isGeneric(method) ? new ExpressionJavaLambdaSimpleGeneric(getPosition(), nativeClass, arguments, statements, environment.getType(nativeClass)) : new ExpressionJavaLambda(getPosition(), nativeClass, arguments, statements, environment.getType(nativeClass));
            } else {
                environment.error(getPosition(), predictedType.toString() + " is not a functional interface");
                return new ExpressionInvalid(getPosition());
            }
        } else {
            if(predictedType != null && predictedType instanceof ZenTypeFunctionCallable) {
                return new ExpressionFunction(getPosition(), arguments, returnType, statements, ((ZenTypeFunctionCallable) predictedType).getClassName());
            }
            System.out.println("No known predicted type");
            return new ExpressionFunction(getPosition(), arguments, returnType, statements, environment.makeClassNameWithMiddleName(getPosition().getFile().getClassName()));
        }
    }
    
    private boolean isGeneric(Method method) {
        for(Type type : method.getGenericParameterTypes()) {
            if(type.getTypeName().equals("T"))
                return true;
        }
        return false;
        
    }
}
