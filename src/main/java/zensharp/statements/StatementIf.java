package zensharp.statements;

import org.objectweb.asm.Label;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.parser.expression.ParsedExpression;
import zensharp.type.ZenType;

import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

import java.util.*;

public class StatementIf extends Statement {
    
    private final ParsedExpression condition;
    private final Statement onThen;
    private final Statement onElse;
    
    public StatementIf(ZenPosition position, ParsedExpression condition, Statement onThen, Statement onElse) {
        super(position);
        
        this.condition = condition;
        this.onThen = onThen;
        this.onElse = onElse;
    }
    
    @Override
    public void compile(IEnvironmentMethod environment) {
        environment.getOutput().position(getPosition());
        
        Expression cCondition = condition.compile(environment, ZenType.BOOL).eval(environment).cast(getPosition(), environment, ZenType.BOOL);
        
        ZenType expressionType = cCondition.getType();
        if(expressionType.canCastImplicit(ZenType.BOOL, environment)) {
            Label labelEnd = new Label();
            Label labelElse = onElse == null ? labelEnd : new Label();
            
            cCondition.compileIf(labelElse, environment);
            onThen.compile(environment);
            
            MethodOutput methodOutput = environment.getOutput();
            if(onElse != null) {
                methodOutput.goTo(labelEnd);
                methodOutput.label(labelElse);
                onElse.compile(environment);
            }
            
            methodOutput.label(labelEnd);
        } else {
            environment.error(getPosition(), "condition is not a boolean");
        }
    }
    
    @Override
    public List<Statement> getSubStatements() {
        List<Statement> out = new ArrayList<>();
        out.add(this);
        out.addAll(onThen.getSubStatements());
        if(onElse != null)
            out.addAll(onElse.getSubStatements());
        return out;
    }
}
