package zensharp.symbols;

import org.objectweb.asm.*;
import zensharp.compiler.IEnvironmentMethod;
import zensharp.definitions.ParsedGlobalValue;

import zensharp.type.ZenType;
import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialGlobalValue;

public class SymbolGlobalValue implements IZenSymbol {
    
    private final ParsedGlobalValue value;
    private final PartialGlobalValue instance;
    
    
    public SymbolGlobalValue(ParsedGlobalValue value, IEnvironmentMethod environmentClass) {
        this.value = value;
        this.instance = new PartialGlobalValue(this);
        
        initField(environmentClass.getClassOutput());
        compileGlobal(environmentClass);
    }
    
    @Override
    public IPartialExpression instance(ZenPosition position) {
        return instance;
    }
    
    public String getName() {
        return value.getName();
    }
    
    
    public ZenType getType() {
        return value.getType();
    }
    
    public ParsedGlobalValue getValue() {
        return value;
    }
    
    public String getOwner() {
        return value.getOwner();
    }
    
    public String getASMDescriptor() {
        return getType().toASMType().getDescriptor();
    }
    
    public ZenPosition getPosition() {
        return value.getPosition();
    }
    
    
    /**
     * Initializes the Public Static Final field `name`
     * Does not give any value to it.
     * Needs to be run before compileGlobal!
     *
     * @param visitor class visitor visiting the generated script class
     */
    
    private void initField(ClassVisitor visitor) {
        visitor.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, getName(), getASMDescriptor(), null, null).visitEnd();
    }
    
    /**
     * Sets the Public Static Final field `name`'s initial value.
     *
     * @param clinitMethodEnvironment a method environment that refers to the generated script class' {@code<clinit>} method!
     */
    private void compileGlobal(IEnvironmentMethod clinitMethodEnvironment) {
        value.getValue().compile(clinitMethodEnvironment, getType()).eval(clinitMethodEnvironment).cast(getPosition(), clinitMethodEnvironment, getType()).compile(true, clinitMethodEnvironment);
        clinitMethodEnvironment.getOutput().putStaticField(getOwner(), getName(), getASMDescriptor());
    }
}
