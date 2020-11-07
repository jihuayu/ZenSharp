package zensharp.type.casting;

import org.objectweb.asm.Type;
import org.objectweb.asm.*;



import zensharp.compiler.EnvironmentMethod;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.compiler.ZenClassWriter;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeFunction;
import zensharp.type.ZenTypeFunctionCallable;
import zensharp.util.MethodOutput;
import zensharp.util.ZenTypeUtil;

import java.lang.reflect.*;


/**
 * @author Stan
 */
public class CastingRuleMatchedFunction implements ICastingRule {
    
    private final ZenTypeFunction fromType;
    private final ZenType toType;
    private final ICastingRule returnCastingRule;
    private final ICastingRule[] argumentCastingRules;
    
    public CastingRuleMatchedFunction(ZenTypeFunction fromType, ZenType toType, ICastingRule returnCastingRule, ICastingRule[] argumentCastingRules) {
        this.fromType = fromType;
        this.toType = toType;
        this.returnCastingRule = returnCastingRule;
        this.argumentCastingRules = argumentCastingRules;
    }
    
    @Override
    public void compile(IEnvironmentMethod outerEnvironment) {
        final Class<?> aClass = toType.toJavaClass();
        final Method method = ZenTypeUtil.findFunctionalInterfaceMethod(aClass);
        if(method == null) {
            outerEnvironment.error("Internal error: Cannot convert from " + fromType + " to " + toType + " because latter is not a functional interface!");
            return;
        }
    
        final String className = outerEnvironment.makeClassNameWithMiddleName("generated_bridge_class");
        
        final ZenClassWriter classWriter = new ZenClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", new String[]{Type.getInternalName(aClass)});
        classWriter.visitSource("generated_classfile", null);
        classWriter.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "wrappedFun", fromType.getSignature(), null, null)
                .visitEnd();
        {
            final MethodOutput bridgeOutput = new MethodOutput(classWriter, Opcodes.ACC_PUBLIC, "<init>", "(" + fromType
                    .getSignature() + ")V", null, null);
            bridgeOutput.start();
            bridgeOutput.loadObject(0);
            bridgeOutput.invokeSpecial("java/lang/Object", "<init>", "()V");
            bridgeOutput.loadObject(0);
            bridgeOutput.loadObject(1);
            bridgeOutput.putField(className, "wrappedFun", fromType.getSignature());
            bridgeOutput.ret();
            bridgeOutput.end();
        }
        
        {
            final MethodOutput output = new MethodOutput(classWriter, Opcodes.ACC_PUBLIC, method.getName(), Type
                    .getMethodDescriptor(method), null, null);
            final EnvironmentMethod environment = new EnvironmentMethod(output, outerEnvironment);
            output.start();
            output.loadObject(0);
            output.getField(className, "wrappedFun", fromType.getSignature());
            int i = 0;
            for(Class<?> parameterType : method.getParameterTypes()) {
                output.load(Type.getType(parameterType), i + 1);
                final ICastingRule argumentCastingRule = argumentCastingRules[i];
                if(argumentCastingRule != null) {
                    argumentCastingRule.compile(environment);
                }
                i++;
            }
            output.invokeInterface(Type.getType(fromType.getSignature()).getInternalName(), "accept", ((ZenTypeFunctionCallable) fromType)
                    .getDescriptor());
            if(returnCastingRule != null) {
                returnCastingRule.compile(environment);
            }
            if(method.getReturnType() != void.class) {
                output.returnType(Type.getType(method.getReturnType()));
            }
            output.ret();
            output.end();
        }
        classWriter.visitEnd();
        outerEnvironment.putClass(className, classWriter.toByteArray());
    
        {
            final MethodOutput output = outerEnvironment.getOutput();
            output.newObject(className);
            output.dupX1();
            output.swap();
            output.invokeSpecial(className, "<init>", "(" + fromType.getSignature() + ")V");
        }
    }
    
    @Override
    public ZenType getInputType() {
        return fromType;
    }
    
    @Override
    public ZenType getResultingType() {
        return toType;
    }
}
