package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;

import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionInvalid;
import zensharp.expression.ExpressionString;

/**
 * @author Stanneke
 */
public class ParsedExpressionVariable extends ParsedExpression {
    
    private final String name;
    
    public ParsedExpressionVariable(ZenPosition position, String name) {
        super(position);
        
        this.name = name;
    }
    
    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        IPartialExpression result = environment.getValue(name, getPosition());
        if(result == null) {
            if(predictedType == null) {
                environment.error(getPosition(), "could not find " + name);
                return new ExpressionInvalid(getPosition());
            }
            
            // enable usage of static members of the same type as the predicted
            // type (eg. enum values)
            IPartialExpression member = predictedType.getStaticMember(getPosition(), environment, name);
            if(member == null || member.getType().getCastingRule(predictedType, environment) == null) {
                environment.error(getPosition(), "could not find " + name);
                return new ExpressionInvalid(getPosition());
            } else {
                return member;
            }
        } else {
            return result;
        }
    }
    
    @Override
    public Expression compileKey(IEnvironmentMethod environment, ZenType predictedType) {
        return new ExpressionString(getPosition(), name);
    }
}
