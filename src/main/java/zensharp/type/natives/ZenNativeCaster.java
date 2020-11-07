package zensharp.type.natives;

import org.objectweb.asm.Label;
import zensharp.compiler.IEnvironmentGlobal;

import zensharp.dump.IDumpConvertable;
import zensharp.dump.IDumpable;
import zensharp.dump.types.DumpIJavaMethod;
import zensharp.type.ZenType;
import zensharp.type.casting.CastingRuleVirtualMethod;
import zensharp.type.casting.ICastingRuleDelegate;
import zensharp.util.MethodOutput;

import java.util.*;

/**
 * @author Stanneke
 */
public class ZenNativeCaster implements IDumpConvertable {

    private final IJavaMethod method;

    public ZenNativeCaster(IJavaMethod method) {
        this.method = method;
    }

    public ZenType getReturnType() {
        return method.getReturnType();
    }

    public void constructCastingRule(ICastingRuleDelegate rules) {
        ZenType type = getReturnType();
        rules.registerCastingRule(type, new CastingRuleVirtualMethod(method));
    }

    public void compile(MethodOutput output) {
        if (method.isStatic()) {
            method.invokeStatic(output);
        } else {
            method.invokeVirtual(output);
        }
    }

    public void compileAnyCanCastImplicit(ZenType type, MethodOutput output, IEnvironmentGlobal environment, int localClass) {
        String casterAny = method.getReturnType().getAnyClassName(environment);
        if (casterAny == null) {
            // TODO: make sure no type ever does this
            return;
        }

        Label skip = new Label();
        output.loadObject(localClass);
        output.invokeStatic(casterAny, "rtCanCastImplicit", "(Ljava/lang/Class;)Z");
        output.ifEQ(skip);
        output.iConst1();
        output.returnInt();
        output.label(skip);
    }

    public void compileAnyCast(ZenType type, MethodOutput output, IEnvironmentGlobal environment, int localValue, int localClass) {
        Label skip = new Label();
        output.loadObject(localClass);
        output.constant(method.getReturnType().toASMType());
        output.ifACmpNe(skip);
        output.load(type.toASMType(), localValue);

        compile(output);

        output.returnType(method.getReturnType().toASMType());
        output.label(skip);

        String casterAny = method.getReturnType().getAnyClassName(environment);
        if (casterAny == null)
            // TODO: make sure this isn't necessary
            return;

        Label skip2 = new Label();
        output.loadObject(localClass);
        output.invokeStatic(casterAny, "rtCanCastImplicit", "(Ljava/lang/Class;)Z");
        output.ifEQ(skip2);
        output.load(type.toASMType(), localValue);

        compile(output);

        output.returnType(method.getReturnType().toASMType());
        output.label(skip2);
    }
    
    @Override
    public List<? extends IDumpable> asDumpedObject() {
        return Collections.singletonList(new DumpIJavaMethod(method));
    }
}
