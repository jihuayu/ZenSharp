package zensharp.symbols;

import zensharp.dump.IDumpable;

import zensharp.type.ZenType;
import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialType;

import java.util.*;

/**
 * @author Stan
 */
public class SymbolType implements IZenSymbol {
    
    private final ZenType type;
    
    public SymbolType(ZenType type) {
        this.type = type;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new PartialType(position, type);
    }
    
    @Override
    public String toString() {
        return "SymbolType: " + type.toString();
    }
    
    public ZenType getType() {
        return type;
    }
    
    @Override
    public List<? extends IDumpable> asDumpedObject() {
        return type.asDumpedObject();
    }
}
