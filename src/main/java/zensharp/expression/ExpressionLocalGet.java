package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.symbols.SymbolLocal;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionLocalGet extends Expression {
    
    private final SymbolLocal variable;
    
    public ExpressionLocalGet(ZenPosition position, SymbolLocal variable) {
        super(position);
        
        this.variable = variable;
    }
    
    @Override
    public ZenType getType() {
        return variable.getType();
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        int local = environment.getLocal(variable);
        environment.getOutput().load(variable.getType().toASMType(), local);
    }
}
