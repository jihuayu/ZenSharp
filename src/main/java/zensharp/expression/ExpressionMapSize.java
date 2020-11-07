package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeInt;
import zensharp.util.ZenPosition;

import java.util.Map;

import static zensharp.util.ZenTypeUtil.internal;

/**
 * @author Stanneke
 */
public class ExpressionMapSize extends Expression {
    
    private final Expression map;
    
    public ExpressionMapSize(ZenPosition position, Expression map) {
        super(position);
        
        this.map = map;
    }
    
    @Override
    public ZenType getType() {
        return ZenTypeInt.INSTANCE;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(result) {
            map.compile(true, environment);
            environment.getOutput().invokeInterface(internal(Map.class), "size", "()I");
        }
    }
}
