package zensharp.expression;

import org.objectweb.asm.Label;
import zensharp.annotations.CompareType;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;

import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

/**
 * @author Stanneke
 */
public class ExpressionCompareGeneric extends Expression {

    private final Expression value; // should return a compareTo value (<=>0)
    private final CompareType type;

    public ExpressionCompareGeneric(ZenPosition position, Expression value, CompareType type) {
        super(position);

        this.value = value;
        this.type = type;
    }

    @Override
    public ZenType getType() {
        return ZenType.BOOL;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        value.compile(result, environment);
        if(result) {
            MethodOutput output = environment.getOutput();
            Label lblThen = new Label();
            Label lblEnd = new Label();

            switch(type) {
                case LT:
                    output.ifLT(lblThen);
                    break;
                case GT:
                    output.ifGT(lblThen);
                    break;
                case LE:
                    output.ifLE(lblThen);
                    break;
                case GE:
                    output.ifGE(lblThen);
                    break;
                case NE:
                    output.ifNE(lblThen);
                    break;
                case EQ:
                    output.ifEQ(lblThen);
                    break;
                default:
                    throw new RuntimeException();
            }

            output.iConst0();
            output.goTo(lblEnd);
            output.label(lblThen);
            output.iConst1();
            output.label(lblEnd);
        }
    }
}
