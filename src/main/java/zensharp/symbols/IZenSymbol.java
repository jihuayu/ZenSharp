package zensharp.symbols;

import zensharp.dump.IDumpConvertable;
import zensharp.dump.IDumpable;
import zensharp.dump.types.DumpDummy;
import zensharp.expression.partial.IPartialExpression;
import zensharp.util.ZenPosition;

import java.util.*;

/**
 * @author Stanneke
 */
public interface IZenSymbol extends IDumpConvertable {
    
    IPartialExpression instance(ZenPosition position);
    
    @Override
    default List<? extends IDumpable> asDumpedObject() {
        return Collections.singletonList(new DumpDummy(this));
    }
}
