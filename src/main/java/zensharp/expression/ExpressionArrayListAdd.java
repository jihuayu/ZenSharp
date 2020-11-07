package zensharp.expression;

import java.util.List;

import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeArrayList;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

public class ExpressionArrayListAdd extends Expression {

	private final Expression array, value;
	private final ZenTypeArrayList type;
	
	public ExpressionArrayListAdd(ZenPosition position, IEnvironmentGlobal environment, Expression array, Expression val) {
		super(position);
		this.array = array;
		this.value = val;
		this.type = (ZenTypeArrayList) array.getType();
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, IEnvironmentMethod environment) {
		MethodOutput output = environment.getOutput();
		array.compile(true, environment);
		output.dup();
		value.compile(true, environment);
		output.invokeInterface(List.class, "add", boolean.class, Object.class);
		output.pop();
	}

}
