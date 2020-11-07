package zensharp.compiler;

import zensharp.symbols.SymbolLocal;
import zensharp.util.MethodOutput;

/**
 * @author Stan
 */
public interface IEnvironmentMethod extends IEnvironmentClass {
    
    MethodOutput getOutput();
    
    int getLocal(SymbolLocal variable);
}
