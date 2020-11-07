package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeString;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionString extends Expression {

    private final String value;

    public ExpressionString(ZenPosition position, String value) {
        super(position);

        this.value = value;
    }

    @Override
    public ZenType getType() {
        return ZenTypeString.INSTANCE;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(result) {
            environment.getOutput().constant(value);
        }
    }
}
