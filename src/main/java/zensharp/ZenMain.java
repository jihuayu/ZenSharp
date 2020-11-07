package zensharp;

import zensharp.impl.GenericCompileEnvironment;
import zensharp.impl.GenericErrorLogger;
import zensharp.impl.GenericRegistry;

import java.util.*;

public class ZenMain {
    public static void println(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        final IZenCompileEnvironment environment = new GenericCompileEnvironment();
        final IZenErrorLogger errorLogger = new GenericErrorLogger(System.err);
        final GenericRegistry genericRegistry = new GenericRegistry(environment, errorLogger);

        genericRegistry.registerGlobal("println", genericRegistry.getStaticFunction(ZenMain.class, "println", String.class));
        genericRegistry.registerNativeClass(ZenTestClassA.class);
        genericRegistry.registerNativeClass(ZenTestClassA.PT.class);

        try {
            final StringJoiner builder = new StringJoiner("\n");
            builder.add("import test.Pt;");
            builder.add("zenClass As extends Pt{}");
            builder.add("var tt = As();");
            builder.add("tt.t();");
            //final String script = "println('hello');";
            final String script = builder.toString();
            ZenModule module = ZenModule.compileScriptString(script, "test.zs", environment, ZenMain.class.getClassLoader());
            Runnable runnable = module.getMain();
            if(runnable != null) {
                runnable.run();
            }
        } catch(Throwable ex) {
            genericRegistry.getErrorLogger().error("Error executing: test.zs: " + ex.getMessage(), ex);
        }
    }
}
