package zensharp;

import zensharp.impl.GenericCompileEnvironment;
import zensharp.impl.GenericErrorLogger;
import zensharp.impl.GenericRegistry;

import java.io.File;

public class ZenShell {

    public static void println(Object s) {
        System.out.println(s);
    }


    public static void main(String[] args) {
        if (args.length==0)
        {
            System.out.println("Need args!");
            return;
        }

//        for (int i = 0;i<ZenTokener.FINALS.length;i++){
//            if (ZenTokener.REGEXPS[i].equals(">")){
//                System.out.println(ZenTokener.FINALS[i]);
//            }
//        }
        final IZenCompileEnvironment environment = new GenericCompileEnvironment();
        final IZenErrorLogger errorLogger = new GenericErrorLogger(System.err);
        final GenericRegistry genericRegistry = new GenericRegistry(environment, errorLogger);

        genericRegistry.registerGlobal("print", genericRegistry.getStaticFunction(ZenShell.class, "println", Object.class));
        genericRegistry.registerNativeClass(ZenTestClassA.class);

        try {
            ZenModule module = ZenModule.compileScriptFile(new File(args[0]),  environment, ZenMain.class.getClassLoader());
            Runnable runnable = module.getMain();
            if(runnable != null) {
                runnable.run();
            }
        } catch(Throwable ex) {
            genericRegistry.getErrorLogger().error("Error executing: " + ex.getMessage(), ex);
        }
    }
}
