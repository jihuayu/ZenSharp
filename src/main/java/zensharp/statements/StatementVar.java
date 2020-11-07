package zensharp.statements;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.expression.Expression;
import zensharp.parser.expression.ParsedExpression;
import zensharp.symbols.SymbolLocal;

import zensharp.type.ZenType;
import zensharp.type.ZenTypeAny;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class StatementVar extends Statement {

    private final String name;
    private final ZenType type;
    private final ParsedExpression initializer;
    private final boolean isFinal;

    public StatementVar(ZenPosition position, String name, ZenType type, ParsedExpression initializer, boolean isFinal) {
        super(position);

        this.name = name;
        this.type = type;
        this.initializer = initializer;
        this.isFinal = isFinal;
    }

    @Override
    public void compile(IEnvironmentMethod environment) {
        environment.getOutput().position(getPosition());

        Expression cInitializer = initializer == null ? null : initializer.compile(environment, type).eval(environment);
        ZenType cType = type == null ? (cInitializer == null ? ZenTypeAny.INSTANCE : cInitializer.getType()) : type;
        SymbolLocal symbol = new SymbolLocal(cType, isFinal);

        environment.putValue(name, symbol, getPosition());

        if(cInitializer != null) {
            cInitializer.compile(true, environment);
            environment.getOutput().store(symbol.getType().toASMType(), environment.getLocal(symbol));
        }
    }
}
