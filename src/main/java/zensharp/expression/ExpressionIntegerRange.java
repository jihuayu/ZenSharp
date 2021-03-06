/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;
import zensharp.value.IntRange;

/**
 * @author Stanneke
 */
public class ExpressionIntegerRange extends Expression {

    private final Expression from;
    private final Expression to;

    public ExpressionIntegerRange(ZenPosition position, Expression from, Expression to) {
        super(position);

        this.from = from;
        this.to = to;
    }

    @Override
    public ZenType getType() {
        return ZenType.INTRANGE;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        MethodOutput output = environment.getOutput();
        output.newObject(IntRange.class);
        output.dup();
        from.compile(true, environment);
        to.compile(true, environment);
        output.construct(IntRange.class, int.class, int.class);
    }
}
