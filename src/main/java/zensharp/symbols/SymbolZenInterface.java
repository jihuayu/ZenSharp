package zensharp.symbols;

import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialZSClass;
import zensharp.expression.partial.PartialZSClassInterface;
import zensharp.type.ZenTypeZenClass;
import zensharp.type.ZenTypeZenInterface;
import zensharp.util.ZenPosition;

public class SymbolZenInterface implements IZenSymbol {

    private final ZenTypeZenInterface type;

    public SymbolZenInterface(ZenTypeZenInterface type) {
        this.type = type;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialZSClassInterface(type);
    }
}
