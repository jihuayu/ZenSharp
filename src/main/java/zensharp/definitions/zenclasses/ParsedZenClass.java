package zensharp.definitions.zenclasses;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import zensharp.ZenTokener;
import zensharp.compiler.*;
import zensharp.definitions.ParsedFunction;
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

public class ParsedZenClass {

    public final ZenPosition position;
    public final String name;
    public final String className;
    public final ZenType parentClassType;
    public final List<ZenType> implClassTypes = new LinkedList<>();

    public final ZenTypeZenClass type;
    private final EnvironmentScript classEnvironment;
    private final List<ParsedClassConstructor> constructors = new LinkedList<>();
    private final List<ParsedZenClassField> statics = new LinkedList<>();
    private final List<ParsedZenClassField> nonStatics = new LinkedList<>();
    private final List<ParsedZenClassMethod> methods = new LinkedList<>();
    private final Map<String, ZenNativeMember> members = new LinkedHashMap<>();
    public Class<?> thisClass;

    public ParsedZenClass(ZenPosition position, String name, String className, EnvironmentScript classEnvironment, ZenType parentClassType, List<ZenType> implClassNames) {
        this.position = position;
        this.parentClassType = parentClassType;
        this.implClassTypes.addAll(implClassNames);
        this.name = name;
        this.className = className;
        this.classEnvironment = classEnvironment;
        this.type = new ZenTypeZenClass(this);
    }


    public static ParsedZenClass parse(ZenTokener parser, IEnvironmentGlobal environmentGlobal) {

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
        ParsedZenClass classTemplate = new ParsedZenClass(position, name, environmentGlobal.makeClassNameWithMiddleName(position.getFile().getClassName() + "_" + name + "_"), classEnvironment, parentClass, implClass);
        classEnvironment.putValue(name, new SymbolType(classTemplate.type), classTemplate.position);

        if (classTemplate.parentClassType instanceof ZenTypeZenClass) {
            classTemplate.members.putAll(((ZenTypeZenClass) classTemplate.parentClassType).zenClass.members);
        }
        else if (classTemplate.parentClassType instanceof ZenTypeNative) {
            classTemplate.members.putAll(((ZenTypeNative) classTemplate.parentClassType).getMembers());
        }

//        for (ZenType i : implClass){
//            if (i instanceof ZenTypeZenInterface){
//                classTemplate.members.putAll(((ZenTypeZenInterface) i).zenClass.getMembers());
//            }
//            else if (i instanceof ZenTypeNative){
//                classTemplate.members.putAll(((ZenTypeNative)i).getMembers());
//            }
//        }

        Token keyword;
        boolean constructorFlag = false;
        while ((keyword = parser.optional(ZenTokener.T_VAL, ZenTokener.T_VAR, ZenTokener.T_STATIC, ZenTokener.T_ZEN_CONSTRUCTOR, ZenTokener.T_FUNCTION)) != null) {
            final int type = keyword.getType();
            switch (type) {
                case ZenTokener.T_VAL:
                case ZenTokener.T_VAR:
                case ZenTokener.T_STATIC:
                    classTemplate.addField(ParsedZenClassField.parse(parser, classEnvironment, type == ZenTokener.T_STATIC, classTemplate.className));
                    break;
                case ZenTokener.T_ZEN_CONSTRUCTOR:
                    classTemplate.addConstructor(ParsedClassConstructor.parse(parser, classEnvironment));
                    constructorFlag = true;
                    break;
                case ZenTokener.T_FUNCTION:
                    classTemplate.addMethod(ParsedZenClassMethod.parse(parser, classEnvironment, classTemplate.className));
            }
        }
        if (!constructorFlag) {
            classTemplate.addConstructor(ParsedClassConstructor.empty());
        }

        parser.required(ZenTokener.T_ACLOSE, "} expected");
        return classTemplate;
    }

    private void addMethod(ParsedZenClassMethod parsedMethod) {
        ParsedFunction method = parsedMethod.method;
        methods.add(parsedMethod);
        if (!members.containsKey(method.getName())) {
            members.put(method.getName(), new ZenNativeMember());
            classEnvironment.putValue(method.getName(), position1 -> new ExpressionThis(position1, type).getMember(position1, classEnvironment, method.getName()), position);
        }
        parsedMethod.addToMember(members.get(method.getName()));
    }

