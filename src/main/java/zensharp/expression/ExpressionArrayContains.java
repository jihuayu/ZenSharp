package zensharp.expression;




import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeArray;
import zensharp.type.ZenTypeArrayList;
import zensharp.util.ArrayUtil;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

import java.util.Collection;

public class ExpressionArrayContains extends Expression {
    
    private final ZenTypeArray type;
    private final Expression array, toCheck;
    
    public ExpressionArrayContains(ZenPosition position, IEnvironmentGlobal environment, Expression array, Expression toCheck) {
        super(position);
        this.type = (ZenTypeArray) array.getType();
        this.array = array;
        this.toCheck = toCheck;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        array.compile(result, environment);
        toCheck.compile(result, environment);
        
        if(!result)
            return;
        
        if(type instanceof ZenTypeArrayList)
            compileList(environment.getOutput());
        else
            compileArray(environment.getOutput());
    }
    
    
    @Override
    public ZenType getType() {
        return ZenType.BOOL;
    }
    
    private final void compileList(MethodOutput output) {
        output.invokeInterface(Collection.class, "contains", boolean.class, Object.class);
    }
    
    
    private final void compileArray(MethodOutput output) {
        if (type.getBaseType().toJavaClass().isPrimitive()) {
            output.invokeStatic(ArrayUtil.class, "contains", boolean.class, type.toJavaClass(), type.getBaseType().toJavaClass());
        } else {
            output.invokeStatic(ArrayUtil.class, "contains", boolean.class, Object[].class, Object.class);
        }
    }
}
