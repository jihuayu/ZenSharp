package zensharp.symbols;


import zensharp.type.natives.IJavaMethod;
import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialStaticMethod;

/**
 * @author Stanneke
 */
public class SymbolJavaStaticMethod implements IZenSymbol {
    
    private final IJavaMethod method;
    
    public SymbolJavaStaticMethod(IJavaMethod method) {
        this.method = method;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialStaticMethod(position, method);
    }
    
    @Override
    public String toString() {
        return "SymbolJavaStaticMethod: " + method.toString();
    }
}