    private void addConstructor(ParsedClassConstructor parsedClassConstructor) {
        constructors.add(parsedClassConstructor);
    }

    private void addField(ParsedZenClassField parsedZenClassField) {
        final String fieldName = parsedZenClassField.name;
        if (!members.containsKey(fieldName))
            members.put(fieldName, new ZenNativeMember());
        parsedZenClassField.addMethodsToMember(members.get(fieldName));
        if (parsedZenClassField.isStatic) {
            statics.add(parsedZenClassField);
            classEnvironment.putValue(fieldName, position1 -> type.getStaticMember(position1, classEnvironment, fieldName), position);
        } else {
            nonStatics.add(parsedZenClassField);
            classEnvironment.putValue(fieldName, position1 -> new ExpressionThis(position1, type).getMember(position1, classEnvironment, fieldName), position);
        }


    }

    public void writeClass(IEnvironmentGlobal environmentGlobal) {
        final ClassWriter newClass = new ZenClassWriter(ClassWriter.COMPUTE_FRAMES);
        newClass.visitSource(position.getFileName(), null);
        String parent = "java/lang/Object";
        if (parentClassType != null) {
            parent = parentClassType.toJavaClass().getName().replace(".", "/");
        }

        String[] impl = implClassTypes.stream().map(i -> i.toJavaClass().getName().replace(".", "/")).toArray(String[]::new);
        newClass.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, className, null, parent, impl);

        final EnvironmentClass environmentNewClass = new EnvironmentClass(newClass, classEnvironment);
        environmentNewClass.putValue("this", position1 -> new ExpressionThis(position1, type), position);

        writeStatics(newClass, environmentNewClass);
        visitNonStatics(newClass);


        writeConstructors(newClass, environmentNewClass);
        writeMethods(newClass, environmentNewClass);
        newClass.visitEnd();

        //ZS ASM STUFF
        byte[] thisClassArray = newClass.toByteArray();
        environmentGlobal.putClass(className, thisClassArray);
        thisClass = environmentGlobal.loader.find(className,thisClassArray);
    }

    private void visitNonStatics(ClassWriter newClass) {
        for (ParsedZenClassField nonStatic : nonStatics) {
            nonStatic.visit(newClass);
        }
    }

    private void writeStatics(ClassWriter newClass, EnvironmentClass environmentNewClass) {
        if (!statics.isEmpty()) {
            final MethodOutput clinit = new MethodOutput(newClass, Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, "<clinit>", "()V", null, null);
            final EnvironmentMethod clinitEnvironment = new EnvironmentMethod(clinit, environmentNewClass);
            clinit.start();
            for (ParsedZenClassField aStatic : statics)
                aStatic.writeAll(clinitEnvironment, newClass, clinit, className);
            clinit.ret();
            clinit.end();
        }
    }

    private void writeConstructors(ClassWriter newClass, EnvironmentClass environmentNewClass) {
        for (ParsedClassConstructor constructor : constructors)
            constructor.writeAll(environmentNewClass, newClass, nonStatics, className, position, parentClassType);
    }

    private void writeMethods(ClassWriter newClass, EnvironmentClass environmentNewClass) {
        for (ParsedZenClassMethod parsedMethod : methods)
            parsedMethod.writeAll(newClass, environmentNewClass);
    }

    public ZenType[] predictCallTypes(int numArguments) {
        for (ParsedClassConstructor con : constructors) {
            if (con.types.length == numArguments)
                return con.types;
        }
        return new ZenType[0];
    }

    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression[] arguments) {
        for (ParsedClassConstructor constructor : constructors) {
            if (constructor.canAccept(arguments, environment))
                return constructor.call(position, arguments, type);
        }
        environment.error("Could not find constructor for " + name + " with " + arguments.length + " arguments.");
        return new ExpressionInvalid(position);
    }

    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name, boolean isStatic) {
        if (members.containsKey(name))
            return isStatic
                    ? members.get(name).instance(position, environment)
                    : members.get(name).instance(position, environment, value);
        environment.error("Could not find " + (isStatic ? "static " : "") + "member " + name);
        return new ExpressionInvalid(position);
    }
}