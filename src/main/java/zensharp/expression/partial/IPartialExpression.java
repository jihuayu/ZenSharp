package zensharp.expression.partial;


import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.symbols.IZenSymbol;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public interface IPartialExpression {
    
    Expression eval(IEnvironmentGlobal environment);
    
    Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other);
    
    IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name);
    
    Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values);
    
    ZenType[] predictCallTypes(int numArguments);
    
    IZenSymbol toSymbol();
    
    ZenType getType();
    
    ZenType toType(IEnvironmentGlobal environment);
}
