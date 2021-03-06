package zensharp;


import zensharp.impl.IBracketHandler;
import zensharp.parser.Token;

import zensharp.util.Pair;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.compiler.TypeRegistry;
import zensharp.symbols.IZenSymbol;
import zensharp.symbols.SymbolPackage;

import java.util.*;

public interface IZenRegistry {
    
    void registerGlobal(String name, IZenSymbol symbol);
    
    void registerExpansion(Class<?> cls);
    
    void registerBracketHandler(IBracketHandler handler);
    
    void removeBracketHandler(IBracketHandler handler);
    
    void registerNativeClass(Class<?> cls);
    
    IZenSymbol getStaticFunction(Class cls, String name, Class... arguments);
    
    IZenSymbol getStaticField(Class cls, String name);
    
    IZenSymbol resolveBracket(IEnvironmentGlobal environment, List<Token> tokens);
    
    IEnvironmentGlobal makeGlobalEnvironment(Map<String, byte[]> classes);
    
    IZenCompileEnvironment getCompileEnvironment();
    
    Map<String, IZenSymbol> getGlobals();
    
    Set<Pair<Integer, IBracketHandler>> getBracketHandlers();
    
    TypeRegistry getTypes();
    
    SymbolPackage getRoot();
    
    Map<String, TypeExpansion> getExpansions();
    
    void setCompileEnvironment(IZenCompileEnvironment compileEnvironment);
    
    void setGlobals(Map<String, IZenSymbol> globals);
    
    void setBracketHandlers(Set<Pair<Integer, IBracketHandler>> bracketHandlers);
    
    void setTypes(TypeRegistry types);
    
    void setRoot(SymbolPackage root);
    
    void setExpansions(Map<String, TypeExpansion> expansions);
    
    IZenErrorLogger getErrorLogger();
    
    void setErrorLogger(IZenErrorLogger errorLogger);
    
    IZenLogger getLogger();
    
    void setLogger(IZenLogger logger);
    
}
