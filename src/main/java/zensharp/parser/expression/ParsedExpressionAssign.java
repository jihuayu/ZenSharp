package zensharp.parser.expression;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.partial.IPartialExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ParsedExpressionAssign extends ParsedExpression {
    
    private final ParsedExpression left;
    private final ParsedExpression right;
    
    public ParsedExpressionAssign(ZenPosition position, ParsedExpression left, ParsedExpression right) {
        super(position);
        
        this.left = left;
        this.right = right;
    }
    
    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        IPartialExpression cLeft = left.compile(environment, predictedType);
        return cLeft.assign(getPosition(), environment, right.compile(environment, cLeft.getType()).eval(environment));
    }
}
