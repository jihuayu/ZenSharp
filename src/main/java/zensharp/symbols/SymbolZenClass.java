package zensharp.symbols;




import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialZSClass;
import zensharp.type.ZenTypeZenClass;
import zensharp.util.ZenPosition;

public class SymbolZenClass implements IZenSymbol {
    
    
    private final ZenTypeZenClass type;
    
    public SymbolZenClass(ZenTypeZenClass type) {
        this.type = type;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialZSClass(type);
    }
}
