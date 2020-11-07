package zensharp.expression.partial;


import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionNothing;
import zensharp.symbols.IZenSymbol;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeZenClass;
import zensharp.type.ZenTypeZenInterface;
import zensharp.util.ZenPosition;

public class PartialZSClassInterface implements IPartialExpression {

    private final ZenTypeZenInterface type;

    public PartialZSClassInterface(ZenTypeZenInterface type) {
        
        this.type = type;
    }
    
    @Override
    public Expression eval(IEnvironmentGlobal environment) {
        return new ExpressionNothing(type.zenClass.position);
    }
    
    @Override
    public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
        throw new UnsupportedOperationException("Cannot assign to a class");
    }
    
    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        return type.getMember(position, environment, this, name);
    }
    
    @Override
    public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
        return type.call(position, environment, this.eval(environment), values);
    }
    
    @Override
    public ZenType[] predictCallTypes(int numArguments) {
        return type.predictCallTypes(numArguments);
    }
    
    @Override
    public IZenSymbol toSymbol() {
        return position -> this;
    }
    
    @Override
    public ZenType getType() {
        return type;
    }
    
    @Override
    public ZenType toType(IEnvironmentGlobal environment) {
        return type;
    }
}
