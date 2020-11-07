package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionArgument extends Expression {
    
    private final int id;
    private final ZenType type;
    
    public ExpressionArgument(ZenPosition position, int id, ZenType type) {
        super(position);
        
        this.id = id;
        this.type = type;
    }
    
    @Override
    public ZenType getType() {
        return type;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        environment.getOutput().load(type.toASMType(), id);
    }
}
