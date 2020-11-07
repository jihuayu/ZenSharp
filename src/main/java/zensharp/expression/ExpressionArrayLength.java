package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeInt;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionArrayLength extends Expression {
    
    private final Expression value;
    
    public ExpressionArrayLength(ZenPosition position, Expression value) {
        super(position);
        
        this.value = value;
    }
    
    @Override
    public ZenType getType() {
        return ZenTypeInt.INSTANCE;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(result) {
            value.compile(true, environment);
            environment.getOutput().arrayLength();
        }
    }
}
