package zensharp.symbols;

import zensharp.type.natives.IJavaMethod;
import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialStaticGetter;

/**
 * @author Stanneke
 */
public class SymbolJavaStaticGetter implements IZenSymbol {
    
    private final IJavaMethod method;
    
    public SymbolJavaStaticGetter(IJavaMethod method) {
        this.method = method;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialStaticGetter(position, method);
    }
}
