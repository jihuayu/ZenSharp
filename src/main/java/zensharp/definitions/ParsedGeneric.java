package zensharp.definitions;

import zensharp.ZenTokener;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.parser.Token;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ParsedGeneric {
    private final Token token;
    private final ZenType type;

    public ParsedGeneric(Token Token, ZenType type) {
        this.token = Token;
        this.type = type;
    }

    public static ParsedGeneric parse(ZenTokener parser, IEnvironmentGlobal environment) {
        Token tName = parser.required(ZenTokener.T_ID, "Generic Name required");
        ZenType type = ZenType.Object;
        if (parser.optional(ZenTokener.T_ZEN_EXTEND) != null) {
            type = ZenType.read(parser, environment);
        }

        return new ParsedGeneric(tName,type);
    }


    public ZenType getType() {
        return type;
    }

    public String getName(){
        return token.getValue();
    }

    public ZenPosition getPosition() {
        return token.getPosition();
    }

    public Token getToken() {
        return token;
    }
}
