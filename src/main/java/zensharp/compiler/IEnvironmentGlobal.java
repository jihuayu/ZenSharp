package zensharp.compiler;


import zensharp.IZenCompileEnvironment;
import zensharp.IZenErrorLogger;
import zensharp.TypeExpansion;
import zensharp.ZenClassLoader;
import zensharp.expression.partial.IPartialExpression;
import zensharp.symbols.IZenSymbol;
import zensharp.util.ZenPosition;

import java.util.Set;

/**
 * @author Stan
 */
public interface IEnvironmentGlobal extends ITypeRegistry, IZenErrorLogger {
    ZenClassLoader loader = new ZenClassLoader();

    IZenCompileEnvironment getEnvironment();
    
    TypeExpansion getExpansion(String name);

    String makeClassName();
    
    String makeClassNameWithMiddleName(String middleName);
    
    boolean containsClass(String name);
    
    Set<String> getClassNames();
    
    byte[] getClass(String name);
    
    void putClass(String name, byte[] data);
    
    IPartialExpression getValue(String name, ZenPosition position);
    
    void putValue(String name, IZenSymbol value, ZenPosition position);
}
