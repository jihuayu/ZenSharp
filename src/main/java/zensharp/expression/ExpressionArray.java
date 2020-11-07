package zensharp.expression;

import org.objectweb.asm.Type;


import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeArray;
import zensharp.type.ZenTypeArrayBasic;
import zensharp.type.ZenTypeArrayList;
import zensharp.type.casting.ICastingRule;

import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

public class ExpressionArray extends Expression {

    private final Expression[] contents;
    private final ZenTypeArrayBasic type;

    public ExpressionArray(ZenPosition position, ZenTypeArrayBasic type, Expression... contents) {
        super(position);

        this.contents = contents;
        this.type = type;
    }

    @Override
    public Expression cast(ZenPosition position, IEnvironmentGlobal environment, ZenType type) {
        if(this.type.equals(type)) {
            return this;
        }

        if(type instanceof ZenTypeArray) {
            ZenTypeArray arrayType = (ZenTypeArray) type;
            Expression[] newContents = new Expression[contents.length];
            for(int i = 0; i < contents.length; i++) {
                newContents[i] = contents[i].cast(position, environment, arrayType.getBaseType());
            }
            if(type instanceof ZenTypeArrayBasic)
                return new ExpressionArray(getPosition(), (ZenTypeArrayBasic) arrayType, newContents);
            else
                return new ExpressionArrayList(getPosition(), (ZenTypeArrayList) arrayType, newContents);

        } else {
            ICastingRule castingRule = this.type.getCastingRule(type, environment);
            if(castingRule == null) {
                environment.error(position, "cannot cast " + this.type + " to " + type);
                return new ExpressionInvalid(position, type);
            } else {
                return new ExpressionAs(position, this, castingRule);
            }
        }
    }

    @Override
    public ZenType getType() {
        return type;
    }

    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        ZenType baseType = type.getBaseType();
        Type asmBaseType = type.getBaseType().toASMType();

        MethodOutput output = environment.getOutput();
        output.constant(contents.length);
        output.newArray(asmBaseType);

        for(int i = 0; i < contents.length; i++) {
            output.dup();
            output.constant(i);
            contents[i].cast(this.getPosition(), environment, baseType).compile(result, environment);
            output.arrayStore(asmBaseType);
        }
    }
}
