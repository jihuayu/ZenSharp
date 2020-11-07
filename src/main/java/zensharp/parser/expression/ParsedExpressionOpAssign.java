package zensharp.parser.expression;

import zensharp.annotations.OperatorType;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionOpAssign extends ParsedExpression {
    
    private final ParsedExpression left;
    private final ParsedExpression right;
    private final OperatorType operator;
    
    public ParsedExpressionOpAssign(ZenPosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
        super(position);
        
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
    
    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        // TODO: validate if the prediction rules are sound
        Expression cLeft = left.compile(environment, predictedType).eval(environment);
        Expression cRight = right.compile(environment, cLeft.getType()).eval(environment);
        
        Expression value = cLeft.getType().binary(getPosition(), environment, cLeft, cRight, operator);
        
        return left.compile(environment, predictedType).assign(getPosition(), environment, value);
    }
}
