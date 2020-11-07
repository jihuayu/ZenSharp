package zensharp.expression;


import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.definitions.ParsedFunctionArgument;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeFunction;
import zensharp.type.natives.JavaMethod;
import zensharp.util.ZenPosition;

import java.util.*;

/**
 * @author Stanneke
 */

@Deprecated
public class ExpressionJavaMethodStatic extends Expression {
    
    private final JavaMethod method;
    private final ZenType type;
    
    public ExpressionJavaMethodStatic(ZenPosition position, JavaMethod method, IEnvironmentGlobal environment) {
        super(position);
        
        this.method = method;
        
        List<ParsedFunctionArgument> arguments = new ArrayList<>();
        for(int i = 0; i < method.getParameterTypes().length; i++) {
            arguments.add(new ParsedFunctionArgument("p" + i, environment.getType(method.getMethod().getGenericParameterTypes()[i])));
        }
        
        this.type = new ZenTypeFunction(environment.getType(method.getMethod().getGenericReturnType()), arguments);
    }
    
    @Override
    public ZenType getType() {
        return type;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        // TODO: compile
    }
}
