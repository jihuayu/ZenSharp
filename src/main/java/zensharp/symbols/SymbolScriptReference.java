package zensharp.symbols;



import zensharp.compiler.IEnvironmentGlobal;
import zensharp.util.ZenPosition;
import zensharp.expression.partial.IPartialExpression;
import zensharp.expression.partial.PartialScriptReference;

public class SymbolScriptReference implements IZenSymbol {
    private final PartialScriptReference instance;
    
    
    private SymbolScriptReference() {
        this.instance = new PartialScriptReference();
    }
    
    public SymbolScriptReference(PartialScriptReference instance) {
        this.instance = instance;
    }
    
    @Override
    public PartialScriptReference instance(ZenPosition position) {
        return instance;
    }
    
    public static PartialScriptReference getOrCreateReference(IEnvironmentGlobal environmentGlobal) {
        IPartialExpression reference = environmentGlobal.getValue("scripts", null);
        if (reference == null) {
            environmentGlobal.putValue("scripts", new SymbolScriptReference(), null);
            reference = environmentGlobal.getValue("scripts", null);
        }
        return (PartialScriptReference) reference;
    }
}
