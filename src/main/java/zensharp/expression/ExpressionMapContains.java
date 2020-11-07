package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeAssociative;
import zensharp.util.ZenPosition;

import java.util.Map;

import static zensharp.util.ZenTypeUtil.internal;

/**
 * @author Stanneke
 */
public class ExpressionMapContains extends Expression {

    private final Expression map;
    private final Expression key;

    public ExpressionMapContains(ZenPosition position, Expression map, Expression key) {
        super(position);

        this.map = map;
        this.key = key;
    }

    @Override
    public ZenType getType() {
        return ZenType.BOOL;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(result) {
        	ZenTypeAssociative mapType = (ZenTypeAssociative) map.getType();
            map.compile(result, environment);
            key.cast(getPosition(), environment, mapType.getKeyType()).compile(result, environment);

            environment.getOutput().invokeInterface(internal(Map.class), "containsKey", "(Ljava/lang/Object;)Z");
        }
    }
}
