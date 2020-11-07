package zensharp.compiler;

import zensharp.type.ZenType;

import java.lang.reflect.Type;

/**
 * @author Stan
 */
public interface ITypeRegistry {
    
    ZenType getType(Type type);
}
