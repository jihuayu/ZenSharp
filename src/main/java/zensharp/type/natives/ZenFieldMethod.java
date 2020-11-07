package zensharp.type.natives;

import org.objectweb.asm.Opcodes;

import zensharp.expression.Expression;
import zensharp.type.ZenType;
import zensharp.util.MethodOutput;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.ITypeRegistry;

import java.lang.reflect.Field;

public class ZenFieldMethod implements IJavaMethod {
    
    private final Field field;
    private final ZenType type;
    private final boolean isSetter;
    
    public ZenFieldMethod(Field field, ITypeRegistry types, boolean isSetter) {
        this.field = field;
        this.isSetter = isSetter;
        this.type = types.getType(field.getType());
    }
    
    
    @Override
    public boolean isStatic() {
        return (field.getModifiers() & Opcodes.ACC_STATIC) != 0;
    }
    
    @Override
    public boolean accepts(int numArguments) {
        return numArguments == (isSetter ? 1 : 0);
    }
    
    @Override
    public boolean accepts(IEnvironmentGlobal environment, Expression... arguments) {
        return arguments.length == (isSetter ? 1 : 0);
    }
    
    @Override
    public int getPriority(IEnvironmentGlobal environment, Expression... arguments) {
        return 0;
    }
    
    @Override
    public void invokeVirtual(MethodOutput output) {
        if(isSetter)
            output.putField(field.getDeclaringClass(), field.getName(), field.getType());
        else
            output.getField(field.getDeclaringClass(), field.getName(), field.getType());
    }
    
    @Override
    public void invokeStatic(MethodOutput output) {
        if(!isStatic())
            throw new UnsupportedOperationException("Cannot perform static operations on a nonstatic Field");
        if(isSetter)
            output.putStaticField(field.getDeclaringClass(), field);
        else
            output.getStaticField(field.getDeclaringClass(), field);
        
    }
    
    @Override
    public ZenType[] getParameterTypes() {
        return isSetter ? new ZenType[]{type} : new ZenType[0];
    }
    
    @Override
    public ZenType getReturnType() {
        return isSetter ? ZenType.VOID : type;
    }
    
    @Override
    public boolean isVarargs() {
        return false;
    }
    
    @Override
    public String getErrorDescription() {
        return "FIELD " + field.getName();
    }
}
