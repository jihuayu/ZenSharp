package zensharp.expression;



import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeArray;
import zensharp.util.ZenPosition;

/**
 * @author Stan
 */
public class ExpressionArrayGet extends Expression {
    
    private final Expression array;
    private final Expression index;
    private final ZenType baseType;
    
    public ExpressionArrayGet(ZenPosition position, Expression array, Expression index) {
        super(position);
        
        this.array = array;
        this.index = index;
        this.baseType = ((ZenTypeArray) array.getType()).getBaseType();
    }
    
    @Override
    public ZenType getType() {
        return baseType;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        array.compile(result, environment);
        index.compile(result, environment);
        
        if(result) {
            environment.getOutput().arrayLoad(baseType.toASMType());
        }
    }
    
    @Override
    public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
        return new ExpressionArraySet(position, array, index, other);
    }
}
