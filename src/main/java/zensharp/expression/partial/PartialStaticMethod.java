package zensharp.expression.partial;




import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionCallStatic;
import zensharp.expression.ExpressionInvalid;
import zensharp.symbols.IZenSymbol;
import zensharp.symbols.SymbolJavaStaticMethod;
import zensharp.type.ZenType;
import zensharp.type.natives.IJavaMethod;
import zensharp.util.ZenPosition;

import java.util.Arrays;

/**
 * @author Stanneke
 */
public class PartialStaticMethod implements IPartialExpression {

    private final ZenPosition position;
    private final IJavaMethod method;

    public PartialStaticMethod(ZenPosition position, IJavaMethod method) {
        this.position = position;
        this.method = method;
    }

    @Override
    public Expression eval(IEnvironmentGlobal environment) {
        environment.error(position, "not a valid expression");
        return new ExpressionInvalid(position);
    }

    @Override
    public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
        environment.error(position, "cannot alter this final");
        return new ExpressionInvalid(position);
    }

    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        environment.error(position, "methods have no members");
        return new ExpressionInvalid(position);
    }

    @Override
    public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
        if(method.accepts(environment, values)) {
            return new ExpressionCallStatic(position, environment, method, values);
        } else {
            environment.error(position, "parameter count mismatch: got " + values.length + " arguments");
            return new ExpressionInvalid(position, method.getReturnType());
        }
    }

    @Override
    public ZenType[] predictCallTypes(int numArguments) {
        return Arrays.copyOf(method.getParameterTypes(), numArguments);
    }

    @Override
    public IZenSymbol toSymbol() {
        return new SymbolJavaStaticMethod(method);
    }

    @Override
    public ZenType getType() {
        return method.getReturnType();
    }

    @Override
    public ZenType toType(IEnvironmentGlobal environment) {
        return ZenType.ANY;
    }
}
