package zensharp.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ExpressionFunctionCall extends Expression {
    
    private final Expression[] values;
    private final ZenType returnType;
    private final Expression receiver;
    private final String className;
    private final String descriptor;
    
    

    
    public ExpressionFunctionCall(ZenPosition position, Expression[] values, ZenType returnType, Expression receiver, String className, String descriptor) {
        super(position);
        this.values = values;
        this.returnType = returnType;
        this.receiver = receiver;
        this.className = className;
        this.descriptor = descriptor;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        receiver.compile(true, environment);
        for (Expression value : values) {
            value.compile(true, environment);
        }
        environment.getOutput().invokeInterface(className, "accept", descriptor);
        if(returnType != ZenType.VOID && !result) {
            environment.getOutput().pop(returnType.isLarge());
        }
    }
    
    @Override
    public ZenType getType() {
        return returnType;
    }
}
