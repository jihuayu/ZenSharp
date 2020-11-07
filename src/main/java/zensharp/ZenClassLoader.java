package zensharp;

public class ZenClassLoader extends ClassLoader {
    public Class<?> find(String className, byte[] array) {
        return defineClass(className, array, 0, array.length);
    }
}
