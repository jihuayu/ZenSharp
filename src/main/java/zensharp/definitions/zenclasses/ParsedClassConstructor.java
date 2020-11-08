package zensharp.definitions.zenclasses;

import org.objectweb.asm.*;
import zensharp.ZenTokener;

import zensharp.definitions.ParsedFunctionArgument;
import zensharp.expression.Expression;
import zensharp.statements.Statement;
import zensharp.symbols.SymbolArgument;


import zensharp.compiler.EnvironmentMethod;
import zensharp.compiler.IEnvironmentClass;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeZenClass;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;
import zensharp.util.ZenTypeUtil;

import java.util.*;
import java.util.stream.IntStream;

public class ParsedClassConstructor {

    final ZenType[] types;
    private final String[] names;
    private final List<Statement> statements;
    private final String signature;

    private ParsedClassConstructor(List<ZenType> types, List<String> names, List<Statement> statements) {
        this.types = types.toArray(new ZenType[types.size()]);
        this.names = names.toArray(new String[names.size()]);
        this.statements = statements;

        StringBuilder sig = new StringBuilder();
        sig.append("(");
        for(ZenType argument : types) {
            sig.append(argument.getSignature());
        }
        sig.append(")");
        sig.append("V");
        signature = sig.toString();
    }
    private ParsedClassConstructor() {
        this(new LinkedList<>(),new LinkedList<>(),new LinkedList<>());
    }

    static ParsedClassConstructor parse(ZenTokener parser, IEnvironmentGlobal environment) {
        parser.required(ZenTokener.T_BROPEN, "( Needed");
        List<ZenType> types = new LinkedList<>();
        List<String> names = new LinkedList<>();

        while(parser.optional(ZenTokener.T_BRCLOSE) == null) {
            String name = parser.required(ZenTokener.T_ID, "Parameter identifier required").getValue();
            ZenType type = ZenType.ANY;
            if(parser.optional(ZenTokener.T_AS) != null)
                type = ZenType.read(parser, environment);
            if(names.contains(name))
                environment.error("Constructor parameter already present: " + name);
            types.add(type);
            names.add(name);
            parser.optional(ZenTokener.T_COMMA);
        }
        parser.required(ZenTokener.T_AOPEN, "{ required");
        List<Statement> statements = new ArrayList<>();
        while(parser.optional(ZenTokener.T_ACLOSE) == null) {
            statements.add(Statement.read(parser, environment, null));
        }
        return new ParsedClassConstructor(types, names, statements);
    }

    static ParsedClassConstructor empty() {
        return new ParsedClassConstructor();
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder("(");
        for(ZenType type : types) {
            builder.append(type.toASMType().getDescriptor());
        }
        return builder.append(")V").toString();
    }

    public void writeAll(IEnvironmentClass environmentNewClass, ClassVisitor newClass, List<ParsedZenClassField> nonStatics, String className, ZenPosition position, ZenType parent) {
        final MethodOutput init = new MethodOutput(newClass, Opcodes.ACC_PUBLIC, "<init>", this.getDescription(), signature, null);
        EnvironmentMethod initEnvironment = new EnvironmentMethod(init, environmentNewClass);
        init.start();
        init.loadObject(0);
        if (parent==null){
            init.invokeSpecial(ZenTypeUtil.internal(Object.class), "<init>", "()V");
        }
        else {
            init.invokeSpecial(ZenTypeUtil.internal(parent.toJavaClass()), "<init>", "()V");
        }

        for(ParsedZenClassField nonStatic : nonStatics) {
            if(!nonStatic.hasInitializer())
                continue;
            init.loadObject(0);
            final Expression expression = nonStatic.initializer.compile(initEnvironment, nonStatic.type).eval(environmentNewClass);
            if(nonStatic.type == ZenType.ANY)
                nonStatic.type = expression.getType();

            expression.compile(true, initEnvironment);
            init.putField(className, nonStatic.name, nonStatic.type.toASMType().getDescriptor());
        }

        final EnvironmentMethod environmentMethod = new EnvironmentMethod(init, environmentNewClass);
        this.injectParameters(environmentMethod, position);
        this.writeConstructor(environmentMethod);
        init.ret();
        init.end();
    }


    public void writeConstructor(IEnvironmentMethod environmentMethod) {
        for(Statement statement : statements) {
            statement.compile(environmentMethod);
        }
    }

    public boolean canAccept(Expression[] arguments, IEnvironmentGlobal environment) {
        return arguments.length == types.length && IntStream.range(0, arguments.length).allMatch(i -> arguments[i].getType().canCastImplicit(types[i], environment));
    }

    public Expression call(ZenPosition position, Expression[] arguments, ZenTypeZenClass type) {
        if(arguments.length != this.types.length)
            throw new IllegalArgumentException(String.format("Expected %d arguments, received %d", types.length, arguments.length));
        return new ExpressionCallConstructor(position, type, arguments);
    }

    public void injectParameters(IEnvironmentMethod environmentMethod, ZenPosition position) {
        for(int i = 0, j = 0; i < types.length; i++) {
            environmentMethod.putValue(names[i], new SymbolArgument(i + 1 + j, types[i]), position);
            if(types[i].isLarge())
                j++;
        }
    }

    private final class ExpressionCallConstructor extends Expression {

        private final ZenTypeZenClass type;
        private final Expression[] arguments;

        ExpressionCallConstructor(ZenPosition position, ZenTypeZenClass type, Expression[] arguments) {
            super(position);
            this.type = type;
            this.arguments = arguments;
        }

        @Override
        public void compile(boolean result, IEnvironmentMethod environment) {
            MethodOutput output = environment.getOutput();
            output.newObject(type.getName());
            output.dup();
            for(int i = 0; i < arguments.length; i++) {
                Expression ex = arguments[i];
                ex.cast(ex.getPosition(), environment, types[i]).compile(true, environment);
            }
            output.invokeSpecial(type.getName(), "<init>", ParsedClassConstructor.this.getDescription());
        }

        @Override
        public ZenType getType() {
            return type;
        }
    }
}
