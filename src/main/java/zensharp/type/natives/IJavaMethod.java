package zensharp.type.natives;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.expression.Expression;
import zensharp.type.ZenType;
import zensharp.util.MethodOutput;

/**
 * @author Stan
 */
public interface IJavaMethod {
    
    boolean isStatic();

    boolean isDeclare();

    boolean accepts(int numArguments);
    
    boolean accepts(IEnvironmentGlobal environment, Expression... arguments);
    
    int getPriority(IEnvironmentGlobal environment, Expression... arguments);
    
    void invokeVirtual(MethodOutput output);
    
    void invokeStatic(MethodOutput output);
    
    ZenType[] getParameterTypes();
    
    ZenType getReturnType();
    
    boolean isVarargs();
    
    String getErrorDescription();
}
