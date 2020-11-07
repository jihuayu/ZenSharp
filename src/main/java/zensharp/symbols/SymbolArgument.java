package zensharp.symbols;

import zensharp.expression.ExpressionArgument;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class SymbolArgument implements IZenSymbol {
    
    private final int id;
    private final ZenType type;
    
    public SymbolArgument(int id, ZenType type) {
        this.id = id;
        this.type = type;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new ExpressionArgument(position, id, type);
    }
}
