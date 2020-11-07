package zensharp;


import zensharp.parser.Token;
import zensharp.symbols.IZenSymbol;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.TypeRegistry;

import java.util.List;

/**
 * @author Stanneke
 */
public interface IZenCompileEnvironment {
    
    IZenErrorLogger getErrorLogger();
    
    IZenSymbol getGlobal(String name);
    
    IZenSymbol getBracketed(IEnvironmentGlobal environment, List<Token> tokens);
    
    TypeRegistry getTypeRegistry();
    
    TypeExpansion getExpansion(String type);
    
    void setRegistry(IZenRegistry  registry);
}
