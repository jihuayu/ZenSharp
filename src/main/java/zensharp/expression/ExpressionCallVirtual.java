package zensharp.expression;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.natives.IJavaMethod;
import zensharp.type.natives.JavaMethod;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionCallVirtual extends Expression {

    private final IJavaMethod method;

    private final Expression receiver;
    private final Expression[] arguments;

    public ExpressionCallVirtual(ZenPosition position, IEnvironmentGlobal environment, IJavaMethod method, Expression receiver, Expression... arguments) {
        super(position);

        this.method = method;

        this.receiver = receiver;
        this.arguments = JavaMethod.rematch(position, method, environment, arguments);
    }

    @Override
    public ZenType getType() {
        if(method instanceof JavaMethod && ((JavaMethod) method).returnsSelf)
            return receiver.getType();
        return method.getReturnType();
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        receiver.compile(true, environment);

        for(Expression argument : arguments) {
            argument.compile(true, environment);
        }

        method.invokeVirtual(environment.getOutput());
        if(!result && method.getReturnType() != ZenType.VOID) {
            environment.getOutput().pop(method.getReturnType().isLarge());
        } else if(method instanceof JavaMethod && ((JavaMethod) method).returnsSelf) {
            environment.getOutput().checkCast(receiver.getType().toASMType().getInternalName());
        }
    }
}
