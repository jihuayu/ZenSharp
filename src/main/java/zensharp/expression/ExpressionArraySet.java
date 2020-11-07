package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stan
 */
public class ExpressionArraySet extends Expression {
    
    private final Expression array;
    private final Expression index;
    private final Expression value;
    
    public ExpressionArraySet(ZenPosition position, Expression array, Expression index, Expression value) {
        super(position);
        
        this.array = array;
        this.index = index;
        this.value = value;
    }
    
    @Override
    public ZenType getType() {
        return ZenType.VOID;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        array.compile(result, environment);
        index.compile(result, environment);
        value.compile(result, environment);
        
        if(result) {
            environment.getOutput().arrayStore(value.getType().toASMType());
        }
    }
}
