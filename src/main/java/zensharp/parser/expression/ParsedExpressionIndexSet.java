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
public class ParsedExpressionIndexSet extends ParsedExpression {
    
    private final ParsedExpression value;
    private final ParsedExpression index;
    private final ParsedExpression setValue;
    
    public ParsedExpressionIndexSet(ZenPosition position, ParsedExpression value, ParsedExpression index, ParsedExpression setValue) {
        super(position);
        
        this.value = value;
        this.index = index;
        this.setValue = setValue;
    }
    
    @Override
    public IPartialExpression compile(IEnvironmentMethod environment, ZenType predictedType) {
        // TODO: improve prediction in this expression
        Expression cValue = value.compile(environment, null).eval(environment);
        Expression cIndex = index.compile(environment, null).eval(environment);
        Expression cSetValue = setValue.compile(environment, null).eval(environment);
        return cValue.getType().trinary(getPosition(), environment, cValue, cIndex, cSetValue, OperatorType.INDEXSET);
    }
}
