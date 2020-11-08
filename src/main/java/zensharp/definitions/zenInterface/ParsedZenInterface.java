package zensharp.definitions.zenInterface;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import zensharp.ZenTokener;
import zensharp.compiler.*;
import zensharp.definitions.IGetGenerics;
import zensharp.definitions.ParsedFunctionDeclare;
import zensharp.definitions.ParsedGeneric;
import zensharp.expression.ExpressionInvalid;
import zensharp.expression.ExpressionThis;
import zensharp.expression.partial.IPartialExpression;
import zensharp.parser.Token;
import zensharp.symbols.SymbolGeneric;
import zensharp.symbols.SymbolType;
import zensharp.type.*;
import zensharp.type.natives.ZenNativeMember;
import zensharp.util.Pair;
import zensharp.util.ZenPosition;

import java.util.*;
import java.util.stream.Collectors;

public class ParsedZenInterface implements IGetGenerics {

    public final ZenPosition position;
    public final String name;
    public final String className;

    public final List<ZenType> implClassTypes = new LinkedList<>();
    public final ZenTypeZenInterface type;

    private final EnvironmentScript classEnvironment;

    private final List<Pair<Token, ZenType>> generics = new LinkedList<>();
    public Class<?> thisClass;
    private final List<ParsedZenInterfaceMethod> methods = new LinkedList<>();
    private final Map<String, ZenNativeMember> members = new LinkedHashMap<>();

    public ParsedZenInterface(ZenPosition position, String name, String className, EnvironmentScript classEnvironment, List<ZenType> implClassNames,List<Pair<Token, ZenType>> generics) {
        this.generics.addAll(generics);
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
        List<Pair<Token, ZenType>> generics = new LinkedList<>();
        if (parser.optional(ZenTokener.T_LT) != null) {
            ParsedGeneric g = ParsedGeneric.parse(parser,environmentGlobal);
            generics.add(new Pair<>(g.getToken(), g.getType()));
            while (parser.optional(ZenTokener.T_COMMA) != null) {
                g = ParsedGeneric.parse(parser,environmentGlobal);
                generics.add(new Pair<>(g.getToken(), g.getType()));
            }
            parser.required(ZenTokener.T_GT, "> required");
        }
        for (Pair<Token,ZenType> i : generics) {
            ZenType type = i.getValue();
            Token token = i.getKey();
            classEnvironment.putValue(token.getValue(), new SymbolType(type), token.getPosition());
        }

        List<ZenType> implClass = new ArrayList<>();
        if (parser.isNext(ZenTokener.T_ZEN_EXTEND)) {
            parser.next();
            ZenType type = ZenType.read(parser, classEnvironment);
            implClass.add(type);
            if (parser.optional(ZenTokener.T_COMMA) != null) {
                type = ZenType.read(parser, classEnvironment);
                implClass.add(type);
            }
        }
        parser.required(ZenTokener.T_AOPEN, "{ expected");

        final String name = id.getValue();
        final ZenPosition position = id.getPosition();
        ParsedZenInterface classTemplate = new ParsedZenInterface(position, name,
                environmentGlobal.makeClassNameWithMiddleName(position.getFile().getClassName() + "_" + name + "_"),
                classEnvironment, implClass,generics);
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
        String parentSign = "Ljava/lang/Object;";

        StringBuilder generic = new StringBuilder();
        for (Pair<Token, ZenType> i : generics) {
            generic.append(i.getKey().getValue());
            generic.append(":");
            ZenType type = i.getValue();
            generic.append(type.getSignature());
        }
        String sign = "<" + generic.toString() + ">" + parentSign;
        //TODO:接口的 sign
        String[] impl = implClassTypes.stream().map(i -> i.toJavaClass().getName().replace(".", "/")).toArray(String[]::new);
        newClass.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC +  + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE, className, sign, "java/lang/Object", impl);

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

    public List<Pair<Token, ZenType>> getGenerics() {
        return generics;
    }
}
