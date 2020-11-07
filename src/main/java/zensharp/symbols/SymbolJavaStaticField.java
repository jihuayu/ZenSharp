package zensharp.symbols;

import zensharp.compiler.ITypeRegistry;

import zensharp.util.ZenPosition;
import zensharp.expression.partial.ExpressionJavaStaticField;
import zensharp.expression.partial.IPartialExpression;

import java.lang.reflect.Field;

/**
 * @author Stan
 */
public class SymbolJavaStaticField implements IZenSymbol {
    
    private final Class cls;
    private final Field field;
    private final ITypeRegistry types;
    
    public SymbolJavaStaticField(Class cls, Field field, ITypeRegistry types) {
        this.cls = cls;
        this.field = field;
        this.types = types;
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return new ExpressionJavaStaticField(position, cls, field, types);
    }
    
    @Override
    public String toString() {
        return "SymbolJavaStaticField: " + field.toString();
    }
}
