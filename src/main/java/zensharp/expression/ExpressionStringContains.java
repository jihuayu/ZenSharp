package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stan
 */
public class ExpressionStringContains extends Expression {

    private final Expression haystack;
    private final Expression needle;

    public ExpressionStringContains(ZenPosition position, Expression haystack, Expression needle) {
        super(position);

        this.haystack = haystack;
        this.needle = needle;
    }

    @Override
    public ZenType getType() {
        return ZenType.BOOL;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        
        haystack.compile(result, environment);
        needle.compile(result, environment);

        if(result) {
            environment.getOutput().invokeVirtual(String.class, "contains", boolean.class, CharSequence.class);
        }
    }
    
}
