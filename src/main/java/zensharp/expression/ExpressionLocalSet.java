package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.symbols.SymbolLocal;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionLocalSet extends Expression {
    
    private final SymbolLocal variable;
    private final Expression value;
    
    public ExpressionLocalSet(ZenPosition position, SymbolLocal variable, Expression value) {
        super(position);
        
        this.variable = variable;
        this.value = value;
    }
    
    @Override
    public ZenType getType() {
        return variable.getType();
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        int local = environment.getLocal(variable);
        
        value.compile(true, environment);
        if(result) {
            environment.getOutput().dup();
        }
        environment.getOutput().store(variable.getType().toASMType(), local);
    }
}
