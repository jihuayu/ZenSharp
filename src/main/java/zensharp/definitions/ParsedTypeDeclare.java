package zensharp.definitions;

import zensharp.ZenTokener;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.parser.Token;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ParsedTypeDeclare {
    private final Token id;
    private final ZenType type;

    public ParsedTypeDeclare(Token id, ZenType type) {
        this.id = id;
        this.type = type;
    }

    public static ParsedTypeDeclare parse(ZenTokener parser, IEnvironmentGlobal environment) {
        parser.next();
        Token tName = parser.required(ZenTokener.T_ID, "identifier expected");
        parser.required(ZenTokener.T_ASSIGN, "= expected");
        ZenType type = ZenType.read(parser,environment);
        parser.required(ZenTokener.T_SEMICOLON, "; expected");
        return new ParsedTypeDeclare(tName,type);
    }

    public ZenType getType() {
        return type;
    }

    public String getName(){
        return id.getValue();
    }

    public ZenPosition getPosition() {
        return id.getPosition();
    }

}
