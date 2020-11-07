package zensharp.symbols;


import zensharp.type.ZenType;
import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialStaticGenerated;

/**
 * @author Stan
 */
public class SymbolZenStaticMethod implements IZenSymbol {
    
    private final String className;
    private final String methodName;
    private final String signature;
    private final ZenType[] argumentTypes;
    private final ZenType returnType;
    
    public SymbolZenStaticMethod(String className, String methodName, String signature, ZenType[] argumentTypes, ZenType returnType) {
        this.className = className;
        this.methodName = methodName;
        this.signature = signature;
        this.argumentTypes = argumentTypes;
        this.returnType = returnType;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialStaticGenerated(position, className, methodName, signature, argumentTypes, returnType);
    }
}
