package zensharp.symbols;

import zensharp.expression.partial.IPartialExpression;
import zensharp.util.ZenPosition;

import java.util.*;

/**
 * @author Stanneke
 */
public interface IZenSymbol {
    
    IPartialExpression instance(ZenPosition position);

}
