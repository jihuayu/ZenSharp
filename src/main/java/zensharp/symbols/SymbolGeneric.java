package zensharp.symbols;

import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialType;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author jihuayu
 */
public class SymbolGeneric implements IZenSymbol {

    private final ZenType type;

    private final String name;

    public SymbolGeneric(String name, ZenType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialType(position, type);
    }

    @Override
    public String toString() {
        return "SymbolType: " + name + ":" + type.toString();
    }

    public ZenType getType() {
        return type;
    }

}
