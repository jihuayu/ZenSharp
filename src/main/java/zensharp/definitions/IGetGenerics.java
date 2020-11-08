package zensharp.definitions;

import zensharp.parser.Token;
import zensharp.type.ZenType;
import zensharp.util.Pair;

import java.util.List;

public interface IGetGenerics {
    List<Pair<Token, ZenType>> getGenerics();
}
