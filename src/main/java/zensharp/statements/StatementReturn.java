package zensharp.statements;

import org.objectweb.asm.Type;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.parser.expression.ParsedExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class StatementReturn extends Statement {
    
    private final ZenType returnType;
    private final ParsedExpression expression;
    
    public StatementReturn(ZenPosition position, ZenType returnType, ParsedExpression expression) {
        super(position);
        
        this.returnType = returnType;
        this.expression = expression;
    }
    
    public ParsedExpression getExpression() {
        return expression;
    }
    
    @Override
    public boolean isReturn() {
        return false;
    }
    
    @Override
    public void compile(IEnvironmentMethod environment) {
        environment.getOutput().position(getPosition());
        
        if(expression == null) {
            environment.getOutput().ret();
        } else {
            Expression cExpression = expression.compile(environment, returnType).eval(environment);
            cExpression.compile(true, environment);
            
            Type returnType = cExpression.getType().toASMType();
            environment.getOutput().returnType(returnType);
        }
    }
}
