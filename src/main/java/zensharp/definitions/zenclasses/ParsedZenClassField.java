package zensharp.definitions.zenclasses;

import org.objectweb.asm.*;
import zensharp.ZenTokener;

import zensharp.expression.Expression;
import zensharp.parser.expression.ParsedExpression;
import zensharp.type.ZenType;

import zensharp.util.MethodOutput;
import zensharp.compiler.EnvironmentScript;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.type.natives.IJavaMethod;
import zensharp.type.natives.JavaMethod;
import zensharp.type.natives.ZenNativeMember;

public class ParsedZenClassField {

    public final boolean isStatic;
    public final ParsedExpression initializer;
    public final String name;
    private final String ownerName;
    public ZenType type;

    public ParsedZenClassField(boolean isStatic, ZenType type, ParsedExpression initializer, String name, String ownerName) {

        this.isStatic = isStatic;
        this.type = type;
        this.initializer = initializer;
        this.name = name;
        this.ownerName = ownerName;
    }

    public static ParsedZenClassField parse(ZenTokener parser, EnvironmentScript classEnvironment, boolean isStatic, String ownerName) {
        String name = parser.required(ZenTokener.T_ID, "Identifier expected").getValue();
        ZenType type = ZenType.ANY;
        if(parser.optional(ZenTokener.T_AS) != null) {
            type = ZenType.read(parser, classEnvironment);
        }
        ParsedExpression initializer = null;
        if(parser.optional(ZenTokener.T_ASSIGN) != null) {
            initializer = ParsedExpression.read(parser, classEnvironment);
        }
        parser.required(ZenTokener.T_SEMICOLON, "; expected");


        return new ParsedZenClassField(isStatic, type, initializer, name, ownerName);

    }

    void addMethodsToMember(ZenNativeMember zenNativeMember) {
        zenNativeMember.setGetter(new ZenClassFieldMethod(false));
        zenNativeMember.setSetter(new ZenClassFieldMethod(true));
    }

    public boolean hasInitializer() {
        return initializer != null;
    }

    public void visit(ClassWriter newClass) {
        newClass.visitField(!isStatic ? Opcodes.ACC_PUBLIC : (Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC), name, type.toASMType().getDescriptor(), type.getSignature(), null).visitEnd();
    }

    public void writeAll(IEnvironmentMethod clinitEnvironment, ClassWriter newClass, MethodOutput clinit, String className) {
        final Expression expression = hasInitializer() ? initializer.compile(clinitEnvironment, type).eval(clinitEnvironment) : null;
        if(type == ZenType.ANY && expression != null)
            type = expression.getType();
        visit(newClass);

        if(expression != null) {
            expression.compile(true, clinitEnvironment);
            clinit.putStaticField(className, name, type.toASMType().getDescriptor());
        }
    }

    private final class ZenClassFieldMethod implements IJavaMethod {

        private final boolean isSetter;


        private ZenClassFieldMethod(boolean isSetter) {

            this.isSetter = isSetter;
        }

        @Override
        public boolean isDeclare() {
            return false;
        }

        @Override
        public boolean isStatic() {
            return ParsedZenClassField.this.isStatic;
        }

        @Override
        public boolean accepts(int numArguments) {
            return numArguments == (isSetter ? 1 : 0);
        }

        @Override
        public boolean accepts(IEnvironmentGlobal environment, Expression... arguments) {
            return accepts(arguments.length) && (!isSetter || arguments[0].getType().canCastImplicit(ParsedZenClassField.this.type, environment));
        }

        @Override
        public int getPriority(IEnvironmentGlobal environment, Expression... arguments) {
            return accepts(environment, arguments) ? JavaMethod.PRIORITY_LOW : JavaMethod.PRIORITY_INVALID;
        }

        @Override
        public void invokeVirtual(MethodOutput output) {
            if(isStatic())
                if(isSetter)
                    output.putStaticField(ParsedZenClassField.this.ownerName, ParsedZenClassField.this.name, ParsedZenClassField.this.type.toASMType().getDescriptor());
                else
                    output.getStaticField(ParsedZenClassField.this.ownerName, ParsedZenClassField.this.name, ParsedZenClassField.this.type.toASMType().getDescriptor());
            else {
                if(isSetter)
                    output.putField(ParsedZenClassField.this.ownerName, ParsedZenClassField.this.name, ParsedZenClassField.this.type.toASMType().getDescriptor());
                else
                    output.getField(ParsedZenClassField.this.ownerName, ParsedZenClassField.this.name, ParsedZenClassField.this.type.toASMType().getDescriptor());
            }
        }

        @Override
        public void invokeStatic(MethodOutput output) {
            if(!isStatic())
                throw new IllegalArgumentException("Cannot invoke nonstatic method from a static context");
            output.getStaticField(ParsedZenClassField.this.ownerName, ParsedZenClassField.this.name, ParsedZenClassField.this.type.toASMType().getDescriptor());
        }

        @Override
        public ZenType[] getParameterTypes() {
            return isSetter ? new ZenType[]{type} : new ZenType[0];
        }

        @Override
        public ZenType getReturnType() {
            return isSetter ? ZenType.VOID : ParsedZenClassField.this.type;
        }

        @Override
        public boolean isVarargs() {
            return false;
        }
    
        @Override
        public String getErrorDescription() {
            return "INTERNAL METHOD";
        }
    }
}
