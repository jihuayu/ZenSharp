package zensharp.statements;

import zensharp.compiler.IEnvironmentMethod;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class StatementNull extends Statement {

    public StatementNull(ZenPosition position) {
        super(position);
    }

    @Override
    public void compile(IEnvironmentMethod environment) {

    }
}
