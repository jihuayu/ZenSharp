package zensharp.definitions.zenInterface;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import zensharp.ZenTokener;
import zensharp.compiler.*;
import zensharp.definitions.ParsedFunction;
import zensharp.definitions.ParsedFunctionDeclare;
import zensharp.definitions.zenclasses.ParsedClassConstructor;
import zensharp.definitions.zenclasses.ParsedZenClass;
import zensharp.definitions.zenclasses.ParsedZenClassField;
import zensharp.definitions.zenclasses.ParsedZenClassMethod;
import zensharp.expression.Expression;
import zensharp.expression.ExpressionInvalid;
import zensharp.expression.ExpressionThis;
import zensharp.expression.partial.IPartialExpression;
import zensharp.parser.Token;
import zensharp.symbols.SymbolType;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeNative;
import zensharp.type.ZenTypeZenClass;
import zensharp.type.ZenTypeZenInterface;
import zensharp.type.natives.ZenNativeMember;
import zensharp.util.MethodOutput;
import zensharp.util.ZenPosition;

import java.util.*;

public class ParsedZenInterface {

    public final ZenPosition position;
    public final String name;
    public final String className;

    public final List<ZenType> implClassTypes = new LinkedList<>();
    public final ZenTypeZenInterface type;

    private final EnvironmentScript classEnvironment;

    public Class<?> thisClass;
    private final List<ParsedZenInterfaceMethod> methods = new LinkedList<>();
    private final Map<String, ZenNativeMember> members = new LinkedHashMap<>();

    public ParsedZenInterface(ZenPosition position, String name, String className, EnvironmentScript classEnvironment, ZenType parentClassType, List<ZenType> implClassNames) {
        this.position = position;
        this.implClassTypes.addAll(implClassNames);
        this.name = name;
        this.className = className;
        this.classEnvironment = classEnvironment;
        this.type = new ZenTypeZenInterface(this);
    }


    public static ParsedZenInterface parse(ZenTokener parser, IEnvironmentGlobal environmentGlobal) {

        EnvironmentScript classEnvironment = new EnvironmentScript(environmentGlobal);
        parser.next();

        final Token id = parser.required(ZenTokener.T_ID, "ClassName required");
        ZenType parentClass = null;
        List<ZenType> implClass = new ArrayList<>();
        if (parser.isNext(ZenTokener.T_ZEN_EXTEND)) {
            parser.next();
            ZenType type = ZenType.read(parser, environmentGlobal);
            parentClass = type;
        }
        if (parser.isNext(ZenTokener.T_ZEN_IMPL)) {
            parser.next();
            ZenType type = ZenType.read(parser, environmentGlobal);
            implClass.add(type);
            if (parser.optional(ZenTokener.T_COMMA) != null) {
                type = ZenType.read(parser, environmentGlobal);
                implClass.add(type);
            }
        }
        parser.required(ZenTokener.T_AOPEN, "{ expected");

        final String name = id.getValue();
        final ZenPosition position = id.getPosition();
        ParsedZenInterface classTemplate = new ParsedZenInterface(position, name, environmentGlobal.makeClassNameWithMiddleName(position.getFile().getClassName() + "_" + name + "_"), classEnvironment, parentClass, implClass);
        classEnvironment.putValue(name, new SymbolType(classTemplate.type), classTemplate.position);
        for (ZenType i : classTemplate.implClassTypes){
            if (i instanceof ZenTypeZenInterface){
                classTemplate.members.putAll(((ZenTypeZenInterface) i).zenClass.members);
            }
            if (i instanceof ZenTypeNative){
                classTemplate.members.putAll(((ZenTypeNative) i).getMembers());
            }
        }


        Token keyword;
        boolean constructorFlag = false;
        while ((keyword = parser.optional(ZenTokener.T_VAL, ZenTokener.T_VAR, ZenTokener.T_STATIC, ZenTokener.T_ZEN_CONSTRUCTOR, ZenTokener.T_FUNCTION)) != null) {
            final int type = keyword.getType();
            switch (type) {
                case ZenTokener.T_VAL:
                case ZenTokener.T_VAR:
                case ZenTokener.T_STATIC:
                case ZenTokener.T_ZEN_CONSTRUCTOR:
                    break;
                case ZenTokener.T_FUNCTION:
                    classTemplate.addMethod(ParsedZenInterfaceMethod.parse(parser, classEnvironment, classTemplate.className));
            }
        }


        parser.required(ZenTokener.T_ACLOSE, "} expected");
        return classTemplate;
    }
    private void addMethod(ParsedZenInterfaceMethod parsedMethod) {
        ParsedFunctionDeclare method = parsedMethod.method;
        methods.add(parsedMethod);
        if (!members.containsKey(method.getName())) {
            members.put(method.getName(), new ZenNativeMember());
            classEnvironment.putValue(method.getName(), position1 -> new ExpressionThis(position1, type).getMember(position1, classEnvironment, method.getName()), position);
        }
        parsedMethod.addToMember(members.get(method.getName()));
    }

    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name, boolean isStatic) {
        if (members.containsKey(name))
            return isStatic
                    ? members.get(name).instance(position, environment)
                    : members.get(name).instance(position, environment, value);
        environment.error("Could not find " + (isStatic ? "static " : "") + "member " + name);
        return new ExpressionInvalid(position);
    }

    public Map<String, ZenNativeMember> getMembers() {
        return members;
    }

    public void writeClass(IEnvironmentGlobal environmentGlobal) {
        final ClassWriter newClass = new ZenClassWriter(ClassWriter.COMPUTE_FRAMES);
        newClass.visitSource(position.getFileName(), null);

        String[] impl = implClassTypes.stream().map(i -> i.toJavaClass().getName().replace(".", "/")).toArray(String[]::new);
        newClass.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC +  + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE, className, null, "java/lang/Object", impl);

        final EnvironmentClass environmentNewClass = new EnvironmentClass(newClass, classEnvironment);
        environmentNewClass.putValue("this", position1 -> new ExpressionThis(position1, type), position);


        writeMethods(newClass, environmentNewClass);
        newClass.visitEnd();

        //ZS ASM STUFF
        byte[] thisClassArray = newClass.toByteArray();
        environmentGlobal.putClass(className, thisClassArray);
        thisClass = environmentGlobal.loader.find(className,thisClassArray);
    }
    private void writeMethods(ClassWriter newClass, EnvironmentClass environmentNewClass) {
        for (ParsedZenInterfaceMethod parsedMethod : methods)
            parsedMethod.writeAll(newClass, environmentNewClass);
    }
}
