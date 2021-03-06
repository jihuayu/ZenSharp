package zensharp.definitions.zenInterface;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import zensharp.ZenTokener;
import zensharp.compiler.*;
import zensharp.definitions.ParsedFunction;
import zensharp.definitions.ParsedFunctionArgument;
import zensharp.definitions.ParsedFunctionDeclare;
import zensharp.definitions.zenclasses.ParsedZenClassMethod;
import zensharp.expression.Expression;
import zensharp.parser.Token;
import zensharp.statements.Statement;
import zensharp.statements.StatementReturn;
import zensharp.symbols.SymbolArgument;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeAny;
import zensharp.type.natives.IJavaMethod;
import zensharp.type.natives.JavaMethod;
import zensharp.type.natives.ZenNativeMember;
import zensharp.util.MethodOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParsedZenInterfaceMethod {


    final ParsedFunctionDeclare method;
    final String className;

    public ParsedZenInterfaceMethod(ParsedFunctionDeclare parse, String className) {

        this.method = parse;
        this.className = className;
    }

    public static ParsedZenInterfaceMethod parse(ZenTokener parser, EnvironmentScript classEnvironment, String className) {
        Token tName = parser.required(ZenTokener.T_ID, "identifier expected");

        // function (argname [as type], argname [as type], ...) [as type];

        parser.required(ZenTokener.T_BROPEN, "( expected");

        List<ParsedFunctionArgument> arguments = new ArrayList<>();
        if(parser.optional(ZenTokener.T_BRCLOSE) == null) {
            Token argName = parser.required(ZenTokener.T_ID, "identifier expected");
            ZenType type = ZenTypeAny.INSTANCE;
            if(parser.optional(ZenTokener.T_AS) != null) {
                type = ZenType.read(parser, classEnvironment);
            }

            arguments.add(new ParsedFunctionArgument(argName.getValue(), type));

            while(parser.optional(ZenTokener.T_COMMA) != null) {
                Token argName2 = parser.required(ZenTokener.T_ID, "identifier expected");
                ZenType type2 = ZenTypeAny.INSTANCE;
                if(parser.optional(ZenTokener.T_AS) != null) {
                    type2 = ZenType.read(parser, classEnvironment);
                }

                arguments.add(new ParsedFunctionArgument(argName2.getValue(), type2));
            }

            parser.required(ZenTokener.T_BRCLOSE, ") expected");
        }

        ZenType type = ZenTypeAny.INSTANCE;
        if(parser.optional(ZenTokener.T_AS) != null) {
            type = ZenType.read(parser, classEnvironment);
        }

        parser.required(ZenTokener.T_SEMICOLON, "; expected");

        return new ParsedZenInterfaceMethod(new ParsedFunctionDeclare(tName.getPosition(), tName.getValue(), arguments, type), className);
    }

    public void addToMember(ZenNativeMember zenNativeMember) {
        zenNativeMember.addMethod(new ZenClassMethodDeclare());
    }

    public void writeAll(ClassVisitor newClass, IEnvironmentClass environmentNewClass) {
        String description = method.getSignature();
        newClass.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, method.getName(), description, method.getSignature(), null);
    }

    public class ZenClassMethodDeclare implements IJavaMethod {

        @Override
        public boolean isStatic() {
            return false;
        }

        @Override
        public boolean accepts(int numArguments) {
            return method.getArgumentTypes().length == numArguments;
        }

        @Override
        public boolean accepts(IEnvironmentGlobal environment, Expression... arguments) {
            return accepts(arguments.length) && IntStream.range(0, arguments.length).allMatch(i -> arguments[i].getType().canCastImplicit(method.getArgumentTypes()[i], environment));
        }

        @Override
        public boolean isDeclare() {
            return true;
        }

        @Override
        public int getPriority(IEnvironmentGlobal environment, Expression... arguments) {
            return matchesExact(arguments) ? JavaMethod.PRIORITY_HIGH : accepts(environment, arguments) ? JavaMethod.PRIORITY_LOW : JavaMethod.PRIORITY_INVALID;
        }

        private boolean matchesExact(Expression... arguments) {
            if(!accepts(arguments.length))
                return false;
            for(int i = 0; i < arguments.length; i++) {
                if(arguments[i].getType().toJavaClass() != method.getArgumentTypes()[i].toJavaClass())
                    return false;
            }
            return true;
        }

        @Override
        public void invokeVirtual(MethodOutput output) {
            throw new UnsupportedOperationException("Cannot statically invoke a declare method");

        }

        @Override
        public void invokeStatic(MethodOutput output) {
            throw new UnsupportedOperationException("Cannot statically invoke a declare method");
        }

        @Override
        public ZenType[] getParameterTypes() {
            return method.getArgumentTypes();
        }

        @Override
        public ZenType getReturnType() {
            return method.getReturnType();
        }

        @Override
        public boolean isVarargs() {
            return false;
        }

        @Override
        public String getErrorDescription() {
            final StringBuilder builder = new StringBuilder(method.getName()).append("(");

            for(ZenType zenType : method.getArgumentTypes()) {
                builder.append(zenType.toString()).append(", ");
            }

            final int length = builder.length();
            builder.delete(length - 2, length);

            return builder.append(")").toString();
        }
    }
}
