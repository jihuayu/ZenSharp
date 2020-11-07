package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;


import zensharp.type.ZenType;
import zensharp.type.ZenTypeString;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

import java.util.List;

/**
 * @author Stanneke
 */
public class ExpressionStringConcat extends Expression {

    private final List<Expression> values;

    public ExpressionStringConcat(ZenPosition position, List<Expression> values) {
        super(position);

        this.values = values;
    }

    public void add(Expression value) {
        values.add(value);
    }

    @Override
    public ZenType getType() {
        return ZenTypeString.INSTANCE;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(result) {
            MethodOutput output = environment.getOutput();

            // Step 1: construct StringBuilder
            output.newObject(StringBuilder.class);
            output.dup();
            output.construct(StringBuilder.class);

            // Step 2: concatenate Strings
            for(Expression value : values) {
                value.compile(true, environment);
                output.invoke(StringBuilder.class, "append", StringBuilder.class, String.class);
            }

            // Step 3: return String
            output.invoke(StringBuilder.class, "toString", String.class);
        } else {
            for(Expression value : values) {
                value.compile(false, environment);
            }
        }
    }
}
