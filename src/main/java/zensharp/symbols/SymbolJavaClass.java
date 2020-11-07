package zensharp.symbols;


import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialJavaClass;

import java.lang.reflect.Type;

/**
 * @author Stanneke
 */
public class SymbolJavaClass implements IZenSymbol {
    
    private final Type cls;
    
    public SymbolJavaClass(Type cls) {
        this.cls = cls;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialJavaClass(position, cls);
    }
    
    @Override
    public String toString() {
        return "SymbolJavaClass: " + cls.toString();
    }
}
